package com.laptrinhweb.zerostarcafe.domain.cart.dao;

import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;

import java.sql.SQLException;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * DAO interface for cart-related database operations. Provides CRUD operations
 * for carts, cart items, and cart item options. Method names follow JPA naming
 * conventions for future migration compatibility.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * try (Connection conn = DBConnection.getConnection()) {
 *     CartDAO cartDAO = new CartDAOImpl(conn);
 *     Cart cart = cartDAO.findByUserIdAndStoreId(userId, storeId);
 * }
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 29/12/2025
 * @since 1.0.0
 */
public interface CartDAO {

    // ==========================================================
    // CART OPERATIONS
    // ==========================================================

    /**
     * Finds a cart by user ID and store ID.
     *
     * @param userId  the user ID
     * @param storeId the store ID
     * @return the cart if found, null otherwise
     */
    Cart findByUserIdAndStoreId(long userId, long storeId) throws SQLException;

    /**
     * Inserts a new cart.
     *
     * @param cart the cart to insert (storeId and userId must be set)
     * @return the inserted cart with generated ID
     */
    Cart save(Cart cart) throws SQLException;

    /**
     * Deletes a cart by ID (cascades to items and options).
     *
     * @param cartId the cart ID
     */
    void deleteById(long cartId) throws SQLException;

    // ==========================================================
    // CART ITEM OPERATIONS
    // ==========================================================

    /**
     * Finds all items for a cart, ordered by created_at.
     *
     * @param cartId the cart ID
     * @return list of cart items with their options loaded
     */
    List<CartItem> findItemsByCartId(long cartId) throws SQLException;

    /**
     * Finds a cart item by cart ID and item hash.
     *
     * @param cartId   the cart ID
     * @param itemHash the item hash
     * @return the cart item if found, null otherwise
     */
    CartItem findItemByCartIdAndItemHash(long cartId, String itemHash) throws SQLException;

    /**
     * Finds a cart item by ID.
     *
     * @param itemId the cart item ID
     * @return the cart item if found, null otherwise
     */
    CartItem findItemById(long itemId) throws SQLException;

    /**
     * Inserts a new cart item.
     *
     * @param item the cart item to insert
     * @return the inserted item with generated ID
     */
    CartItem saveItem(CartItem item) throws SQLException;

    /**
     * Updates the quantity of a cart item.
     *
     * @param itemId the cart item ID
     * @param qty    the new quantity
     */
    void updateItemQty(long itemId, int qty) throws SQLException;

    /**
     * Deletes a cart item by ID (cascades to options).
     *
     * @param itemId the cart item ID
     */
    void deleteItemById(long itemId) throws SQLException;

    /**
     * Deletes all items from a cart.
     *
     * @param cartId the cart ID
     */
    void deleteAllItemsByCartId(long cartId) throws SQLException;

    // ==========================================================
    // CART ITEM OPTIONS OPERATIONS
    // ==========================================================

    /**
     * Finds all options for a cart item.
     *
     * @param cartItemId the cart item ID
     * @return list of cart item options
     */
    List<CartItemOption> findOptionsByCartItemId(long cartItemId) throws SQLException;

    /**
     * Inserts a cart item option.
     *
     * @param option the option to insert
     * @return the inserted option with generated ID
     */
    CartItemOption saveOption(CartItemOption option) throws SQLException;
}
