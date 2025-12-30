package com.laptrinhweb.zerostarcafe.domain.cart.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Constants for cart feature including cache configuration,
 * request parameters, and session keys.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.1.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public final class CartConstants {

    private CartConstants() {
    }

    /**
     * Cache configuration for write-behind persistence.
     */
    public static final class Cache {
        /** Time to wait (ms) after last change before persisting. 30 seconds. */
        public static final long IDLE_PERSIST_DELAY_MS = 30_000;

        /** Maximum time (ms) a cart can stay dirty before forced persist. 2 minutes. */
        public static final long MAX_DIRTY_DURATION_MS = 120_000;

        /** Interval (ms) between safety flush checks. 30 seconds. */
        public static final long SAFETY_CHECK_INTERVAL_MS = 30_000;

        /** Initial delay (ms) before first safety flush check. 1 minute. */
        public static final long SAFETY_CHECK_INITIAL_DELAY_MS = 60_000;

        /** Interval (ms) between cache eviction checks. 5 minutes. */
        public static final long EVICTION_CHECK_INTERVAL_MS = 300_000;

        /** Time (ms) after which an inactive cart is evicted. 30 minutes. */
        public static final long INACTIVE_EVICTION_THRESHOLD_MS = 1_800_000;

        /** Number of threads in the scheduler pool. */
        public static final int SCHEDULER_POOL_SIZE = 2;

        /** Number of threads in the I/O (database persist) pool. */
        public static final int IO_POOL_SIZE = 4;

        /** Timeout (seconds) for executor shutdown. */
        public static final int SHUTDOWN_TIMEOUT_SECONDS = 30;
    }

    /**
     * Request parameter names.
     */
    public static final class Param {
        public static final String MENU_ITEM_ID = "menuItemId";
        public static final String ITEM_ID = "itemId";
        public static final String QTY = "qty";
        public static final String NOTE = "note";
        public static final String OPTIONS = "options";
    }

    /**
     * Request attribute names for JSP.
     */
    public static final class Request {
        public static final String CART = "cart";
        public static final String CART_ITEMS = "cartItems";
        public static final String CART_TOTAL = "cartTotal";
        public static final String CART_COUNT = "cartCount";
    }

    /**
     * Session attribute names.
     */
    public static final class Session {
        /** Flag to trigger cart merge after login */
        public static final String NEEDS_CART_MERGE = "needsCartMerge";
    }

    /**
     * LocalStorage key pattern for guest cart.
     * Usage: "cart_" + storeId for guest, "user_cart_" + storeId for logged-in backup
     */
    public static final String GUEST_CART_KEY_PREFIX = "cart_";
    public static final String USER_CART_BACKUP_KEY_PREFIX = "user_cart_";
}
