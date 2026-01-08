package com.laptrinhweb.zerostarcafe.domain.product.service;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.domain.product.dao.ProductDAO;
import com.laptrinhweb.zerostarcafe.domain.product.dao.ProductDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.product.dto.ProductCardDTO;
import com.laptrinhweb.zerostarcafe.domain.product.dto.ProductDetailDTO;
import com.laptrinhweb.zerostarcafe.domain.product.model.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Service layer for product-related business logic. Handles product retrieval,
 * price resolution, localization, and DTO conversion.
 * Language is automatically determined from LocaleContext.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ProductService productService = ProductService.getInstance();
 *
 * // Get products by category (locale from LocaleContext)
 * List<ProductCardDTO> products = productService.getProductsByCategory(1L, storeId);
 *
 * // Get full product details (locale from LocaleContext)
 * ProductDetailDTO product = productService.getProductDetail(9L, storeId);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductService {

    private static final ProductService INSTANCE = new ProductService();
    private final ProductDAO productDAO = new ProductDAOImpl();

    public static ProductService getInstance() {
        return INSTANCE;
    }

    /**
     * Retrieves products by category as cards for UI display.
     *
     * @param categoryId the category ID
     * @param storeId    the store ID for availability and pricing
     * @return list of product cards with localized content
     */
    public List<ProductCardDTO> getProductsByCategoryId(@NonNull Long categoryId, @NonNull Long storeId) {
        try {
            List<Product> products = productDAO.findByCategoryId(categoryId, storeId);
            return ProductMapper.toProductCards(products);
        } catch (SQLException e) {
            throw new AppException("Failed to get products by category=" + categoryId
                    + " for store=" + storeId, e);
        }
    }

    /**
     * Retrieves products in a category as cards with pagination.
     *
     * @param categoryId the category ID
     * @param storeId    the store ID for availability and pricing
     * @param limit      maximum number of results to return
     * @param offset     number of results to skip
     * @return list of product cards with localized content
     */
    public List<ProductCardDTO> getProductsByCategoryId(@NonNull Long categoryId, @NonNull Long storeId, int limit, int offset) {
        try {
            List<Product> products = productDAO.findByCategoryId(categoryId, storeId, limit, offset);
            return ProductMapper.toProductCards(products);
        } catch (SQLException e) {
            throw new AppException("Failed to get products by category=" + categoryId
                    + " for store=" + storeId + " with pagination", e);
        }
    }

    public List<ProductCardDTO> getProductsByCategorySlug(@NonNull String categorySlug, @NonNull Long storeId) {
        try {
            List<Product> products = productDAO.findByCategorySlug(categorySlug, storeId);
            return ProductMapper.toProductCards(products);
        } catch (SQLException e) {
            throw new AppException("Failed to get products by category slug=" + categorySlug
                    + " for store=" + storeId, e);
        }
    }

    /**
     * Retrieves full product details including options.
     *
     * @param productId the product ID
     * @param storeId   the store ID for availability and pricing
     * @return product detail with localized content, or null if not found
     */
    public ProductDetailDTO getProductDetail(@NonNull Long productId, @NonNull Long storeId) {
        try {
            Product product = productDAO.findById(productId, storeId).orElse(null);
            if (product == null) {
                return null;
            }
            return ProductMapper.toProductDetail(product);
        } catch (SQLException e) {
            throw new AppException("Failed to get product detail id=" + productId
                    + " for store=" + storeId, e);
        }
    }


    /**
     * Retrieves product details by slug for SEO-friendly URLs.
     *
     * @param slug    the product slug
     * @param storeId the store ID for availability and pricing
     * @return product detail with localized content, or null if not found
     */
    public ProductDetailDTO getProductBySlug(@NonNull String slug, @NonNull Long storeId) {
        try {
            Product product = productDAO.findBySlug(slug, storeId).orElse(null);
            if (product == null) {
                return null;
            }
            return ProductMapper.toProductDetail(product);
        } catch (SQLException e) {
            throw new AppException("Failed to get product by slug=" + slug
                    + " for store=" + storeId, e);
        }
    }

    /**
     * Retrieves all available products in a store as cards.
     *
     * @param storeId the store ID
     * @return list of available product cards
     */
    public List<ProductCardDTO> getAvailableProducts(@NonNull Long storeId) {
        try {
            List<Product> products = productDAO.findAvailableInStore(storeId);
            return ProductMapper.toProductCards(products);
        } catch (SQLException e) {
            throw new AppException("Failed to get available products for store=" + storeId, e);
        }
    }

    /**
     * Retrieves a product card by ID (lightweight version).
     *
     * @param productId the product ID
     * @param storeId   the store ID for availability and pricing
     * @return product card with localized content, or null if not found
     */
    public ProductCardDTO getProductCard(@NonNull Long productId, @NonNull Long storeId) {
        try {
            Product product = productDAO.findById(productId, storeId).orElse(null);
            if (product == null) {
                return null;
            }
            return ProductMapper.toProductCard(product);
        } catch (SQLException e) {
            throw new AppException("Failed to get product card id=" + productId
                    + " for store=" + storeId, e);
        }
    }

    /**
     * Searches for products by name and description.
     *
     * @param query   the search query (product name or description)
     * @param storeId the store ID for availability and pricing
     * @return list of product cards matching the search query
     */
    public List<ProductCardDTO> searchProducts(@NonNull String query, @NonNull Long storeId) {
        try {
            List<Product> products = productDAO.searchProducts(query.trim(), storeId);
            return ProductMapper.toProductCards(products);
        } catch (SQLException e) {
            throw new AppException("Failed to search products with query='" + query
                    + "' for store=" + storeId, e);
        }
    }

    /**
     * Searches for products with pagination support.
     *
     * @param query   the search query (product name or description)
     * @param storeId the store ID for availability and pricing
     * @param limit   maximum number of results to return
     * @param offset  number of results to skip
     * @return list of product cards matching the search query
     */
    public List<ProductCardDTO> searchProducts(@NonNull String query, @NonNull Long storeId, int limit, int offset) {
        try {
            List<Product> products = productDAO.searchProducts(query.trim(), storeId, limit, offset);
            return ProductMapper.toProductCards(products);
        } catch (SQLException e) {
            throw new AppException("Failed to search products with pagination query='" + query
                    + "' for store=" + storeId, e);
        }
    }

    /**
     * Retrieves products by category slug with pagination.
     *
     * @param categorySlug the category slug
     * @param storeId      the store ID for availability and pricing
     * @param limit        maximum number of results to return
     * @param offset       number of results to skip
     * @return list of product cards with localized content
     */
    public List<ProductCardDTO> getProductsByCategorySlug(@NonNull String categorySlug, @NonNull Long storeId, int limit, int offset) {
        try {
            List<Product> products = productDAO.findByCategorySlug(categorySlug, storeId, limit, offset);
            return ProductMapper.toProductCards(products);
        } catch (SQLException e) {
            throw new AppException("Failed to get products by category slug=" + categorySlug
                    + " for store=" + storeId + " with pagination", e);
        }
    }

    /**
     * Counts total search results for a query.
     *
     * @param query   the search query (product name or description)
     * @param storeId the store ID for availability and pricing
     * @return total number of products matching the search query
     */
    public int countSearchResults(@NonNull String query, @NonNull Long storeId) {
        try {
            return productDAO.countSearchResults(query.trim(), storeId);
        } catch (SQLException e) {
            throw new AppException("Failed to count search results with query='" + query
                    + "' for store=" + storeId, e);
        }
    }
}