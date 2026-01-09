package com.laptrinhweb.zerostarcafe.domain.cart.dao;

import com.laptrinhweb.zerostarcafe.core.context.DBContext;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;

import java.sql.*;
import java.util.*;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link CartDAO}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
public class CartDAOImpl implements CartDAO {

    @Override
    public Optional<Cart> findByUserIdAndStoreId(long userId, long storeId) throws SQLException {
        String sql = "SELECT id, user_id, store_id, created_at, updated_at " +
                "FROM carts WHERE user_id = ? AND store_id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cart cart = mapToCart(rs);

                    // Load full items with options
                    cart.setItems(loadCartItems(cart.getId(), conn));

                    return Optional.of(cart);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<CartItem> findCartItemById(long cartItemId) throws SQLException {
        String sql = "SELECT " +
                "ci.id, ci.cart_id, ci.menu_item_id, ci.qty, ci.unit_price_snapshot, " +
                "ci.options_price_snapshot, ci.note, ci.item_hash, ci.item_name_snapshot, " +
                "ci.image_url_snapshot, ci.created_at, ci.updated_at, " +
                "cio.id as option_id, cio.option_value_id, " +
                "cio.option_group_name_snapshot, cio.option_value_name_snapshot, " +
                "cio.price_delta_snapshot " +
                "FROM cart_items ci " +
                "LEFT JOIN cart_item_options cio ON ci.id = cio.cart_item_id " +
                "WHERE ci.id = ?";

        Connection conn = DBContext.getOrCreate();
        CartItem item = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (item == null) {
                        item = mapToCartItem(rs);
                    }

                    // Add option if exists
                    long optionId = rs.getLong("option_id");
                    if (optionId > 0) {
                        item.getOptions().add(mapToCartItemOption(rs));
                    }
                }
            }
        }

        return Optional.ofNullable(item);
    }

    @Override
    public int countCartItems(long cartId) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM cart_items WHERE cart_id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }

        return 0;
    }

    @Override
    public Cart save(Cart cart) throws SQLException {
        String sql = "INSERT INTO carts (user_id, store_id, created_at, updated_at) " +
                "VALUES (?, ?, NOW(), NOW())";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, cart.getUserId());
            ps.setLong(2, cart.getStoreId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long cartId = rs.getLong(1);
                    cart.setId(cartId);
                    return cart;
                }
            }
        }

        throw new SQLException("Failed to create cart, no ID obtained");
    }

    @Override
    public long saveCartItem(CartItem cartItem) throws SQLException {
        String sql = "INSERT INTO cart_items (cart_id, menu_item_id, qty, unit_price_snapshot, " +
                "options_price_snapshot, note, item_hash, item_name_snapshot, image_url_snapshot, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

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
            ps.setString(9, cartItem.getImageUrlSnapshot());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("Failed to create cart item, no ID obtained");
    }

    @Override
    public void saveCartItemOption(CartItemOption cartItemOption) throws SQLException {
        String sql = "INSERT INTO cart_item_options (cart_item_id, option_value_id, " +
                "option_group_name_snapshot, option_value_name_snapshot, price_delta_snapshot) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemOption.getCartItemId());
            ps.setLong(2, cartItemOption.getOptionValueId());
            ps.setString(3, cartItemOption.getOptionGroupNameSnapshot());
            ps.setString(4, cartItemOption.getOptionValueNameSnapshot());
            ps.setInt(5, cartItemOption.getPriceDeltaSnapshot());

            ps.executeUpdate();
        }
    }

    @Override
    public void updateCartItemQuantity(long cartItemId, int quantity) throws SQLException {
        String sql = "UPDATE cart_items SET qty = ?, updated_at = NOW() WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, cartItemId);

            ps.executeUpdate();
        }
    }

    @Override
    public void deleteCartItem(long cartItemId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);

            ps.executeUpdate();
        }
    }

    @Override
    public void clearCart(long cartId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartId);

            ps.executeUpdate();
        }
    }

    @Override
    public void deleteCart(long cartId) throws SQLException {
        String sql = "DELETE FROM carts WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartId);

            ps.executeUpdate();
        }
    }

    /**
     * Load all cart items with options in single query using LEFT JOIN.
     * Uses LinkedHashMap to group options by cart_item_id (preserve insertion order).
     */
    private List<CartItem> loadCartItems(long cartId, Connection conn) throws SQLException {
        String sql = "SELECT " +
                "ci.id, ci.cart_id, ci.menu_item_id, ci.qty, ci.unit_price_snapshot, " +
                "ci.options_price_snapshot, ci.note, ci.item_hash, ci.item_name_snapshot, " +
                "ci.image_url_snapshot, ci.created_at, ci.updated_at, " +
                "cio.id as option_id, cio.option_value_id, " +
                "cio.option_group_name_snapshot, cio.option_value_name_snapshot, " +
                "cio.price_delta_snapshot " +
                "FROM cart_items ci " +
                "LEFT JOIN cart_item_options cio ON ci.id = cio.cart_item_id " +
                "WHERE ci.cart_id = ? " +
                "ORDER BY ci.created_at DESC";

        Map<Long, CartItem> itemMap = new LinkedHashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long itemId = rs.getLong("id");

                    // Get or create CartItem
                    CartItem item = itemMap.get(itemId);
                    if (item == null) {
                        item = mapToCartItem(rs);
                        itemMap.put(itemId, item);
                    }

                    // Add option if exists
                    long optionId = rs.getLong("option_id");
                    if (optionId > 0) {
                        item.getOptions().add(mapToCartItemOption(rs));
                    }
                }
            }
        }

        return new ArrayList<>(itemMap.values());
    }

    private Cart mapToCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setId(rs.getLong("id"));
        cart.setUserId(rs.getLong("user_id"));
        cart.setStoreId(rs.getLong("store_id"));
        cart.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        cart.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return cart;
    }

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
        item.setImageUrlSnapshot(rs.getString("image_url_snapshot"));
        item.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        item.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return item;
    }

    private CartItemOption mapToCartItemOption(ResultSet rs) throws SQLException {
        CartItemOption option = new CartItemOption();
        option.setId(rs.getLong("option_id"));
        option.setCartItemId(rs.getLong("id"));
        option.setOptionValueId(rs.getLong("option_value_id"));
        option.setOptionGroupNameSnapshot(rs.getString("option_group_name_snapshot"));
        option.setOptionValueNameSnapshot(rs.getString("option_value_name_snapshot"));
        option.setPriceDeltaSnapshot(rs.getInt("price_delta_snapshot"));
        return option;
    }
}
