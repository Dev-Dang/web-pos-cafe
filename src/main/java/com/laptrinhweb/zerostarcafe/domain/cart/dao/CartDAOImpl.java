package com.laptrinhweb.zerostarcafe.domain.cart.dao;

import com.laptrinhweb.zerostarcafe.core.context.DBContext;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link CartDAO} that interacts with
 * the carts, cart_items, and cart_item_options tables.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
public class CartDAOImpl implements CartDAO {

    // ==========================================================
    // CART OPERATIONS
    // ==========================================================

    @Override
    public Optional<Cart> findByUserIdAndStoreId(Long userId, Long storeId) throws SQLException {
        String sql = """
                SELECT id, store_id, user_id, created_at, updated_at
                FROM carts 
                WHERE user_id = ? AND store_id = ?
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapToCart(rs));
            }
        }
    }

    @Override
    public Optional<Cart> findById(Long id) throws SQLException {
        String sql = """
                SELECT id, store_id, user_id, created_at, updated_at
                FROM carts 
                WHERE id = ?
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                Cart cart = mapToCart(rs);
                
                // Load cart items with options
                List<CartItem> items = findItemsByCartId(id);
                cart.setItems(items);
                
                return Optional.of(cart);
            }
        }
    }

    @Override
    public Cart save(Cart cart) throws SQLException {
        if (cart.getId() == null) {
            return insertCart(cart);
        } else {
            updateCart(cart);
            return cart;
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM carts WHERE id = ?";

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    // ==========================================================
    // CART ITEM OPERATIONS
    // ==========================================================

    @Override
    public Optional<CartItem> findItemById(Long id) throws SQLException {
        String sql = """
                SELECT id, cart_id, menu_item_id, qty, unit_price_snapshot, 
                       options_price_snapshot, note, item_hash, item_name_snapshot,
                       created_at, updated_at
                FROM cart_items 
                WHERE id = ?
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                CartItem item = mapToCartItem(rs);
                
                // Load item options
                List<CartItemOption> options = findOptionsByCartItemId(id);
                item.setOptions(options);
                
                return Optional.of(item);
            }
        }
    }

    @Override
    public Optional<CartItem> findItemByCartIdAndItemHash(Long cartId, String itemHash) throws SQLException {
        String sql = """
                SELECT id, cart_id, menu_item_id, qty, unit_price_snapshot, 
                       options_price_snapshot, note, item_hash, item_name_snapshot,
                       created_at, updated_at
                FROM cart_items 
                WHERE cart_id = ? AND item_hash = ?
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartId);
            ps.setString(2, itemHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                CartItem item = mapToCartItem(rs);
                
                // Load item options
                List<CartItemOption> options = findOptionsByCartItemId(item.getId());
                item.setOptions(options);
                
                return Optional.of(item);
            }
        }
    }

    @Override
    public List<CartItem> findItemsByCartId(Long cartId) throws SQLException {
        String sql = """
                SELECT id, cart_id, menu_item_id, qty, unit_price_snapshot, 
                       options_price_snapshot, note, item_hash, item_name_snapshot,
                       created_at, updated_at
                FROM cart_items 
                WHERE cart_id = ?
                ORDER BY created_at ASC
                """;

        List<CartItem> items = new ArrayList<>();
        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = mapToCartItem(rs);
                    
                    // Load options for each item
                    List<CartItemOption> options = findOptionsByCartItemId(item.getId());
                    item.setOptions(options);
                    
                    items.add(item);
                }
            }
        }
        return items;
    }

    @Override
    public CartItem saveItem(CartItem cartItem) throws SQLException {
        if (cartItem.getId() == null) {
            return insertCartItem(cartItem);
        } else {
            updateCartItem(cartItem);
            return cartItem;
        }
    }

    @Override
    public void deleteItemById(Long id) throws SQLException {
        // First delete options
        deleteOptionsByCartItemId(id);
        
        // Then delete item
        String sql = "DELETE FROM cart_items WHERE id = ?";
        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    // ==========================================================
    // CART ITEM OPTION OPERATIONS
    // ==========================================================

    @Override
    public List<CartItemOption> findOptionsByCartItemId(Long cartItemId) throws SQLException {
        String sql = """
                SELECT id, cart_item_id, option_value_id, option_group_name_snapshot,
                       option_value_name_snapshot, price_delta_snapshot
                FROM cart_item_options 
                WHERE cart_item_id = ?
                ORDER BY id ASC
                """;

        List<CartItemOption> options = new ArrayList<>();
        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItemOption option = mapToCartItemOption(rs);
                    options.add(option);
                }
            }
        }
        return options;
    }

    @Override
    public CartItemOption saveOption(CartItemOption option) throws SQLException {
        String sql = """
                INSERT INTO cart_item_options (cart_item_id, option_value_id, option_group_name_snapshot,
                                             option_value_name_snapshot, price_delta_snapshot) 
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, option.getCartItemId());
            ps.setLong(2, option.getOptionValueId());
            ps.setString(3, option.getOptionGroupNameSnapshot());
            ps.setString(4, option.getOptionValueNameSnapshot());
            ps.setInt(5, option.getPriceDeltaSnapshot());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        option.setId(rs.getLong(1));
                        return option;
                    }
                }
            }
        }
        throw new SQLException("Creating cart item option failed, no ID obtained.");
    }

    @Override
    public void deleteOptionsByCartItemId(Long cartItemId) throws SQLException {
        String sql = "DELETE FROM cart_item_options WHERE cart_item_id = ?";

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);
            ps.executeUpdate();
        }
    }

    // ==========================================================
    // PRIVATE HELPER METHODS
    // ==========================================================

    /**
     * Insert new cart and return with generated ID.
     */
    private Cart insertCart(Cart cart) throws SQLException {
        String sql = """
                INSERT INTO carts (store_id, user_id, created_at, updated_at) 
                VALUES (?, ?, NOW(), NOW())
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, cart.getStoreId());
            ps.setLong(2, cart.getUserId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cart.setId(rs.getLong(1));
                        return cart;
                    }
                }
            }
        }
        throw new SQLException("Creating cart failed, no ID obtained.");
    }

    /**
     * Update existing cart timestamp.
     */
    private void updateCart(Cart cart) throws SQLException {
        String sql = "UPDATE carts SET updated_at = NOW() WHERE id = ?";

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cart.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Insert new cart item and return with generated ID.
     */
    private CartItem insertCartItem(CartItem cartItem) throws SQLException {
        String sql = """
                INSERT INTO cart_items (cart_id, menu_item_id, qty, unit_price_snapshot, 
                                      options_price_snapshot, note, item_hash, item_name_snapshot,
                                      created_at, updated_at) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, cartItem.getCartId());
            ps.setLong(2, cartItem.getMenuItemId());
            ps.setInt(3, cartItem.getQty());
            ps.setInt(4, cartItem.getUnitPriceSnapshot());
            ps.setInt(5, cartItem.getOptionsPriceSnapshot());
            ps.setString(6, cartItem.getNote());
            ps.setString(7, cartItem.getItemHash());
            ps.setString(8, cartItem.getItemNameSnapshot());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cartItem.setId(rs.getLong(1));
                        return cartItem;
                    }
                }
            }
        }
        throw new SQLException("Creating cart item failed, no ID obtained.");
    }

    /**
     * Update existing cart item.
     */
    private void updateCartItem(CartItem cartItem) throws SQLException {
        String sql = """
                UPDATE cart_items 
                SET qty = ?, note = ?, updated_at = NOW() 
                WHERE id = ?
                """;

        Connection conn = DBContext.getOrCreate();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartItem.getQty());
            ps.setString(2, cartItem.getNote());
            ps.setLong(3, cartItem.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Maps a ResultSet row to a Cart object.
     */
    private Cart mapToCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setId(rs.getLong("id"));
        cart.setStoreId(rs.getLong("store_id"));
        cart.setUserId(rs.getLong("user_id"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            cart.setCreatedAt(created.toLocalDateTime());
        }

        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) {
            cart.setUpdatedAt(updated.toLocalDateTime());
        }

        return cart;
    }

    /**
     * Maps a ResultSet row to a CartItem object.
     */
    private CartItem mapToCartItem(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setId(rs.getLong("id"));
        item.setCartId(rs.getLong("cart_id"));
        item.setMenuItemId(rs.getLong("menu_item_id"));
        item.setQty(rs.getInt("qty"));
        item.setUnitPriceSnapshot(rs.getInt("unit_price_snapshot"));
        item.setOptionsPriceSnapshot(rs.getInt("options_price_snapshot"));
        item.setNote(rs.getString("note"));
        item.setItemHash(rs.getString("item_hash"));
        item.setItemNameSnapshot(rs.getString("item_name_snapshot"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            item.setCreatedAt(created.toLocalDateTime());
        }

        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) {
            item.setUpdatedAt(updated.toLocalDateTime());
        }

        return item;
    }

    /**
     * Maps a ResultSet row to a CartItemOption object.
     */
    private CartItemOption mapToCartItemOption(ResultSet rs) throws SQLException {
        CartItemOption option = new CartItemOption();
        option.setId(rs.getLong("id"));
        option.setCartItemId(rs.getLong("cart_item_id"));
        option.setOptionValueId(rs.getLong("option_value_id"));
        option.setOptionGroupNameSnapshot(rs.getString("option_group_name_snapshot"));
        option.setOptionValueNameSnapshot(rs.getString("option_value_name_snapshot"));
        option.setPriceDeltaSnapshot(rs.getInt("price_delta_snapshot"));
        return option;
    }
}