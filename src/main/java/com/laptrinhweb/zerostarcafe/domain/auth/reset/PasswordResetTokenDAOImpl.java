package com.laptrinhweb.zerostarcafe.domain.auth.reset;

import com.laptrinhweb.zerostarcafe.core.context.DBContext;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * JDBC implementation for password reset tokens.
 */
public class PasswordResetTokenDAOImpl implements PasswordResetTokenDAO {

    @Override
    public PasswordResetToken save(PasswordResetToken token) throws SQLException {
        String sql = """
                INSERT INTO password_reset_tokens (
                    user_id, token_hash, expires_at, request_ip, user_agent
                ) VALUES (?, ?, ?, ?, ?)
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"})) {
            ps.setLong(1, token.getUserId());
            ps.setString(2, token.getTokenHash());
            ps.setTimestamp(3, Timestamp.valueOf(token.getExpiresAt()));
            ps.setString(4, token.getRequestIp());
            ps.setString(5, token.getUserAgent());

            int affected = ps.executeUpdate();
            if (affected != 1)
                throw new SQLException("Insert reset token failed, rows affected=" + affected);

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    token.setId(rs.getLong(1));
                    return token;
                }
            }
            throw new SQLException("Failed to retrieve generated ID");
        }
    }

    @Override
    public Optional<PasswordResetToken> findValidByHash(String tokenHash) throws SQLException {
        String sql = """
                SELECT * FROM password_reset_tokens
                WHERE token_hash = ?
                  AND used_at IS NULL
                  AND expires_at > ?
                LIMIT 1
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tokenHash);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(rowMapper(rs));
            }
        }
    }

    @Override
    public void markUsedByHash(String tokenHash) throws SQLException {
        String sql = """
                UPDATE password_reset_tokens
                SET used_at = ?
                WHERE token_hash = ?
                  AND used_at IS NULL
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, tokenHash);
            ps.executeUpdate();
        }
    }

    @Override
    public void markUsedByUserId(Long userId) throws SQLException {
        String sql = """
                UPDATE password_reset_tokens
                SET used_at = ?
                WHERE user_id = ?
                  AND used_at IS NULL
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    private PasswordResetToken rowMapper(ResultSet rs) throws SQLException {
        PasswordResetToken token = new PasswordResetToken();
        token.setId(rs.getLong("id"));
        token.setUserId(rs.getLong("user_id"));
        token.setTokenHash(rs.getString("token_hash"));

        Timestamp expires = rs.getTimestamp("expires_at");
        if (expires != null) token.setExpiresAt(expires.toLocalDateTime());

        Timestamp used = rs.getTimestamp("used_at");
        if (used != null) token.setUsedAt(used.toLocalDateTime());

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) token.setCreatedAt(created.toLocalDateTime());

        token.setRequestIp(rs.getString("request_ip"));
        token.setUserAgent(rs.getString("user_agent"));
        return token;
    }
}
