package com.laptrinhweb.zerostarcafe.domain.cart.service;

import com.laptrinhweb.zerostarcafe.core.database.DBAction;
import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.security.TokenUtils;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.cart.dao.CartDAO;
import com.laptrinhweb.zerostarcafe.domain.cart.dao.CartDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Service layer for shopping cart database operations. Provides business logic
 * for managing carts, adding/updating/removing items, and handling
 * item hash generation for deduplication.
 * </p>
 *
 * <p>
 * <b>Important:</b> This service handles DB persistence only. For the full cart
 * strategy with caching and write-behind, see CartCacheService (to be implemented).
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * CartService cartService = new CartService();
 * Cart cart = cartService.getOrCreateCart(userId, storeId);
 * cart = cartService.addItem(userId, storeId, item);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.2.0
 * @lastModified 29/12/2025
 * @since 1.0.0
 */
public final class CartService {

    /**
     * Gets or creates a cart for a user at a specific store.
     * No transaction needed - simple read or single insert.
     *
     * @param userId  the user ID
     * @param storeId the store ID
     * @return the cart (existing or newly created), null if failed
     */
    public Cart getOrCreateCart(long userId, long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            CartDAO cartDAO = new CartDAOImpl(conn);
            return findOrCreateCart(cartDAO, userId, storeId);
        } catch (SQLException e) {
            LoggerUtil.error(CartService.class,
                    "Failed to get/create cart for userId=" + userId + ", storeId=" + storeId, e);
            return null;
        }
    }

    /**
     * Gets a cart for a user at a specific store (without creating).
     * No transaction needed - simple read.
     *
     * @param userId  the user ID
     * @param storeId the store ID
     * @return the cart if found, null otherwise
     */
    public Cart getCart(long userId, long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            CartDAO cartDAO = new CartDAOImpl(conn);
            return cartDAO.findByUserIdAndStoreId(userId, storeId);
        } catch (SQLException e) {
            LoggerUtil.error(CartService.class,
                    "Failed to get cart for userId=" + userId + ", storeId=" + storeId, e);
            return null;
        }
    }

    /**
     * Adds an item to the cart. If an item with the same hash already exists,
     * the quantity is increased instead of creating a duplicate.
     *
     * @param userId  the user ID
     * @param storeId the store ID
     * @param item    the item to add (must have menuItemId, qty, snapshots, and options set)
     * @return the updated cart, or null if failed
     */
    public Cart addItem(long userId, long storeId, CartItem item) {
        try {
            return DBAction.run(conn -> {
                CartDAO cartDAO = new CartDAOImpl(conn);
                Cart cart = findOrCreateCart(cartDAO, userId, storeId);

                prepareItemForCart(item, cart.getId());
                upsertItemByHash(cartDAO, cart.getId(), item);

                return cartDAO.findByUserIdAndStoreId(userId, storeId);
            });
        } catch (AppException e) {
            LoggerUtil.error(CartService.class,
                    "Failed to add item to cart for userId=" + userId, e);
            return null;
        }
    }

    /**
     * Updates the quantity of a cart item.
     *
     * @param userId  the user ID (for validation)
     * @param storeId the store ID (for validation)
     * @param itemId  the cart item ID
     * @param newQty  the new quantity (if <= 0, item is removed)
     * @return the updated cart, or null if failed
     */
    public Cart updateItemQty(long userId, long storeId, long itemId, int newQty) {
        try {
            return DBAction.run(conn -> {
                CartDAO cartDAO = new CartDAOImpl(conn);

                // Verify item belongs to user's cart
                CartItem item = cartDAO.findItemById(itemId);
                if (item == null) {
                    return null;
                }

                Cart cart = cartDAO.findByUserIdAndStoreId(userId, storeId);
                if (cart == null || cart.getId() != item.getCartId()) {
                    return null;
                }

                if (newQty <= 0) {
                    cartDAO.deleteItemById(itemId);
                } else {
                    cartDAO.updateItemQty(itemId, newQty);
                }

                return cartDAO.findByUserIdAndStoreId(userId, storeId);
            });
        } catch (AppException e) {
            LoggerUtil.error(CartService.class,
                    "Failed to update item qty for userId=" + userId + ", itemId=" + itemId, e);
            return null;
        }
    }

    /**
     * Removes an item from the cart.
     *
     * @param userId  the user ID (for validation)
     * @param storeId the store ID (for validation)
     * @param itemId  the cart item ID
     * @return the updated cart, or null if failed
     */
    public Cart removeItem(long userId, long storeId, long itemId) {
        return updateItemQty(userId, storeId, itemId, 0);
    }

    /**
     * Clears all items from a user's cart at a store.
     * No transaction needed - single delete operation.
     *
     * @param userId  the user ID
     * @param storeId the store ID
     * @return the empty cart, or null if failed
     */
    public Cart clearCart(long userId, long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            CartDAO cartDAO = new CartDAOImpl(conn);

            Cart cart = cartDAO.findByUserIdAndStoreId(userId, storeId);
            if (cart == null) {
                return null;
            }

            cartDAO.deleteAllItemsByCartId(cart.getId());
            return cartDAO.findByUserIdAndStoreId(userId, storeId);
        } catch (SQLException e) {
            LoggerUtil.error(CartService.class,
                    "Failed to clear cart for userId=" + userId + ", storeId=" + storeId, e);
            return null;
        }
    }

    /**
     * Merges a guest cart (from client-side) into a user's server cart.
     * Items with matching hashes have their quantities summed.
     *
     * <p><b>Security note:</b> This method only uses menuItemId and options from
     * the guest cart. Prices MUST be re-validated from the database, never trusted
     * from client. The caller is responsible for re-fetching current prices.</p>
     *
     * @param userId     the user ID
     * @param storeId    the store ID
     * @param guestItems the items from the guest cart (prices will be ignored/re-validated)
     * @return the merged cart, or null if failed
     */
    public Cart mergeGuestCart(long userId, long storeId, List<CartItem> guestItems) {
        if (guestItems == null || guestItems.isEmpty()) {
            return getOrCreateCart(userId, storeId);
        }

        try {
            return DBAction.run(conn -> {
                CartDAO cartDAO = new CartDAOImpl(conn);
                Cart cart = findOrCreateCart(cartDAO, userId, storeId);

                for (CartItem guestItem : guestItems) {
                    prepareItemForCart(guestItem, cart.getId());
                    upsertItemByHash(cartDAO, cart.getId(), guestItem);
                }

                return cartDAO.findByUserIdAndStoreId(userId, storeId);
            });
        } catch (AppException e) {
            LoggerUtil.error(CartService.class,
                    "Failed to merge guest cart for userId=" + userId, e);
            return null;
        }
    }

    /**
     * Persists a cart snapshot to the database.
     * Used by the cache layer for write-behind persistence.
     *
     * @param cart the cart to persist (must have valid userId and storeId)
     * @return true if successful
     */
    public boolean persistCart(Cart cart) {
        if (cart == null) {
            return false;
        }

        try {
            DBAction.run(conn -> {
                CartDAO cartDAO = new CartDAOImpl(conn);

                // Find or create the DB cart record
                Cart dbCart = findOrCreateCart(cartDAO, cart.getUserId(), cart.getStoreId());

                // Clear existing items and re-insert all
                cartDAO.deleteAllItemsByCartId(dbCart.getId());

                for (CartItem item : cart.getItems()) {
                    item.setCartId(dbCart.getId());
                    CartItem saved = cartDAO.saveItem(item);

                    for (CartItemOption option : item.getOptions()) {
                        option.setCartItemId(saved.getId());
                        cartDAO.saveOption(option);
                    }
                }

                return true;
            });
            return true;
        } catch (AppException e) {
            LoggerUtil.error(CartService.class,
                    "Failed to persist cart for userId=" + cart.getUserId(), e);
            return false;
        }
    }

    // ==========================================================
    // PRIVATE HELPER METHODS
    // ==========================================================

    /**
     * Finds or creates a cart for a user at a store.
     */
    private Cart findOrCreateCart(CartDAO cartDAO, long userId, long storeId) throws SQLException {
        Cart cart = cartDAO.findByUserIdAndStoreId(userId, storeId);
        if (cart != null) {
            return cart;
        }

        cart = new Cart();
        cart.setUserId(userId);
        cart.setStoreId(storeId);
        cart.setItems(new ArrayList<>());
        return cartDAO.save(cart);
    }

    /**
     * Prepares an item for insertion: generates hash, sets cartId, calculates options price.
     */
    private void prepareItemForCart(CartItem item, long cartId) {
        item.setCartId(cartId);
        item.setItemHash(generateItemHash(item.getMenuItemId(), item.getOptions(), item.getNote()));
        item.setOptionsPriceSnapshot(item.calculateOptionsPriceFromList());
    }

    /**
     * Inserts or updates an item by hash. If item with same hash exists, qty is summed.
     */
    private void upsertItemByHash(CartDAO cartDAO, long cartId, CartItem item) throws SQLException {
        CartItem existing = cartDAO.findItemByCartIdAndItemHash(cartId, item.getItemHash());

        if (existing != null) {
            int newQty = existing.getQty() + item.getQty();
            cartDAO.updateItemQty(existing.getId(), newQty);
        } else {
            CartItem inserted = cartDAO.saveItem(item);

            for (CartItemOption option : item.getOptions()) {
                option.setCartItemId(inserted.getId());
                cartDAO.saveOption(option);
            }
        }
    }

    // ==========================================================
    // HASH GENERATION
    // ==========================================================

    /**
     * Generates a SHA-256 hash for item deduplication.
     * Hash is based on: menuItemId + sorted optionValueIds + note
     *
     * @param menuItemId the menu item ID
     * @param options    the selected options
     * @param note       the customer note (can be null)
     * @return the hex-encoded SHA-256 hash
     */
    public String generateItemHash(long menuItemId, List<CartItemOption> options, String note) {
        StringBuilder sb = new StringBuilder();
        sb.append(menuItemId);
        sb.append("|");

        // Sort options by optionValueId for consistent hash
        if (options != null && !options.isEmpty()) {
            List<Long> optionIds = new ArrayList<>();
            for (CartItemOption opt : options) {
                optionIds.add(opt.getOptionValueId());
            }
            Collections.sort(optionIds);

            for (Long id : optionIds) {
                sb.append(id).append(",");
            }
        }
        sb.append("|");

        // Include note (normalized)
        if (note != null && !note.isBlank()) {
            sb.append(note.trim().toLowerCase());
        }

        return TokenUtils.hashToken(sb.toString());
    }
}
