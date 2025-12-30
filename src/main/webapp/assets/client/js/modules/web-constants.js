/**
 * ------------------------------------------------------------
 * Module: Web Constants
 * ------------------------------------------------------------
 * @description
 * Centralized endpoints and cookie names used by client modules
 * for store detection and session context.
 *
 * @example
 * import { StoreWebConstants } from './web-constants.js';
 * fetch(StoreWebConstants.Endpoint.STORE_DETECT)
 *
 * @version 1.0.2
 * @since 1.0.0
 * @lastModified 30/12/2025
 * @module web-constants
 * @author Dang Van Trung
 */
export const StoreWebConstants = {

    Endpoint: {
        STORE_DETECT: "store-detect",
        GET_PRODUCTS: "api/products"
    },

    Cookie: {
        LAST_STORE_ID: "store_id",
        LAST_TABLE_ID: "table_id",
    }
};

export const ProductWebConstants = {
    Endpoint: {
        PRODUCT_MODAL: "api/products",
        PRODUCTS_BY_CATEGORY: "api/products/category",
        PRODUCT_SEARCH: "api/products/search"
    }
};

export const CartWebConstants = {
    Endpoint: {
        CART_GET: "cart",
        CART_ADD: "cart/add",
        CART_UPDATE: "cart/update",
        CART_REMOVE: "cart/remove",
        CART_CLEAR: "cart/clear",
        CART_MERGE: "cart/merge",
        CHECKOUT: "checkout",
        INVOICE: "invoice"
    },

    LocalStorage: {
        GUEST_CART_PREFIX: "cart_",
        USER_CART_BACKUP_PREFIX: "user_cart_"
    }
};