package com.laptrinhweb.zerostarcafe.domain.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 *
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ... code here
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */
public class CategoryDAOImpl implements CategoryDAO {
    private final Connection conn;

    public CategoryDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // ==========================================================
    // RETRIEVAL
    // ==========================================================

    @Override
    public List<Category> findAllByIsActiveTrueOrderByOrderIndexAsc() throws SQLException {
        String sql = """
                SELECT id, name, icon_url, order_index, is_active
                FROM categories
                WHERE is_active = TRUE
                ORDER BY order_index ASC
                """;

        List<Category> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rowMapper(rs));
            }
        }

        return list;
    }

    // ==========================================================
    // MAPPING UTIL
    // ==========================================================

    private Category rowMapper(ResultSet rs) throws SQLException {
        Category c = new Category();

        c.setId(rs.getLong("id"));
        c.setName(rs.getString("name"));
        c.setIconUrl(rs.getString("icon_url"));
        c.setOrderIndex(rs.getInt("order_index"));
        c.setActive(rs.getBoolean("is_active"));

        return c;
    }
}