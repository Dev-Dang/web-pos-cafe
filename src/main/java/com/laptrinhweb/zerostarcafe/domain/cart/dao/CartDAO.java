package com.laptrinhweb.zerostarcafe.domain.cart.dao;

import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides database access operations for the {@link Cart} entity and related
 * cart items and options. This DAO handles cart management operations.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *     <li>Find cart by user and store</li>
 *     <li>Create and manage cart instances</li>
 *     <li>Add, update, remove cart items</li>
 *     <li>Manage cart item options</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * CartDAO cartDAO = new CartDAOImpl();
 *
 * // Get user's cart for specific store
 * Optional<Cart> cart = cartDAO.findByUserIdAndStoreId(userId, storeId);
 *
 * // Create new cart
 * Cart newCart = cartDAO.save(cart);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
public interface CartDAO {

    // ===== Cart Operations =====

    /**
     * Finds a cart by user ID and store ID.
     * @param userId the user ID
     * @param storeId the store ID  
     * @return an {@link Optional} containing the cart if found
     * @throws SQLException if a database access error occurs
     */
    Optional<Cart> findByUserIdAndStoreId(Long userId, Long storeId) throws SQLException;

    /**
     * Finds a cart by ID with all items and options loaded.
     * @param id the cart ID
     * @return an {@link Optional} containing the cart if found
     * @throws SQLException if a database access error occurs
     */
    Optional<Cart> findById(Long id) throws SQLException;

    /**
     * Saves a cart (insert if new, update if existing).
     * @param cart the cart to save
     * @return the saved cart with generated ID if new
     * @throws SQLException if a database access error occurs
     */
    Cart save(Cart cart) throws SQLException;

    /**
     * Deletes a cart by ID.
     * @param id the cart ID to delete
     * @throws SQLException if a database access error occurs
     */
    void deleteById(Long id) throws SQLException;

    // ===== Cart Item Operations =====

    /**
     * Finds a cart item by ID with options loaded.
     * @param id the cart item ID
     * @return an {@link Optional} containing the cart item if found
     * @throws SQLException if a database access error occurs
     */
    Optional<CartItem> findItemById(Long id) throws SQLException;

    /**
     * Finds a cart item by cart ID and item hash.
     * @param cartId the cart ID
     * @param itemHash the item hash for deduplication
     * @return an {@link Optional} containing the cart item if found
     * @throws SQLException if a database access error occurs
     */
    Optional<CartItem> findItemByCartIdAndItemHash(Long cartId, String itemHash) throws SQLException;

    /**
     * Finds all items for a cart.
     * @param cartId the cart ID
     * @return list of cart items with options
     * @throws SQLException if a database access error occurs
     */
    List<CartItem> findItemsByCartId(Long cartId) throws SQLException;

    /**
     * Saves a cart item (insert if new, update if existing).
     * @param cartItem the cart item to save
     * @return the saved cart item with generated ID if new
     * @throws SQLException if a database access error occurs
     */
    CartItem saveItem(CartItem cartItem) throws SQLException;

    /**
     * Deletes a cart item by ID.
     * @param id the cart item ID to delete
     * @throws SQLException if a database access error occurs
     */
    void deleteItemById(Long id) throws SQLException;

    // ===== Cart Item Option Operations =====

    /**
     * Finds all options for a cart item.
     * @param cartItemId the cart item ID
     * @return list of cart item options
     * @throws SQLException if a database access error occurs
     */
    List<CartItemOption> findOptionsByCartItemId(Long cartItemId) throws SQLException;

    /**
     * Saves a cart item option.
     * @param option the cart item option to save
     * @return the saved cart item option with generated ID
     * @throws SQLException if a database access error occurs
     */
    CartItemOption saveOption(CartItemOption option) throws SQLException;

    /**
     * Deletes all options for a cart item.
     * @param cartItemId the cart item ID
     * @throws SQLException if a database access error occurs
     */
    void deleteOptionsByCartItemId(Long cartItemId) throws SQLException;
}