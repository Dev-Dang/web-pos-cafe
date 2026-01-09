package com.laptrinhweb.zerostarcafe.web.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Web layer constants for HTTP headers and request attributes.
 * Organized by category with consistent naming patterns.
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WebConstants {

    // ============================================
    // HTTP HEADERS (Unpoly Protocol)
    // ============================================
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Header {

        public static final String UP_VERSION = "X-Up-Version";
        public static final String UP_TARGET = "X-Up-Target";
        public static final String UP_MODE = "X-Up-Mode";
        public static final String UP_LOCATION = "X-Up-Location";
    }

    // ============================================
    // UNPOLY ATTRIBUTES
    // ============================================
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UnpolyAttribute {

        public static final String UP_TARGET = "up-target";
        public static final String UP_MODE = "up-mode";
        public static final String UP_HREF = "up-href";
        public static final String UP_REVEAL = "up-reveal";
        public static final String UP_TRANSITION = "up-transition";
    }

    // ============================================
    // HTTP REQUEST ATTRIBUTES
    // ============================================
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Attribute {

        public static final String MESSAGES = "messages";
        public static final String RESPONSE_CONTEXT = "responseContext";
        public static final String FORM_DATA = "formData";
        public static final String FORM_ERRORS = "formErrors";
        public static final String NEED_CART_MERGE = "needsCartMerge";
        public static final String SELECTED_CATEGORY_ID = "selectedCategoryId";
        public static final String SELECTED_CATEGORY_SLUG = "selectedCategorySlug";
        public static final String SELECTED_CATEGORY = "selectedCategory";
        public static final String CURRENT_CATEGORY_ID = "currentCategoryId";
        public static final String PRODUCT_DETAIL = "productDetail";
        public static final String PRODUCT_MODAL = "productModal";
        public static final String SEARCH_QUERY = "searchQuery";
        public static final String SEARCH_RESULTS_COUNT = "searchResultsCount";
        public static final String SEARCH_PAGE = "searchPage";
        public static final String SEARCH_HAS_MORE = "searchHasMore";
        public static final String CATEGORY_PAGE = "categoryPage";
        public static final String HAS_MORE = "hasMore";
        public static final String CURRENT_PAGE = "currentPage";
        public static final String NEXT_PAGE = "nextPage";
        public static final String PAGINATION_URL = "paginationUrl";
        public static final String PRODUCT_CARDS = "productCards";
    }

    // ============================================
    // AUTH
    // ============================================
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Auth {

        public static final String LOGIN_MODAL = "loginModal";
    }

    // ============================================
    // CART
    // ============================================
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Cart {

        public static final String CART = "cart";
        public static final String CART_ITEM_ID = "cartItemId";
        public static final String MENU_ITEM_ID = "menuItemId";
        public static final String QUANTITY = "qty";
        public static final String NOTE = "note";
        public static final String OPTION_VALUE_IDS = "optionValueIds";
        public static final String ACTION = "action";
    }

    // ============================================
    // FLAGS
    // ============================================
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Flag {

        public static final String RE_OPEN_MODAL = "reOpenModal";
    }

    // ============================================
    // PARAMETERS
    // ============================================
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Param {

        public static final String CATEGORY_SLUG = "categorySlug";
        public static final String PRODUCT_ID = "productId";
        public static final String PRODUCT_SLUG = "productSlug";
        public static final String SEARCH_KEYWORD = "keyword";
        public static final String PAGE = "page";
    }

    // ============================================
    // PAGINATION
    // ============================================
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Default {

        public static final int PAGE_SIZE = 9;
    }

    // ============================================
    // LOCALE & INTERNATIONALIZATION
    // ============================================
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Locale {

        public static final String COOKIE_NAME = "lang";
        public static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 30; // 30 days
        public static final String SESSION_ATTRIBUTE = "locale";
        public static final String I18N_ATTRIBUTE = "i18n";
        public static final String SUPPORTED_VI = "vi-VN";
        public static final String SUPPORTED_EN = "en-US";
        public static final String DEFAULT_LOCALE = "vi-VN";
        public static final String DEFAULT_LANGUAGE = "vi";
    }
}
