package com.laptrinhweb.zerostarcafe.domain.category;

import com.laptrinhweb.zerostarcafe.core.context.DBContext;
import com.laptrinhweb.zerostarcafe.core.context.LocaleContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link CategoryDAO} that interacts with
 * the {@code categories} table for reading category information.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
public class CategoryDAOImpl implements CategoryDAO {

    // ==========================================================
    // RETRIEVAL
    // ==========================================================

    @Override
    public List<Category> findAllByIsActiveTrueOrderByOrderIndexAsc() throws SQLException {
        // Get current language from LocaleContext
        String language = LocaleContext.getLanguage();
        String jsonPath = "$." + language;

        String sql = """
                SELECT id,
                       JSON_UNQUOTE(JSON_EXTRACT(name, ?)) as name,
                       slug, icon_url, order_index, is_active
                FROM categories
                WHERE is_active = TRUE
                ORDER BY order_index ASC
                """;

        List<Category> list = new ArrayList<>();

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jsonPath);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rowMapper(rs));
                }
            }
        }

        return list;
    }

    @Override
    public Category findBySlug(String slug) throws SQLException {
        // Get current language from LocaleContext
        String language = LocaleContext.getLanguage();
        String jsonPath = "$." + language;

        String sql = """
                SELECT id,
                       JSON_UNQUOTE(JSON_EXTRACT(name, ?)) as name,
                       slug, icon_url, order_index, is_active
                FROM categories
                WHERE slug = ? AND is_active = TRUE
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jsonPath);
            ps.setString(2, slug);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowMapper(rs);
                }
            }
        }

        return null;
    }

    // ==========================================================
    // MAPPING UTIL
    // ==========================================================

    private Category rowMapper(ResultSet rs) throws SQLException {
        Category c = new Category();

        c.setId(rs.getLong("id"));
        c.setName(rs.getString("name"));
        c.setSlug(rs.getString("slug"));
        c.setIconUrl(rs.getString("icon_url"));
        c.setOrderIndex(rs.getInt("order_index"));
        c.setActive(rs.getBoolean("is_active"));

        return c;
    }
}