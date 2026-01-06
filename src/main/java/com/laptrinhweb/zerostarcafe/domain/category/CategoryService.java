package com.laptrinhweb.zerostarcafe.domain.category;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Service class for managing category-related business logic.
 * Provides methods to load active categories and retrieve categories by slug.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * CategoryService service = CategoryService.getInstance();
 * List<Category> categories = service.loadActiveCategories();
 * Category category = service.getCategoryBySlug("coffee");
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryService {

    private static final CategoryService INSTANCE = new CategoryService();
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    /**
     * Get the singleton instance of CategoryService.
     *
     * @return the CategoryService instance
     */
    public static CategoryService getInstance() {
        return INSTANCE;
    }

    /**
     * Loads all active categories ordered by display index.
     *
     * @return list of active categories
     * @throws AppException if a database access error occurs
     */
    public List<Category> loadActiveCategories() {
        try {
            return categoryDAO.findAllByIsActiveTrueOrderByOrderIndexAsc();
        } catch (SQLException e) {
            throw new AppException("Failed to load active categories", e);
        }
    }

    /**
     * Finds an active category by its slug.
     *
     * @param slug category slug
     * @return category or null if not found
     * @throws AppException if a database access error occurs
     */
    public Category getCategoryBySlug(@NonNull String slug) {
        try {
            return categoryDAO.findBySlug(slug);
        } catch (SQLException e) {
            throw new AppException("Failed to get category by slug: " + slug, e);
        }
    }

}
