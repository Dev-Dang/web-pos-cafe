package com.laptrinhweb.zerostarcafe.domain.cart.service;

import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartConstants;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * <h2>Description:</h2>
 * <p>
 * Cache-first cart service with write-behind persistence to database.
 * Server memory cache is the source of truth for logged-in users.
 * </p>
 *
 * <h2>Architecture:</h2>
 * <ul>
 *     <li>ConcurrentHashMap for fast cart-state lookup</li>
 *     <li>ScheduledExecutorService for debounce scheduling</li>
 *     <li>Separate I/O ExecutorService for DB persists</li>
 *     <li>Stable snapshots for persistence (deep copy)</li>
 *     <li>TTL-based eviction for inactive carts</li>
 * </ul>
 *
 * <h2>Persistence Policy:</h2>
 * <ul>
 *     <li>Idle persist: 30 seconds after last modification</li>
 *     <li>Safety flush: at least once every 2 minutes while dirty</li>
 *     <li>Mandatory checkpoints: checkout, logout, payment (immediate)</li>
 * </ul>
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public final class CartCacheService {

    // ==========================================================
    // SINGLETON
    // ==========================================================

    private static final CartCacheService INSTANCE = new CartCacheService();

    public static CartCacheService getInstance() {
        return INSTANCE;
    }

    // ==========================================================
    // FIELDS
    // ==========================================================

    /** In-memory cache: key = "userId:storeId", value = CachedCart */
    private final ConcurrentHashMap<String, CachedCart> cache;

    /** Scheduler for debounce and periodic checks */
    private final ScheduledExecutorService scheduler;

    /** I/O thread pool for database persist operations */
    private final ExecutorService ioExecutor;

    /** DB persistence service */
    private final CartService cartService;

    /** Counter for temporary IDs (items not yet saved to DB) */
    private long tempIdCounter = -1;

    /** Flag to prevent operations after shutdown */
    private boolean isShutdown = false;

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    private CartCacheService() {
        this.cache = new ConcurrentHashMap<>();
        this.cartService = new CartService();

        // Create scheduler with daemon threads
        this.scheduler = Executors.newScheduledThreadPool(
                CartConstants.Cache.SCHEDULER_POOL_SIZE,
                runnable -> {
                    Thread t = new Thread(runnable, "cart-scheduler");
                    t.setDaemon(true);
                    return t;
                }
        );

        // Create I/O executor with daemon threads
        this.ioExecutor = Executors.newFixedThreadPool(
                CartConstants.Cache.IO_POOL_SIZE,
                runnable -> {
                    Thread t = new Thread(runnable, "cart-io");
                    t.setDaemon(true);
                    return t;
                }
        );

        // Schedule periodic safety flush check
        scheduler.scheduleAtFixedRate(
                this::safetyFlushCheck,
                CartConstants.Cache.SAFETY_CHECK_INITIAL_DELAY_MS,
                CartConstants.Cache.SAFETY_CHECK_INTERVAL_MS,
                TimeUnit.MILLISECONDS
        );

        // Schedule periodic cache eviction check
        scheduler.scheduleAtFixedRate(
                this::evictionCheck,
                CartConstants.Cache.EVICTION_CHECK_INTERVAL_MS,
                CartConstants.Cache.EVICTION_CHECK_INTERVAL_MS,
                TimeUnit.MILLISECONDS
        );

        LoggerUtil.info(CartCacheService.class, "CartCacheService initialized");
    }

    // ==========================================================
    // PUBLIC API
    // ==========================================================

    /**
     * Gets a cart from cache, or loads from DB if not cached.
     *
     * @param userId  the user ID
     * @param storeId the store ID
     * @return a copy of the cart (never null)
     */
    public Cart getCart(long userId, long storeId) {
        String key = buildKey(userId, storeId);
        CachedCart cached = cache.get(key);

        if (cached != null) {
            cached.touch();
            return CartCloner.cloneCart(cached.getCart());
        }

        // Load from DB
        Cart dbCart = cartService.getOrCreateCart(userId, storeId);
        if (dbCart == null) {
            dbCart = createEmptyCart(userId, storeId);
        }

        // Cache it (not dirty since it's from DB)
        CachedCart newCached = new CachedCart(dbCart);
        cache.put(key, newCached);

        return CartCloner.cloneCart(dbCart);
    }

    /**
     * Adds an item to the cart. Updates cache and schedules DB persist.
     * The item is copied before insertion to prevent external modification.
     *
     * @param userId  the user ID
     * @param storeId the store ID
     * @param item    the item to add (will be copied)
     * @return a copy of the updated cart
     */
    public Cart addItem(long userId, long storeId, CartItem item) {
        String key = buildKey(userId, storeId);
        CachedCart cached = getOrLoadCached(key, userId, storeId);

        // Generate hash
        String itemHash = cartService.generateItemHash(
                item.getMenuItemId(),
                item.getOptions(),
                item.getNote()
        );

        // Normalize and copy the item (never store caller's object directly)
        CartItem normalizedItem = CartCloner.normalizeForCache(
                item,
                cached.getCart().getId(),
                itemHash
        );

        // Find existing item by hash
        CartItem existing = findItemByHash(cached.getCart(), itemHash);

        if (existing != null) {
            existing.setQty(existing.getQty() + normalizedItem.getQty());
        } else {
            normalizedItem.setId(generateTempId());
            cached.getCart().getItems().add(normalizedItem);
        }

        markDirtyAndSchedulePersist(key, cached);

        return CartCloner.cloneCart(cached.getCart());
    }

    /**
     * Updates the quantity of a cart item.
     *
     * @param userId  the user ID
     * @param storeId the store ID
     * @param itemId  the cart item ID
     * @param newQty  the new quantity (if <= 0, item is removed)
     * @return a copy of the updated cart, or null if item not found
     */
    public Cart updateItemQty(long userId, long storeId, long itemId, int newQty) {
        String key = buildKey(userId, storeId);
        CachedCart cached = cache.get(key);

        if (cached == null) {
            return null;
        }

        cached.touch();
        CartItem item = findItemById(cached.getCart(), itemId);
        if (item == null) {
            return null;
        }

        if (newQty <= 0) {
            cached.getCart().getItems().remove(item);
        } else {
            item.setQty(newQty);
        }

        markDirtyAndSchedulePersist(key, cached);

        return CartCloner.cloneCart(cached.getCart());
    }

    /**
     * Removes an item from the cart.
     */
    public Cart removeItem(long userId, long storeId, long itemId) {
        return updateItemQty(userId, storeId, itemId, 0);
    }

    /**
     * Clears all items from the cart.
     */
    public Cart clearCart(long userId, long storeId) {
        String key = buildKey(userId, storeId);
        CachedCart cached = cache.get(key);

        if (cached == null) {
            return getCart(userId, storeId);
        }

        cached.touch();
        cached.getCart().getItems().clear();
        markDirtyAndSchedulePersist(key, cached);

        return CartCloner.cloneCart(cached.getCart());
    }

    /**
     * Merges guest cart items into the server cart.
     * Items are copied before insertion. Prices should be validated by caller.
     *
     * @param userId     the user ID
     * @param storeId    the store ID
     * @param guestItems items from guest cart (will be copied)
     * @return a copy of the merged cart
     */
    public Cart mergeGuestCart(long userId, long storeId, List<CartItem> guestItems) {
        if (guestItems == null || guestItems.isEmpty()) {
            return getCart(userId, storeId);
        }

        String key = buildKey(userId, storeId);
        CachedCart cached = getOrLoadCached(key, userId, storeId);

        for (CartItem guestItem : guestItems) {
            String itemHash = cartService.generateItemHash(
                    guestItem.getMenuItemId(),
                    guestItem.getOptions(),
                    guestItem.getNote()
            );

            // Normalize and copy
            CartItem normalizedItem = CartCloner.normalizeForCache(
                    guestItem,
                    cached.getCart().getId(),
                    itemHash
            );

            CartItem existing = findItemByHash(cached.getCart(), itemHash);

            if (existing != null) {
                existing.setQty(existing.getQty() + normalizedItem.getQty());
            } else {
                normalizedItem.setId(generateTempId());
                cached.getCart().getItems().add(normalizedItem);
            }
        }

        markDirtyAndSchedulePersist(key, cached);

        return CartCloner.cloneCart(cached.getCart());
    }

    /**
     * Persists the cart to DB immediately (blocking).
     * Call on checkout, logout, or payment.
     *
     * @param userId  the user ID
     * @param storeId the store ID
     */
    public void persistImmediately(long userId, long storeId) {
        String key = buildKey(userId, storeId);
        CachedCart cached = cache.get(key);

        if (cached == null || !cached.isDirty()) {
            return;
        }

        cached.cancelScheduledPersist();
        persistToDbSync(key, cached);
    }

    /**
     * Evicts a cart from cache (call on logout).
     * Persists first if dirty.
     *
     * @param userId  the user ID
     * @param storeId the store ID
     */
    public void evictFromCache(long userId, long storeId) {
        String key = buildKey(userId, storeId);
        CachedCart cached = cache.get(key);

        if (cached != null) {
            cached.cancelScheduledPersist();
            if (cached.isDirty()) {
                persistToDbSync(key, cached);
            }
        }

        cache.remove(key);
        LoggerUtil.debug(CartCacheService.class, "Cart evicted: " + key);
    }

    /**
     * Flush all dirty carts to DB and shutdown executors.
     * Call on application shutdown.
     */
    public void shutdown() {
        if (isShutdown) {
            return;
        }
        isShutdown = true;

        LoggerUtil.info(CartCacheService.class, "Shutting down CartCacheService...");

        // Cancel all scheduled tasks
        scheduler.shutdown();

        // Persist all dirty carts synchronously
        for (String key : cache.keySet()) {
            CachedCart cached = cache.get(key);
            if (cached != null && cached.isDirty()) {
                cached.cancelScheduledPersist();
                persistToDbSync(key, cached);
            }
        }

        // Shutdown I/O executor
        ioExecutor.shutdown();

        try {
            if (!scheduler.awaitTermination(CartConstants.Cache.SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            if (!ioExecutor.awaitTermination(CartConstants.Cache.SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                ioExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            ioExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        cache.clear();
        LoggerUtil.info(CartCacheService.class, "CartCacheService shutdown complete");
    }

    // ==========================================================
    // PRIVATE: CACHE HELPERS
    // ==========================================================

    private String buildKey(long userId, long storeId) {
        return userId + ":" + storeId;
    }

    private Cart createEmptyCart(long userId, long storeId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setStoreId(storeId);
        cart.setItems(new ArrayList<>());
        return cart;
    }

    private CachedCart getOrLoadCached(String key, long userId, long storeId) {
        CachedCart cached = cache.get(key);
        if (cached == null) {
            getCart(userId, storeId);
            cached = cache.get(key);
        } else {
            cached.touch();
        }
        return cached;
    }

    private CartItem findItemByHash(Cart cart, String hash) {
        for (CartItem item : cart.getItems()) {
            if (hash.equals(item.getItemHash())) {
                return item;
            }
        }
        return null;
    }

    private CartItem findItemById(Cart cart, long itemId) {
        for (CartItem item : cart.getItems()) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        return null;
    }

    private synchronized long generateTempId() {
        return tempIdCounter--;
    }

    // ==========================================================
    // PRIVATE: PERSISTENCE SCHEDULING
    // ==========================================================

    private void markDirtyAndSchedulePersist(String key, CachedCart cached) {
        cached.markDirty();
        cached.cancelScheduledPersist();

        if (isShutdown) {
            return;
        }

        // Schedule idle persist
        ScheduledFuture<?> future = scheduler.schedule(
                () -> persistIfIdle(key),
                CartConstants.Cache.IDLE_PERSIST_DELAY_MS,
                TimeUnit.MILLISECONDS
        );
        cached.setScheduledPersistTask(future);
    }

    private void persistIfIdle(String key) {
        CachedCart cached = cache.get(key);
        if (cached == null || !cached.isDirty()) {
            return;
        }

        if (cached.isIdleForPersist(CartConstants.Cache.IDLE_PERSIST_DELAY_MS)) {
            persistToDbAsync(key, cached);
        }
    }

    private void safetyFlushCheck() {
        for (String key : cache.keySet()) {
            CachedCart cached = cache.get(key);
            if (cached != null && cached.isDirtyTooLong(CartConstants.Cache.MAX_DIRTY_DURATION_MS)) {
                LoggerUtil.info(CartCacheService.class, "Safety flush: " + key);
                persistToDbAsync(key, cached);
            }
        }
    }

    private void evictionCheck() {
        for (String key : cache.keySet()) {
            CachedCart cached = cache.get(key);
            if (cached == null) {
                continue;
            }

            if (cached.isInactiveForEviction(CartConstants.Cache.INACTIVE_EVICTION_THRESHOLD_MS)) {
                if (cached.isDirty()) {
                    persistToDbSync(key, cached);
                }
                cached.cancelScheduledPersist();
                cache.remove(key);
                LoggerUtil.debug(CartCacheService.class, "Evicted inactive cart: " + key);
            }
        }
    }

    // ==========================================================
    // PRIVATE: DB PERSISTENCE
    // ==========================================================

    /**
     * Persists cart to DB asynchronously using I/O thread pool.
     * Takes a snapshot to avoid persisting live mutable object.
     */
    private void persistToDbAsync(String key, CachedCart cached) {
        if (isShutdown) {
            return;
        }

        // Take a stable snapshot
        final Cart snapshot = CartCloner.cloneCart(cached.getCart());
        final int expectedVersion = cached.getVersion();

        ioExecutor.submit(() -> {
            try {
                boolean success = cartService.persistCart(snapshot);
                if (success) {
                    // Check if cart was modified during persist
                    CachedCart current = cache.get(key);
                    if (current != null && current.getVersion() == expectedVersion) {
                        current.markClean();
                        LoggerUtil.debug(CartCacheService.class,
                                "Cart persisted async: " + key + ", v=" + current.getVersion());
                    }
                } else {
                    LoggerUtil.warn(CartCacheService.class, "Failed to persist cart: " + key);
                }
            } catch (Exception e) {
                LoggerUtil.error(CartCacheService.class, "Error persisting cart: " + key, e);
            }
        });
    }

    /**
     * Persists cart to DB synchronously (blocking).
     * Used for immediate persist on checkout/logout/shutdown.
     */
    private void persistToDbSync(String key, CachedCart cached) {
        try {
            Cart snapshot = CartCloner.cloneCart(cached.getCart());
            boolean success = cartService.persistCart(snapshot);
            if (success) {
                cached.markClean();
                LoggerUtil.debug(CartCacheService.class,
                        "Cart persisted sync: " + key + ", v=" + cached.getVersion());
            } else {
                LoggerUtil.warn(CartCacheService.class, "Failed to persist cart: " + key);
            }
        } catch (Exception e) {
            LoggerUtil.error(CartCacheService.class, "Error persisting cart: " + key, e);
        }
    }
}
