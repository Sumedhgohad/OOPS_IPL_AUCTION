package com.ipl.auction.ui;

import com.ipl.auction.chat.GeminiChatService;
import com.ipl.auction.chat.GeminiChatService.ChatMessage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple chat surface that proxies user prompts to the Gemini API.
 */
public class ChatPage extends JPanel {

    private final JTextArea conversationArea;
    private final JTextField inputField;
    private final JButton sendButton;
    private final JLabel statusLabel;
    private final List<ChatMessage> conversation;

    private GeminiChatService chatService;

    public ChatPage() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 245, 250));

        conversation = new ArrayList<>();

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 144, 255));
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel title = new JLabel("AI Chat Assistant");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        statusLabel = new JLabel("Connecting...");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);
        header.add(statusLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        conversationArea = new JTextArea();
        conversationArea.setEditable(false);
        conversationArea.setLineWrap(true);
        conversationArea.setWrapStyleWord(true);
        conversationArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(conversationArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.addActionListener(e -> sendMessage());
        inputPanel.add(inputField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.addActionListener(e -> sendMessage());
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        initializeService();
    }

    private void initializeService() {
        try {
            chatService = new GeminiChatService();
            statusLabel.setText("Ready");
            inputField.setEnabled(true);
            sendButton.setEnabled(true);
        } catch (IllegalStateException ex) {
            statusLabel.setText("API key missing");
            appendLine("System", "Set GEMINI_API_KEY environment variable and reopen the app.");
            inputField.setEnabled(false);
            sendButton.setEnabled(false);
        }
    }

    private void sendMessage() {
        if (chatService == null) {
            JOptionPane.showMessageDialog(this, "Gemini API key not configured.", "Configuration", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userText = inputField.getText().trim();
        if (userText.isEmpty()) {
            return;
        }

        inputField.setText("");
        appendLine("You", userText);
        conversation.add(ChatMessage.user(userText));
        toggleInput(false);

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return chatService.generateReply(conversation);
            }

            @Override
            protected void done() {
                try {
                    String reply = get();
                    conversation.add(ChatMessage.model(reply));
                    appendLine("Gemini", reply);
                } catch (Exception e) {
                    appendLine("System", "Error: " + e.getMessage());
                } finally {
                    toggleInput(true);
                }
            }
        };
        worker.execute();
    }

    private void toggleInput(boolean enabled) {
        sendButton.setEnabled(enabled);
        inputField.setEnabled(enabled);
        if (enabled) {
            inputField.requestFocusInWindow();
        }
    }

    private void appendLine(String speaker, String text) {
        conversationArea.append(speaker + ": " + text + System.lineSeparator() + System.lineSeparator());
        conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
    }

    public void focusInput() {
        if (inputField.isEnabled()) {
            inputField.requestFocusInWindow();
        }
    }
}
