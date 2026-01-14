package com.laptrinhweb.zerostarcafe.domain.user_role;

import com.laptrinhweb.zerostarcafe.core.context.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link UserStoreRole} that interacts with
 * the {@code role_codes} table for reading, inserting, and deleting roles.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 11/11/2025
 * @since 1.0.0
 */
public class UserStoreRoleDAOImpl implements UserStoreRoleDAO {

    // ==========================================================
    // SAVE (Insert or Update)
    // ==========================================================

    @Override
    public boolean save(UserStoreRole usr) throws SQLException {
        String sql = """
                    INSERT INTO user_store_roles (user_id, store_id, role_code)
                    VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE role_code = VALUES(role_code)
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, usr.getUserId());
            ps.setLong(2, usr.getStoreId());
            ps.setString(3, usr.getRoleCode());
            return ps.executeUpdate() > 0;
        }
    }

    // ==========================================================
    // RETRIEVAL
    // ==========================================================

    @Override
    public List<UserStoreRole> findByUserId(long userId) throws SQLException {
        String sql = "SELECT * FROM user_store_roles WHERE user_id=? ORDER BY store_id ASC";
        List<UserStoreRole> list = new ArrayList<>();

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(rowMapper(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<UserStoreRole> findByUserAndStore(long userId, long storeId) throws SQLException {
        String sql = "SELECT * FROM user_store_roles WHERE user_id=? AND store_id=? LIMIT 1";

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, storeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(rowMapper(rs));
            }
        }
    }

    // ==========================================================
    // DELETE
    // ==========================================================

    @Override
    public boolean delete(long userId, long storeId) throws SQLException {
        String sql = "DELETE FROM user_store_roles WHERE user_id=? AND store_id=?";

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, storeId);
            return ps.executeUpdate() > 0;
        }
    }

    // ==========================================================
    // MAPPING UTIL
    // ==========================================================

    private UserStoreRole rowMapper(ResultSet rs) throws SQLException {
        UserStoreRole r = new UserStoreRole();
        r.setId(rs.getLong("id"));
        r.setUserId(rs.getLong("user_id"));
        r.setStoreId(rs.getLong("store_id"));
        r.setRoleCode(rs.getString("role_code"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) r.setCreatedAt(ts.toLocalDateTime());
        return r;
    }
}
