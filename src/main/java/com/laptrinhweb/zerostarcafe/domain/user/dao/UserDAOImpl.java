package com.laptrinhweb.zerostarcafe.domain.user.dao;

import com.laptrinhweb.zerostarcafe.domain.user.model.User;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link UserDAO} that interacts with
 * the {@code users} table for reading, inserting, and deleting roles.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 16/12/2025
 * @since 1.0.0
 */
public class UserDAOImpl implements UserDAO {
    private final Connection conn;

    public UserDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // ==========================================================
    // SAVE (Insert or Update)
    // ==========================================================

    @Override
    public User save(User user) throws SQLException {
        if (user.getId() == null) {
            // INSERT branch (new record)
            String sql = """
                    INSERT INTO users (email, username, password_hash, status, is_super_admin)
                    VALUES (?, ?, ?, ?, ?)
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"})) {
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getPasswordHash());
                ps.setString(4, user.getStatus().name());
                ps.setBoolean(5, user.isSuperAdmin());

                int affected = ps.executeUpdate();
                if (affected != 1)
                    throw new SQLException(
                            "Inserting new user failed, rows affected=" + affected);

                // Retrieve generated ID
                Long generatedId = null;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next())
                        generatedId = rs.getLong(1);
                }

                if (generatedId != null && generatedId > 0) {
                    user.setId(generatedId);
                    return user;
                } else
                    throw new SQLException("Failed to retrieve generated ID");
            }
        } else {
            // UPDATE branch (existing record)
            String sql = """
                    UPDATE users
                    SET email=?, username=?, password_hash=?, status=?, is_super_admin=?
                    WHERE id=?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getPasswordHash());
                ps.setString(4, user.getStatus().name());
                ps.setBoolean(5, user.isSuperAdmin());
                ps.setLong(6, user.getId());

                int affected = ps.executeUpdate();
                if (affected != 1)
                    throw new SQLException(
                            "Update user record failed, rows affected=" + affected
                    );

                return user;
            }
        }
    }

    // ==========================================================
    // EXISTENCE CHECKS
    // ==========================================================

    @Override
    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username=? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email=? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ==========================================================
    // RETRIEVAL
    // ==========================================================

    @Override
    public Optional<User> findById(long id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(rowMapper(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(rowMapper(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        List<User> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rowMapper(rs));
        }
        return list;
    }

    // ==========================================================
    // DELETE
    // ==========================================================

    @Override
    public boolean deleteById(long id) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ==========================================================
    // MAPPING UTIL
    // ==========================================================

    private User rowMapper(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setOauthProvider(rs.getString("oauth_provider"));
        u.setOauthId(rs.getString("oauth_id"));
        u.setStatus(UserStatus.valueOf(rs.getString("status").toUpperCase()));
        u.setSuperAdmin(rs.getBoolean("is_super_admin"));

        Timestamp cAt = rs.getTimestamp("created_at");
        Timestamp uAt = rs.getTimestamp("updated_at");
        if (cAt != null) u.setCreatedAt(cAt.toLocalDateTime());
        if (uAt != null) u.setUpdatedAt(uAt.toLocalDateTime());
        return u;
    }
}