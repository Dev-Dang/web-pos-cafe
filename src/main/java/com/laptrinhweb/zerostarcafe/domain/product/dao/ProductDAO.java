package com.laptrinhweb.zerostarcafe.domain.product.dao;

import com.laptrinhweb.zerostarcafe.domain.product.model.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides database access operations for the {@link Product} entity,
 * which represents menu items with pricing, options, and localized content.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *     <li>Find products by category with store-specific availability</li>
 *     <li>Find product by ID with full details including options</li>
 *     <li>Find products by slug with localized content</li>
 *     <li>Resolve pricing with promotional schedules</li>
 *     <li>Load option groups and values with store availability</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * ProductDAO productDAO = new ProductDAOImpl(connection);
 *
 * // Get products by category with current locale (from LocaleContext)
 * List<Product> products = productDAO.findByCategoryId(1L, storeId);
 *
 * // Load full product details with options
 * Optional<Product> product = productDAO.findById(9L, storeId);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
public interface ProductDAO {

    /**
     * Finds all products in a category that are available in the specified store.
     * Includes pricing resolution and localized content based on current locale.
     *
     * @param categoryId the category ID to filter by
     * @param storeId    the store ID for availability and pricing
     * @return list of products with resolved pricing and availability
     * @throws SQLException if a database access error occurs
     */
    List<Product> findByCategoryId(long categoryId, long storeId) throws SQLException;

    /**
     * Finds products in a category with pagination support.
     *
     * @param categoryId the category ID to filter by
     * @param storeId    the store ID for availability and pricing
     * @param limit      maximum number of results to return
     * @param offset     number of results to skip
     * @return list of products with resolved pricing and availability
     * @throws SQLException if a database access error occurs
     */
    List<Product> findByCategoryId(long categoryId, long storeId, int limit, int offset) throws SQLException;

    /**
     * Finds a product by ID with full details including options and pricing.
     * Includes all option groups and values available in the specified store.
     *
     * @param productId the product ID
     * @param storeId   the store ID for availability and pricing
     * @return an {@link Optional} containing the product if found
     * @throws SQLException if a database access error occurs
     */
    Optional<Product> findById(long productId, long storeId) throws SQLException;

    /**
     * Finds a product by slug with full details.
     * Used for SEO-friendly URLs and product page routing.
     *
     * @param slug    the product slug
     * @param storeId the store ID for availability and pricing
     * @return an {@link Optional} containing the product if found
     * @throws SQLException if a database access error occurs
     */
    Optional<Product> findBySlug(String slug, long storeId) throws SQLException;

    /**
     * Finds all products available in the menu for the specified store.
     * Only returns products with inMenu = true and available status.
     *
     * @param storeId the store ID
     * @return list of available products
     * @throws SQLException if a database access error occurs
     */
    List<Product> findAvailableInStore(long storeId) throws SQLException;

    /**
     * Searches products by name and description in the specified store.
     * Performs full-text search on localized content.
     *
     * @param query   the search query (product name or description)
     * @param storeId the store ID for availability and pricing
     * @return list of products matching the search query
     * @throws SQLException if a database access error occurs
     */
    List<Product> searchProducts(String query, long storeId) throws SQLException;

    /**
     * Searches products with pagination support.
     *
     * @param query   the search query (product name or description)
     * @param storeId the store ID for availability and pricing
     * @param limit   maximum number of results to return
     * @param offset  number of results to skip
     * @return list of products matching the search query
     * @throws SQLException if a database access error occurs
     */
    List<Product> searchProducts(String query, long storeId, int limit, int offset) throws SQLException;

    /**
     * Finds products in a category by slug with pagination support.
     * Used for SEO-friendly URLs and direct slug-based product filtering.
     *
     * @param categorySlug the category slug to filter by
     * @param storeId      the store ID for availability and pricing
     * @param limit        maximum number of results to return
     * @param offset       number of results to skip
     * @return list of products with resolved pricing and availability
     * @throws SQLException if a database access error occurs
     */
    List<Product> findByCategorySlug(String categorySlug, long storeId, int limit, int offset) throws SQLException;

    /**
     * Finds all products in a category by slug that are available in the specified store.
     * This method provides direct slug-based access without requiring category ID lookup.
     * Includes pricing resolution and localized content based on current locale.
     *
     * @param categorySlug the category slug to filter by (e.g., "coffee", "pastries")
     * @param storeId      the store ID for availability and pricing
     * @return list of products with resolved pricing and availability, empty if category not found
     * @throws SQLException if a database access error occurs
     */
    List<Product> findByCategorySlug(String categorySlug, long storeId) throws SQLException;

    /**
     * Counts total search results for a query.
     *
     * @param query   the search query (product name or description)
     * @param storeId the store ID for availability and pricing
     * @return total number of products matching the search query
     * @throws SQLException if a database access error occurs
     */
    int countSearchResults(String query, long storeId) throws SQLException;
}