package com.visualpathit.vprofile;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HexFormat;

/**
 * Database-backed sessions. The Swarm service runs 3 replicas behind a round-robin
 * load balancer, so in-memory Tomcat sessions would be lost whenever a request lands
 * on a different replica. Storing sessions in MySQL makes every replica see them.
 */
public final class SessionStore {
    public static final String COOKIE = "APPSESSION";
    public static final int TTL_SECONDS = 900;
    private static final SecureRandom RNG = new SecureRandom();

    private SessionStore() {}

    public static String create(String username) throws SQLException {
        byte[] raw = new byte[32];
        RNG.nextBytes(raw);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        String sql = "INSERT INTO sessions (token_hash, username, expires_at) "
                + "VALUES (?, ?, DATE_ADD(NOW(), INTERVAL " + TTL_SECONDS + " SECOND))";
        try (Connection c = DbUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sha256(token));
            ps.setString(2, username);
            ps.executeUpdate();
        }
        return token;
    }

    /** @return the logged-in username, or null if there is no valid session */
    public static String userFor(HttpServletRequest req) throws SQLException {
        String token = tokenFrom(req);
        if (token == null) {
            return null;
        }
        String sql = "SELECT username FROM sessions WHERE token_hash = ? AND expires_at > NOW()";
        try (Connection c = DbUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sha256(token));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    public static void destroy(HttpServletRequest req) throws SQLException {
        String token = tokenFrom(req);
        if (token == null) {
            return;
        }
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM sessions WHERE token_hash = ?")) {
            ps.setString(1, sha256(token));
            ps.executeUpdate();
        }
    }

    public static void setCookie(HttpServletResponse res, String token, int maxAge) {
        res.addHeader("Set-Cookie", COOKIE + "=" + token + "; Path=/; Max-Age=" + maxAge + "; HttpOnly; SameSite=Lax");
    }

    private static String tokenFrom(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (COOKIE.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    private static String sha256(String s) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(d);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
