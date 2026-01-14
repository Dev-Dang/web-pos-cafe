package com.laptrinhweb.zerostarcafe.domain.cart.dao;

import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;

import java.sql.SQLException;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Data access interface for cart operations.
 * Provides CRUD operations for carts, cart items, and cart item options.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
public interface CartDAO {

    /**
     * Finds a cart by user ID and store ID with full details (items + options).
     * Returns cart with nested items list, each item has nested options list.
     *
     * @param userId the user ID
     * @param storeId the store ID
     * @return optional cart with full details if found
     * @throws SQLException if database error occurs
     */
    Optional<Cart> findByUserIdAndStoreId(long userId, long storeId) throws SQLException;

    /**
     * Finds a cart item by ID with full option details.
     *
     * @param cartItemId the cart item ID
     * @return optional cart item with options if found
     * @throws SQLException if database error occurs
     */
    Optional<CartItem> findCartItemById(long cartItemId) throws SQLException;

    /**
     * Counts total items in a cart.
     *
     * @param cartId the cart ID
     * @return total number of items
     * @throws SQLException if database error occurs
     */
    int countCartItems(long cartId) throws SQLException;

    /**
     * Saves a new cart and returns full cart with details.
     *
     * @param cart the cart to save
     * @return full cart with ID populated
     * @throws SQLException if database error occurs
     */
    Cart save(Cart cart) throws SQLException;

    /**
     * Saves a new cart item.
     *
     * @param cartItem the cart item to save
     * @return generated cart item ID
     * @throws SQLException if database error occurs
     */
    long saveCartItem(CartItem cartItem) throws SQLException;

    /**
     * Saves a new cart item option.
     *
     * @param cartItemOption the cart item option to save
     * @throws SQLException if database error occurs
     */
    void saveCartItemOption(CartItemOption cartItemOption) throws SQLException;

    /**
     * Updates full cart item details (qty, snapshots, note, hash).
     *
     * @param cartItem the cart item to update
     * @throws SQLException if database error occurs
     */
    void updateCartItemDetails(CartItem cartItem) throws SQLException;

    /**
     * Updates the quantity of a cart item.
     *
     * @param cartItemId the cart item ID
     * @param quantity the new quantity
     * @throws SQLException if database error occurs
     */
    void updateCartItemQuantity(long cartItemId, int quantity) throws SQLException;

    /**
     * Deletes all options for a cart item.
     *
     * @param cartItemId the cart item ID
     * @throws SQLException if database error occurs
     */
    void deleteCartItemOptions(long cartItemId) throws SQLException;

    /**
     * Deletes a cart item and its options.
     *
     * @param cartItemId the cart item ID
     * @throws SQLException if database error occurs
     */
    void deleteCartItem(long cartItemId) throws SQLException;

    /**
     * Deletes all items from a cart.
     *
     * @param cartId the cart ID
     * @throws SQLException if database error occurs
     */
    void clearCart(long cartId) throws SQLException;

    /**
     * Deletes a cart and all its items.
     *
     * @param cartId the cart ID
     * @throws SQLException if database error occurs
     */
    void deleteCart(long cartId) throws SQLException;
}
