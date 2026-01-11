package com.laptrinhweb.zerostarcafe.domain.category;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;

import java.sql.Connection;
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
public class CategoryService {

    public List<Category> loadActiveCategories() {
        try (Connection conn = DBConnection.getConnection()) {
            CategoryDAO categoryDAO = new CategoryDAOImpl(conn);
            return categoryDAO.findAllByIsActiveTrueOrderByOrderIndexAsc();
        } catch (SQLException e) {
            LoggerUtil.warn(Category.class,
                    "Failed to load active categories" + e.getMessage());
            return new ArrayList<>();
        }
    }

}