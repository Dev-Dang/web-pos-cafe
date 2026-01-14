package com.laptrinhweb.zerostarcafe.domain.store.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants related to Store operations, including
 * default store ID, request parameters, session attributes,
 * and cookie names for managing store context in the application.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StoreConstants {

    public static final long DEFAULT_STORE_ID = 1L;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Param {

        public static final String STORE_ID = "storeId";
        public static final String TABLE_ID = "tableId";
        public static final String LATITUDE = "lat";
        public static final String LONGITUDE = "lon";
        public static final String REDIRECT_URL = "redirect";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Attribute {

        public static final String CATEGORIES = "categories";
        public static final String STORE_LIST = "stores";
        public static final String CURRENT_STORE = "currentStore";
        public static final String CURRENT_STORE_CTX = "sessionStoreContext";
        public static final String USER_CART = "cart";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Cookie {

        public static final String LAST_STORE_ID = "store_id";
        public static final String LAST_TABLE_ID = "table_id";
        public static final int MAX_AGE_SECONDS = 30 * 24 * 60 * 60; // 30 days
    }
}