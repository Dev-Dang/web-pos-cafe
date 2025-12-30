package com.laptrinhweb.zerostarcafe.domain.cart.dao;

import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link CartDAO} that interacts with the
 * {@code carts}, {@code cart_items}, and {@code cart_item_options} tables.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * try (Connection conn = DBConnection.getConnection()) {
 *     CartDAO cartDAO = new CartDAOImpl(conn);
 *     Cart cart = cartDAO.findByUserIdAndStoreId(userId, storeId);
 *     if (cart == null) {
 *         cart = new Cart();
 *         cart.setUserId(userId);
 *         cart.setStoreId(storeId);
 *         cart = cartDAO.save(cart);
 *     }
 * }
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 29/12/2025
 * @since 1.0.0
 */
public class CartDAOImpl implements CartDAO {

    private final Connection conn;

    public CartDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // ==========================================================
    // CART OPERATIONS
    // ==========================================================

    @Override
    public Cart findByUserIdAndStoreId(long userId, long storeId) throws SQLException {
        String sql = """
                SELECT id, store_id, user_id, created_at, updated_at
                FROM carts
                WHERE user_id = ? AND store_id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Cart cart = mapCart(rs);
                cart.setItems(findItemsByCartId(cart.getId()));
                return cart;
            }
        }
    }

    @Override
    public Cart save(Cart cart) throws SQLException {
        String sql = """
                INSERT INTO carts (store_id, user_id)
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, cart.getStoreId());
            ps.setLong(2, cart.getUserId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cart.setId(rs.getLong(1));
                }
            }
        }

        return cart;
    }

    @Override
    public void deleteById(long cartId) throws SQLException {
        String sql = "DELETE FROM carts WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartId);
            ps.executeUpdate();
        }
    }

    // ==========================================================
    // CART ITEM OPERATIONS
    // ==========================================================

    @Override
    public List<CartItem> findItemsByCartId(long cartId) throws SQLException {
        String sql = """
                SELECT id, cart_id, menu_item_id, qty, unit_price_snapshot,
                       options_price_snapshot, note, item_hash, item_name_snapshot,
                       created_at, updated_at
                FROM cart_items
                WHERE cart_id = ?
                ORDER BY created_at
                """;

        List<CartItem> items = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = mapCartItem(rs);
                    item.setOptions(findOptionsByCartItemId(item.getId()));
                    items.add(item);
                }
            }
        }

        return items;
    }

    @Override
    public CartItem findItemByCartIdAndItemHash(long cartId, String itemHash) throws SQLException {
        String sql = """
                SELECT id, cart_id, menu_item_id, qty, unit_price_snapshot,
                       options_price_snapshot, note, item_hash, item_name_snapshot,
                       created_at, updated_at
                FROM cart_items
                WHERE cart_id = ? AND item_hash = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartId);
            ps.setString(2, itemHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                CartItem item = mapCartItem(rs);
                item.setOptions(findOptionsByCartItemId(item.getId()));
                return item;
            }
        }
    }

    @Override
    public CartItem findItemById(long itemId) throws SQLException {
        String sql = """
                SELECT id, cart_id, menu_item_id, qty, unit_price_snapshot,
                       options_price_snapshot, note, item_hash, item_name_snapshot,
                       created_at, updated_at
                FROM cart_items
                WHERE id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                CartItem item = mapCartItem(rs);
                item.setOptions(findOptionsByCartItemId(item.getId()));
                return item;
            }
        }
    }

    @Override
    public CartItem saveItem(CartItem item) throws SQLException {
        String sql = """
                INSERT INTO cart_items 
                    (cart_id, menu_item_id, qty, unit_price_snapshot, 
                     options_price_snapshot, note, item_hash, item_name_snapshot)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, item.getCartId());
            ps.setLong(2, item.getMenuItemId());
            ps.setInt(3, item.getQty());
            ps.setInt(4, item.getUnitPriceSnapshot());
            ps.setInt(5, item.getOptionsPriceSnapshot());
            ps.setString(6, item.getNote());
            ps.setString(7, item.getItemHash());
            ps.setString(8, item.getItemNameSnapshot());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getLong(1));
                }
            }
        }

        return item;
    }

    @Override
    public void updateItemQty(long itemId, int qty) throws SQLException {
        String sql = "UPDATE cart_items SET qty = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setLong(2, itemId);
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteItemById(long itemId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, itemId);
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteAllItemsByCartId(long cartId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartId);
            ps.executeUpdate();
        }
    }

    // ==========================================================
    // CART ITEM OPTIONS OPERATIONS
    // ==========================================================

    @Override
    public List<CartItemOption> findOptionsByCartItemId(long cartItemId) throws SQLException {
        String sql = """
                SELECT id, cart_item_id, option_value_id, 
                       option_group_name_snapshot, option_value_name_snapshot, 
                       price_delta_snapshot
                FROM cart_item_options
                WHERE cart_item_id = ?
                ORDER BY id
                """;

        List<CartItemOption> options = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    options.add(mapCartItemOption(rs));
                }
            }
        }

        return options;
    }

    @Override
    public CartItemOption saveOption(CartItemOption option) throws SQLException {
        String sql = """
                INSERT INTO cart_item_options 
                    (cart_item_id, option_value_id, option_group_name_snapshot, 
                     option_value_name_snapshot, price_delta_snapshot)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, option.getCartItemId());
            ps.setLong(2, option.getOptionValueId());
            ps.setString(3, option.getOptionGroupNameSnapshot());
            ps.setString(4, option.getOptionValueNameSnapshot());
            ps.setInt(5, option.getPriceDeltaSnapshot());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    option.setId(rs.getLong(1));
                }
            }
        }

        return option;
    }

    // ==========================================================
    // MAPPING UTILITIES
    // ==========================================================

    private Cart mapCart(ResultSet rs) throws SQLException {
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

    private CartItem mapCartItem(ResultSet rs) throws SQLException {
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

    private CartItemOption mapCartItemOption(ResultSet rs) throws SQLException {
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
