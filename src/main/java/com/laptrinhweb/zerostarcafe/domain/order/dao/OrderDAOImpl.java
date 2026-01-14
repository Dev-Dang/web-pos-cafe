package com.laptrinhweb.zerostarcafe.domain.order.dao;

import com.laptrinhweb.zerostarcafe.core.context.DBContext;
import com.laptrinhweb.zerostarcafe.domain.order.model.Order;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItem;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItemOption;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link OrderDAO}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
public class OrderDAOImpl implements OrderDAO {

    @Override
    public long saveOrder(Order order) throws SQLException {
        String sql = """
                INSERT INTO orders (store_id, table_id, user_id, booking_id, status, opened_at, closed_at, source)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, order.getStoreId());
            
            if (order.getTableId() != null) {
                ps.setLong(2, order.getTableId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            
            if (order.getUserId() != null) {
                ps.setLong(3, order.getUserId());
            } else {
                ps.setNull(3, Types.BIGINT);
            }
            
            if (order.getBookingId() != null) {
                ps.setLong(4, order.getBookingId());
            } else {
                ps.setNull(4, Types.BIGINT);
            }
            
            ps.setString(5, order.getStatus());
            ps.setTimestamp(6, Timestamp.valueOf(order.getOpenedAt()));
            
            if (order.getClosedAt() != null) {
                ps.setTimestamp(7, Timestamp.valueOf(order.getClosedAt()));
            } else {
                ps.setNull(7, Types.TIMESTAMP);
            }
            
            ps.setString(8, order.getSource());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("Failed to create order, no ID obtained");
    }

    @Override
    public long saveOrderItem(OrderItem orderItem) throws SQLException {
        String sql = """
                INSERT INTO order_items (order_id, menu_item_id, qty, unit_price_snapshot, 
                                        options_price_snapshot, item_hash, item_name_snapshot, note)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, orderItem.getOrderId());
            ps.setLong(2, orderItem.getMenuItemId());
            ps.setInt(3, orderItem.getQty());
            ps.setInt(4, orderItem.getUnitPriceSnapshot());
            ps.setInt(5, orderItem.getOptionsPriceSnapshot());
            ps.setString(6, orderItem.getItemHash());
            ps.setString(7, orderItem.getItemNameSnapshot());
            ps.setString(8, orderItem.getNote());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("Failed to create order item, no ID obtained");
    }

    @Override
    public void saveOrderItemOption(OrderItemOption orderItemOption) throws SQLException {
        String sql = """
                INSERT INTO order_item_options (order_item_id, option_value_id, 
                                               option_group_name_snapshot, option_value_name_snapshot, 
                                               price_delta_snapshot)
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderItemOption.getOrderItemId());
            ps.setLong(2, orderItemOption.getOptionValueId());
            ps.setString(3, orderItemOption.getOptionGroupNameSnapshot());
            ps.setString(4, orderItemOption.getOptionValueNameSnapshot());
            ps.setInt(5, orderItemOption.getPriceDeltaSnapshot());

            ps.executeUpdate();
        }
    }

    @Override
    public void updateOrderStatus(long orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, orderId);

            ps.executeUpdate();
        }
    }

    @Override
    public void markOrderAsClosed(long orderId) throws SQLException {
        String sql = "UPDATE orders SET closed_at = NOW() WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);

            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Order> findById(long orderId) throws SQLException {
        String sql = """
                SELECT id, store_id, table_id, user_id, booking_id, status, 
                       opened_at, closed_at, source
                FROM orders
                WHERE id = ?
                """;

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapToOrder(rs);

                    // Load full items with options
                    order.setItems(loadOrderItems(order.getId(), conn));

                    return Optional.of(order);
                }
            }
        }

        return Optional.empty();
    }

    // ==========================================================
    // PRIVATE HELPER METHODS
    // ==========================================================

    /**
     * Load all order items with options in single query using LEFT JOIN.
     */
    private List<OrderItem> loadOrderItems(long orderId, Connection conn) throws SQLException {
        String sql = """
                SELECT oi.id, oi.order_id, oi.menu_item_id, oi.qty, oi.unit_price_snapshot,
                       oi.options_price_snapshot, oi.item_hash, oi.item_name_snapshot, oi.note,
                       oio.id as option_id, oio.option_value_id,
                       oio.option_group_name_snapshot, oio.option_value_name_snapshot,
                       oio.price_delta_snapshot
                FROM order_items oi
                LEFT JOIN order_item_options oio ON oi.id = oio.order_item_id
                WHERE oi.order_id = ?
                ORDER BY oi.id ASC
                """;

        Map<Long, OrderItem> itemsMap = new LinkedHashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long itemId = rs.getLong("id");

                    // Get or create item
                    OrderItem item = itemsMap.computeIfAbsent(itemId, k -> mapToOrderItem(rs));

                    // Add option if exists
                    long optionId = rs.getLong("option_id");
                    if (optionId > 0) {
                        item.getOptions().add(mapToOrderItemOption(rs));
                    }
                }
            }
        }

        return new ArrayList<>(itemsMap.values());
    }

    /**
     * Maps ResultSet to Order object.
     */
    private Order mapToOrder(ResultSet rs) {
        try {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setStoreId(rs.getLong("store_id"));
            
            long tableId = rs.getLong("table_id");
            order.setTableId(rs.wasNull() ? null : tableId);
            
            long userId = rs.getLong("user_id");
            order.setUserId(rs.wasNull() ? null : userId);
            
            long bookingId = rs.getLong("booking_id");
            order.setBookingId(rs.wasNull() ? null : bookingId);
            
            order.setStatus(rs.getString("status"));
            
            Timestamp openedAt = rs.getTimestamp("opened_at");
            order.setOpenedAt(openedAt != null ? openedAt.toLocalDateTime() : LocalDateTime.now());
            
            Timestamp closedAt = rs.getTimestamp("closed_at");
            order.setClosedAt(closedAt != null ? closedAt.toLocalDateTime() : null);
            
            order.setSource(rs.getString("source"));
            
            return order;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to map Order", e);
        }
    }

    /**
     * Maps ResultSet to OrderItem object.
     */
    private OrderItem mapToOrderItem(ResultSet rs) {
        try {
            OrderItem item = new OrderItem();
            item.setId(rs.getLong("id"));
            item.setOrderId(rs.getLong("order_id"));
            item.setMenuItemId(rs.getLong("menu_item_id"));
            item.setQty(rs.getInt("qty"));
            item.setUnitPriceSnapshot(rs.getInt("unit_price_snapshot"));
            item.setOptionsPriceSnapshot(rs.getInt("options_price_snapshot"));
            item.setItemHash(rs.getString("item_hash"));
            item.setItemNameSnapshot(rs.getString("item_name_snapshot"));
            item.setNote(rs.getString("note"));
            return item;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to map OrderItem", e);
        }
    }

    /**
     * Maps ResultSet to OrderItemOption object.
     */
    private OrderItemOption mapToOrderItemOption(ResultSet rs) {
        try {
            OrderItemOption option = new OrderItemOption();
            option.setId(rs.getLong("option_id"));
            option.setOrderItemId(rs.getLong("id"));
            option.setOptionValueId(rs.getLong("option_value_id"));
            option.setOptionGroupNameSnapshot(rs.getString("option_group_name_snapshot"));
            option.setOptionValueNameSnapshot(rs.getString("option_value_name_snapshot"));
            option.setPriceDeltaSnapshot(rs.getInt("price_delta_snapshot"));
            return option;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to map OrderItemOption", e);
        }
    }
}
