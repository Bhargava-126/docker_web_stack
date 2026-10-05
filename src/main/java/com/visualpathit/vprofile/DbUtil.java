package com.visualpathit.vprofile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Opens MySQL connections using environment variables and creates tables on first use. */
public final class DbUtil {
    private static volatile boolean ready = false;

    private DbUtil() {}

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isEmpty()) ? def : v;
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://" + env("DB_HOST", "devopsdb") + ":" + env("DB_PORT", "3306")
                + "/" + env("DB_NAME", "appdb") + "?useSSL=false&allowPublicKeyRetrieval=true";
        Connection c = DriverManager.getConnection(url, env("DB_USER", "appuser"), env("DB_PASSWORD", "apppass"));
        if (!ready) {
            try (Statement s = c.createStatement()) {
                s.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "username VARCHAR(50) NOT NULL UNIQUE,"
                        + "email VARCHAR(100) NOT NULL,"
                        + "password_hash VARCHAR(255) NOT NULL,"
                        + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                s.executeUpdate("CREATE TABLE IF NOT EXISTS sessions ("
                        + "token_hash CHAR(64) PRIMARY KEY,"
                        + "username VARCHAR(50) NOT NULL,"
                        + "expires_at TIMESTAMP NOT NULL)");
            }
            ready = true;
        }
        return c;
    }
}
