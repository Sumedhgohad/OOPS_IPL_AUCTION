package com.ipl.auction.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Minimal helper around the Gemini REST API for single-turn and multi-turn chat prompts.
 */
public class GeminiChatService {

    private static final String API_ROOT = "https://generativelanguage.googleapis.com/v1beta/models/";
    private static final String DEFAULT_MODEL = "gemini-pro";

    private final String apiKey;
    private final String model;

    public GeminiChatService() {
        this(System.getenv("GEMINI_API_KEY"), System.getenv("GEMINI_MODEL"));
    }

    public GeminiChatService(String apiKey) {
        this(apiKey, null);
    }

    public GeminiChatService(String apiKey, String model) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("Set GEMINI_API_KEY environment variable with your Gemini API key.");
        }
        this.apiKey = apiKey.trim();
        String chosenModel = model == null || model.trim().isEmpty() ? DEFAULT_MODEL : model.trim();
        this.model = sanitizeModel(chosenModel);
    }

    public String generateReply(List<ChatMessage> history) throws IOException {
    List<ChatMessage> safeHistory = history == null ? Collections.emptyList() : new ArrayList<>(history);
        String payload = buildPayload(safeHistory);
    HttpURLConnection connection = openConnection();

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
        }

        int status = connection.getResponseCode();
        InputStream responseStream = status >= 200 && status < 300
            ? connection.getInputStream()
            : connection.getErrorStream();

        String responseBody = readFully(responseStream);
        if (status < 200 || status >= 300) {
            throw new IOException("Gemini API error (" + status + "): " + responseBody);
        }

        return extractFirstText(responseBody);
    }

    private HttpURLConnection openConnection() throws IOException {
        String endpoint = API_ROOT + model + ":generateContent?key=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8.name());
    URL url = URI.create(endpoint).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(30000);
        return connection;
    }

    private String buildPayload(List<ChatMessage> history) {
        StringBuilder json = new StringBuilder();
        json.append("{\"contents\":[");
        for (int i = 0; i < history.size(); i++) {
            ChatMessage message = history.get(i);
            if (message == null || message.text == null || message.text.trim().isEmpty()) {
                continue;
            }
            if (json.charAt(json.length() - 1) != '[') {
                json.append(',');
            }
            json.append("{\"role\":\"")
                .append(message.role)
                .append("\",\"parts\":[{\"text\":\"")
                .append(escapeJson(message.text))
                .append("\"}]}");
        }
        json.append("]}");
        return json.toString();
    }

    private String escapeJson(String value) {
        StringBuilder escaped = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '\"':
                    escaped.append("\\\"");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    if (c < 32) {
                        escaped.append(String.format("\\u%04x", (int) c));
                    } else {
                        escaped.append(c);
                    }
            }
        }
        return escaped.toString();
    }

    private String sanitizeModel(String value) {
        String trimmed = value.replaceAll("\\s+", "");
        if (trimmed.isEmpty()) {
            return DEFAULT_MODEL;
        }
        return trimmed;
    }

    private String readFully(InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
    }

    private String extractFirstText(String responseBody) throws IOException {
        if (responseBody == null || responseBody.isEmpty()) {
            throw new IOException("Empty response from Gemini API.");
        }
        int markerPos = responseBody.indexOf("\"text\"");
        while (markerPos != -1) {
            int colonPos = responseBody.indexOf(':', markerPos);
            if (colonPos == -1) {
                break;
            }
            int quoteStart = -1;
            for (int i = colonPos + 1; i < responseBody.length(); i++) {
                char c = responseBody.charAt(i);
                if (Character.isWhitespace(c)) {
                    continue;
                }
                if (c == '"') {
                    quoteStart = i + 1;
                    break;
                }
                quoteStart = -1;
                break;
            }
            if (quoteStart == -1) {
                markerPos = responseBody.indexOf("\"text\"", markerPos + 6);
                continue;
            }

            StringBuilder result = new StringBuilder();
            boolean escaping = false;
            for (int i = quoteStart; i < responseBody.length(); i++) {
                char c = responseBody.charAt(i);
                if (escaping) {
                    switch (c) {
                        case 'n':
                            result.append('\n');
                            break;
                        case 'r':
                            result.append('\r');
                            break;
                        case 't':
                            result.append('\t');
                            break;
                        case '\\':
                            result.append('\\');
                            break;
                        case '"':
                            result.append('"');
                            break;
                        case 'u':
                            if (i + 4 < responseBody.length()) {
                                String hex = responseBody.substring(i + 1, i + 5);
                                try {
                                    int codePoint = Integer.parseInt(hex, 16);
                                    result.append((char) codePoint);
                                    i += 4;
                                } catch (NumberFormatException e) {
                                    result.append('?');
                                }
                            }
                            break;
                        default:
                            result.append(c);
                            break;
                    }
                    escaping = false;
                } else if (c == '\\') {
                    escaping = true;
                } else if (c == '"') {
                    return result.toString().trim();
                } else {
                    result.append(c);
                }
            }
            markerPos = responseBody.indexOf("\"text\"", markerPos + 6);
        }

        throw new IOException("Unable to parse Gemini response: " + responseBody);
    }

    public static class ChatMessage {
        private final String role;
        private final String text;

        private ChatMessage(String role, String text) {
            this.role = role;
            this.text = text;
        }

        public static ChatMessage user(String text) {
            return new ChatMessage("user", text);
        }

        public static ChatMessage model(String text) {
            return new ChatMessage("model", text);
        }

        public String getRole() {
            return role;
        }

        public String getText() {
            return text;
        }
    }

    public static List<ChatMessage> append(List<ChatMessage> current, ChatMessage addition) {
        List<ChatMessage> newHistory = new ArrayList<>(current);
        newHistory.add(addition);
        return newHistory;
    }
}
