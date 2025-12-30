package com.laptrinhweb.zerostarcafe.domain.product.service;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.product.dao.ProductDAO;
import com.laptrinhweb.zerostarcafe.domain.product.dao.ProductDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.product.model.CatalogItem;
import com.laptrinhweb.zerostarcafe.domain.product.model.OptionGroup;
import com.laptrinhweb.zerostarcafe.domain.product.model.OptionValue;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductDetail;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides methods to retrieve catalog items and product details
 * with proper error handling and logging.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ProductService productService = new ProductService();
 *
 * // Load all products for a store
 * List<CatalogItem> items = productService.getCatalogItemsByStoreId(1L);
 *
 * // Get product detail for modal
 * ProductDetail detail = productService.getProductDetailBySlugAndStoreId("ca-phe-sua", 1L);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/12/2025
 * @since 1.0.0
 */
public final class ProductService {

    /**
     * Retrieves all catalog items (products with pricing and availability) for a specific store.
     * This method is used for rendering the store's menu on the homepage.
     *
     * @param storeId the store ID
     * @return list of catalog items for the store, or empty list if error occurs
     */
    public List<CatalogItem> getCatalogItemsByStoreId(@NonNull Long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            return productDAO.findCatalogItemsByStoreId(storeId);
        } catch (AppException | SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get catalog items for storeId=" + storeId, e);
            return List.of();
        }
    }

    /**
     * Retrieves catalog items filtered by category for a specific store.
     *
     * @param storeId    the store ID
     * @param categoryId the category ID
     * @return list of catalog items for the store and category, or empty list if error occurs
     */
    public List<CatalogItem> getCatalogItemsByStoreIdAndCategoryId(@NonNull Long storeId, @NonNull Long categoryId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            return productDAO.findCatalogItemsByStoreIdAndCategoryId(storeId, categoryId);
        } catch (AppException | SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get catalog items for storeId=" + storeId + ", categoryId=" + categoryId, e);
            return List.of();
        }
    }

    /**
     * Retrieves complete product detail by slug including options and pricing for a specific store.
     * This method is used for product detail modals where users configure their order.
     *
     * @param productSlug the product slug
     * @param storeId     the store ID
     * @return the product detail, or null if not found or error occurs
     */
    public ProductDetail getProductDetailBySlugAndStoreId(@NonNull String productSlug, @NonNull Long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            Optional<ProductDetail> detailOpt = productDAO.findProductDetailBySlugAndStoreId(productSlug, storeId);
            return detailOpt.orElse(null);
        } catch (SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get product detail by slug: " + productSlug + " - " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Searches for catalog items by product name for a specific store.
     *
     * @param storeId    the store ID
     * @param searchTerm the search term to match against product names
     * @return list of catalog items matching the search term, or empty list if error occurs
     */
    public List<CatalogItem> searchCatalogItemsByNameAndStoreId(@NonNull Long storeId, @NonNull String searchTerm) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            return productDAO.searchCatalogItemsByNameAndStoreId(storeId, searchTerm);
        } catch (AppException | SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to search products for storeId=" + storeId + ", searchTerm=" + searchTerm, e);
            return List.of();
        }
    }

    /**
     * Retrieves a catalog item by product ID for a specific store.
     * Used for price validation when adding items to cart.
     *
     * @param productId the menu item ID
     * @param storeId   the store ID
     * @return the catalog item, or null if not found or error occurs
     */
    public CatalogItem getCatalogItemByIdAndStoreId(@NonNull Long productId, @NonNull Long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            return productDAO.findCatalogItemByIdAndStoreId(productId, storeId).orElse(null);
        } catch (SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get catalog item by id=" + productId + ", storeId=" + storeId, e);
            return null;
        }
    }

    /**
     * Retrieves complete product detail by product ID for a specific store.
     * Includes option groups and their values.
     *
     * @param productId the menu item ID
     * @param storeId   the store ID
     * @return the product detail, or null if not found or error occurs
     */
    public ProductDetail getProductDetailByIdAndStoreId(@NonNull Long productId, @NonNull Long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            
            CatalogItem item = productDAO.findCatalogItemByIdAndStoreId(productId, storeId).orElse(null);
            if (item == null) {
                return null;
            }
            
            List<OptionGroup> optionGroups = productDAO.findOptionGroupsByProductIdAndStoreId(productId, storeId);
            
            ProductDetail detail = new ProductDetail();
            detail.setItem(item);
            detail.setOptionGroups(optionGroups);
            
            return detail;
        } catch (SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get product detail by id=" + productId + ", storeId=" + storeId, e);
            return null;
        }
    }

    /**
     * Validates an option value and returns its details.
     * Used for cart item validation.
     *
     * @param optionValueId the option value ID
     * @return the option value, or null if not found
     */
    public OptionValue getOptionValueById(@NonNull Long optionValueId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            return productDAO.findOptionValueById(optionValueId).orElse(null);
        } catch (SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get option value by id=" + optionValueId, e);
            return null;
        }
    }

    /**
     * Gets the option group containing a specific option value.
     *
     * @param optionValueId the option value ID
     * @return the option group, or null if not found
     */
    public OptionGroup getOptionGroupByOptionValueId(@NonNull Long optionValueId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            return productDAO.findOptionGroupByOptionValueId(optionValueId).orElse(null);
        } catch (SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get option group by optionValueId=" + optionValueId, e);
            return null;
        }
    }
}

