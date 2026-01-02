package com.laptrinhweb.zerostarcafe.domain.product.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Constants used throughout the product domain for parameter names,
 * request attributes, and session keys related to products and menu items.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 26/12/2025
 * @since 1.0.0
 */
public final class ProductConstants {

    private ProductConstants() {
    }

    public static final class Param {
        public static final String PRODUCT_ID = "productId";
        public static final String CATEGORY_ID = "categoryId";
    }

    public static final class Request {
        public static final String PRODUCTS = "products";
        public static final String PRODUCT = "product";
        public static final String PRODUCT_DETAIL = "productDetail";
        public static final String CATALOG_ITEMS = "catalogItems";
    }

    public static final class Session {
        public static final String CART = "SESSION_CART";
    }
}

