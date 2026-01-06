package com.laptrinhweb.zerostarcafe.domain.category;

import java.sql.SQLException;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides database access operations for the {@link Category} entity,
 * which represents a product category in the cafe menu.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *     <li>Find all active categories ordered by display index</li>
 *     <li>Find an active category by its slug</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * CategoryDAO dao = new CategoryDAOImpl();
 * List<Category> categories = dao.findAllByIsActiveTrueOrderByOrderIndexAsc();
 * Category category = dao.findBySlug("coffee");
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
public interface CategoryDAO {

    /**
     * Loads all active categories ordered by display index.
     *
     * @return list of active categories
     * @throws SQLException if a database access error occurs
     */
    List<Category> findAllByIsActiveTrueOrderByOrderIndexAsc() throws SQLException;

    /**
     * Finds an active category by its slug.
     *
     * @param slug category slug
     * @return category or null if not found
     * @throws SQLException if a database access error occurs
     */
    Category findBySlug(String slug) throws SQLException;

}
