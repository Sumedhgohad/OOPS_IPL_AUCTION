package com.ipl.auction.db;

public class DatabaseConfig {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public DatabaseConfig() {
        String host = getEnvOrDefault("DB_HOST", "localhost");
        String port = getEnvOrDefault("DB_PORT", "3307");
        String database = getEnvOrDefault("DB_NAME", "ipl_auction");
        this.username = getEnvOrDefault("DB_USER", "root");
        this.password = getEnvOrDefault("DB_PASSWORD", "");
        this.jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    }

    private String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null || value.isEmpty() ? defaultValue : value;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}


