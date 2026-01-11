package com.laptrinhweb.zerostarcafe.domain.auth.record;

import com.laptrinhweb.zerostarcafe.domain.auth.model.TokenStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC-based implementation of {@link AuthRecordDAO}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.2
 * @lastModified 14/12/2025
 * @since 1.0.0
 */
public class AuthRecordDAOImpl implements AuthRecordDAO {

    private final Connection conn;

    public AuthRecordDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // ==========================================================
    // SAVE (Insert or Update)
    // ==========================================================

    @Override
    public AuthRecord save(AuthRecord record) throws SQLException {
        if (record.getId() == null) {
            // INSERT branch (new record)
            String sql = """
                        INSERT INTO auth_tokens (
                            user_id, auth_hash, device_id, status,
                            expired_at, last_rotated_at,
                            ip_last, user_agent
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            try (PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"})) {
                ps.setLong(1, record.getUserId());
                ps.setString(2, record.getAuthHash());
                ps.setString(3, record.getDeviceId());
                ps.setString(4, record.getStatus().name());
                ps.setTimestamp(5, Timestamp.valueOf(record.getExpiredAt()));
                ps.setTimestamp(6, Timestamp.valueOf(record.getLastRotatedAt()));
                ps.setString(7, record.getIpLast());
                ps.setString(8, record.getUserAgent());

                int affected = ps.executeUpdate();
                if (affected != 1)
                    throw new SQLException(
                            "Inserting auth record failed, rows affected=" + affected);

                // Retrieve generated ID
                Long generatedId = null;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next())
                        generatedId = rs.getLong(1);
                }

                if (generatedId != null && generatedId > 0) {
                    record.setId(generatedId);
                    return record;
                } else
                    throw new SQLException("Failed to retrieve generated ID");
            }
        } else {
            // UPDATE branch (existing record)
            String sql = """
                        UPDATE auth_tokens
                        SET auth_hash = ?, device_id = ?, status = ?,
                            expired_at = ?, last_rotated_at = ?,
                            ip_last = ?, user_agent = ?
                        WHERE id = ?
                    """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, record.getAuthHash());
                ps.setString(2, record.getDeviceId());
                ps.setString(3, record.getStatus().name());
                ps.setTimestamp(4, Timestamp.valueOf(record.getExpiredAt()));
                ps.setTimestamp(5, Timestamp.valueOf(record.getLastRotatedAt()));
                ps.setString(6, record.getIpLast());
                ps.setString(7, record.getUserAgent());
                ps.setLong(8, record.getId());

                int affected = ps.executeUpdate();
                if (affected != 1)
                    throw new SQLException(
                            "Update auth record failed, rows affected=" + affected
                    );
            }

            return record;
        }
    }

    // ==========================================================
    // RETRIEVAL
    // ==========================================================

    @Override
    public Optional<AuthRecord> findValidByAuthHash(String authHash) throws SQLException {
        String sql = """
                    SELECT * FROM auth_tokens
                    WHERE auth_hash = ?
                      AND status = 'ACTIVE'
                      AND expired_at > ?
                    LIMIT 1
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authHash);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(rowMapper(rs));
            }
        }
    }

    // ==========================================================
    // REVOKE ALL BY USER ID
    // ==========================================================

    @Override
    public void revokeAllByUserId(Long userId) throws SQLException {
        String sql = """
                    UPDATE auth_tokens
                    SET status = 'REVOKED',
                        revoked_at = ?,
                        revoked_reason = 'revoked_by_system'
                    WHERE user_id = ?
                      AND status = 'ACTIVE'
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    // =======================================================================
    // REVOKE SINGLE TOKEN
    // =======================================================================

    @Override
    public void revokeByAuthHash(String authHash) throws SQLException {
        String sql = """
                    UPDATE auth_tokens
                    SET status = 'REVOKED',
                        revoked_at = ?,
                        revoked_reason = 'revoked_single'
                    WHERE auth_hash = ?
                      AND status = 'ACTIVE'
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, authHash);
            ps.executeUpdate();
        }
    }

    // =======================================================================
    // ROW MAPPER
    // =======================================================================

    private AuthRecord rowMapper(ResultSet rs) throws SQLException {
        AuthRecord t = new AuthRecord();

        t.setId(rs.getLong("id"));
        t.setUserId(rs.getLong("user_id"));
        t.setAuthHash(rs.getString("auth_hash"));
        t.setDeviceId(rs.getString("device_id"));

        String statusStr = rs.getString("status");
        if (statusStr != null) t.setStatus(TokenStatus.valueOf(statusStr));

        Timestamp expired = rs.getTimestamp("expired_at");
        if (expired != null) t.setExpiredAt(expired.toLocalDateTime());

        Timestamp rotated = rs.getTimestamp("last_rotated_at");
        if (rotated != null) t.setLastRotatedAt(rotated.toLocalDateTime());

        t.setIpLast(rs.getString("ip_last"));
        t.setUserAgent(rs.getString("user_agent"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) t.setCreatedAt(created.toLocalDateTime());

        Timestamp revokedAt = rs.getTimestamp("revoked_at");
        if (revokedAt != null) t.setRevokedAt(revokedAt.toLocalDateTime());

        t.setRevokedReason(rs.getString("revoked_reason"));

        return t;
    }
}