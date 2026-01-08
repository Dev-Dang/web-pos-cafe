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

export const ModalWebConstants = {
    Endpoint: {
        BASE_MODAL: "auth", // Base endpoint for modals
        LEGACY_MODAL: "modals" // Fallback for other modals
    },
    
    Selector: {
        TRIGGER: ".btn-open-modal",
        CONTAINER: "#modal-container"
    }
};

export const InfiniteScrollConstants = {
    Endpoint: {
        SEARCH: "search",
        CATEGORIES: "categories"
    },
    
    Selector: {
        PRODUCT_GRID: '.product-grid',
        LOAD_TRIGGER: '[data-load-trigger]',
        SEARCH_INPUT: '[data-search-input]',
        CATEGORY_ACTIVE: '.category-item.is-active'
    },
    
    UnpolyAttribute: {
        TARGET: 'up-target',
        MODE: 'up-mode', 
        HREF: 'up-href',
        REVEAL: 'up-reveal'
    },
    
    Config: {
        PAGE_SIZE: 9,
        SCROLL_THRESHOLD: 100
    },
    
    // Helper method to build pagination URLs
    buildPaginationUrl(type, query, page) {
        const contextPath = window.location.pathname.split('/')[1];
        const base = contextPath ? `/${contextPath}` : '';
        
        if (type === 'search') {
            return `${base}/${this.Endpoint.SEARCH}?q=${encodeURIComponent(query)}&page=${page}`;
        }
        return `${base}/${this.Endpoint.CATEGORIES}/${query}?page=${page}`;
    }
};