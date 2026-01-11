package com.laptrinhweb.zerostarcafe.domain.store.model;

/**
 * <h2>Description:</h2>
 * <p>
 *
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ... code here
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */
public final class StoreConstants {

    private StoreConstants() {
    }

    public static final long DEFAULT_STORE_ID = 1L;

    public static final class Param {
        public static final String STORE_ID = "storeId";
        public static final String TABLE_ID = "tableId";
        public static final String LATITUDE = "lat";
        public static final String LONGITUDE = "lon";
        public static final String REDIRECT_URL = "redirect";
    }

    public static final class Request {
        public static final String CATEGORIES = "categories";
        public static final String STORE_LIST = "stores";
        public static final String CURRENT_STORE = "currentStore";
    }

    public static final class Session {
        public static final String CURRENT_STORE_CTX = "SESSION_STORE_CONTEXT";
    }

    public static final class Cookie {
        public static final String LAST_STORE_ID = "store_id";
        public static final String LAST_TABLE_ID = "table_id";
        public static final int MAX_AGE_SECONDS = 30 * 24 * 60 * 60; // 30 days
    }
}