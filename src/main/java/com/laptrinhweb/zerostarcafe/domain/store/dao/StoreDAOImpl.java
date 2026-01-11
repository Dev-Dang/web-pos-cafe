package com.laptrinhweb.zerostarcafe.domain.store.dao;

import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link StoreDAO} that interacts with
 * the {@code stores} table for reading store information.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */
public class StoreDAOImpl implements StoreDAO {

    private final Connection conn;

    public StoreDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // ==========================================================
    // RETRIEVAL
    // ==========================================================

    @Override
    public Optional<Store> findById(long id) throws SQLException {
        String sql = """
                SELECT id, name, address, latitude, longitude, status,
                       created_at, updated_at
                FROM stores
                WHERE id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(rowMapper(rs));
            }
        }
    }

    @Override
    public List<Store> findAllByStatus(StoreStatus status) throws SQLException {
        String sql = """
                SELECT id, name, address, latitude, longitude, status,
                       created_at, updated_at
                FROM stores
                WHERE status = ?
                """;

        List<Store> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name().toLowerCase());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rowMapper(rs));
                }
            }
        }

        return list;
    }

    // ==========================================================
    // MAPPING UTIL
    // ==========================================================

    private Store rowMapper(ResultSet rs) throws SQLException {
        Store s = new Store();

        s.setId(rs.getLong("id"));
        s.setName(rs.getString("name"));
        s.setAddress(rs.getString("address"));
        s.setLatitude(rs.getDouble("latitude"));
        s.setLongitude(rs.getDouble("longitude"));
        s.setStatus(StoreStatus.valueOf(rs.getString("status").toUpperCase()));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            s.setCreatedAt(created.toLocalDateTime());
        }

        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) {
            s.setUpdatedAt(updated.toLocalDateTime());
        }

        return s;
    }
}