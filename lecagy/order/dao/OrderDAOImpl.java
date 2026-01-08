package com.laptrinhweb.zerostarcafe.domain.order.dao;

import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.order.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of OrderDAO.
 * Uses connection passed from service layer to support transactions.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public class OrderDAOImpl implements OrderDAO {

    private static final String SQL_INSERT_ORDER =
            "INSERT INTO orders (store_id, table_id, user_id, booking_id, status, opened_at, source) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_ORDER_ITEM =
            "INSERT INTO order_items (order_id, menu_item_id, qty, unit_price_snapshot, " +
                    "options_price_snapshot, item_hash, item_name_snapshot, note) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_ORDER_ITEM_OPTION =
            "INSERT INTO order_item_options (order_item_id, option_value_id, " +
                    "option_group_name_snapshot, option_value_name_snapshot, price_delta_snapshot) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_FIND_ORDER_BY_ID =
            "SELECT * FROM orders WHERE id = ?";

    private static final String SQL_FIND_ORDER_ITEMS =
            "SELECT * FROM order_items WHERE order_id = ?";

    private static final String SQL_FIND_ORDER_ITEM_OPTIONS =
            "SELECT * FROM order_item_options WHERE order_item_id = ?";

    private static final String SQL_UPDATE_STATUS =
            "UPDATE orders SET status = ?, closed_at = CASE WHEN ? = 'paid' THEN NOW() ELSE closed_at END " +
                    "WHERE id = ?";

    private final Connection conn;

    public OrderDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Order save(Order order) {
        try {
            // Insert order
            PreparedStatement orderStmt = conn.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setLong(1, order.getStoreId());
            orderStmt.setObject(2, order.getTableId());
            orderStmt.setObject(3, order.getUserId());
            orderStmt.setObject(4, order.getBookingId());
            orderStmt.setString(5, order.getStatus().getDbValue());
            orderStmt.setTimestamp(6, Timestamp.valueOf(order.getOpenedAt()));
            orderStmt.setString(7, order.getSource().getDbValue());

            int affected = orderStmt.executeUpdate();
            if (affected == 0) {
                return null;
            }

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setId(generatedKeys.getLong(1));
            }
            generatedKeys.close();
            orderStmt.close();

            // Insert order items
            for (OrderItem item : order.getItems()) {
                item.setOrderId(order.getId());
                saveOrderItem(item);
            }

            return order;

        } catch (SQLException e) {
            LoggerUtil.error(OrderDAOImpl.class, "Failed to save order", e);
            return null;
        }
    }

    private void saveOrderItem(OrderItem item) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_ORDER_ITEM, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, item.getOrderId());
        stmt.setLong(2, item.getMenuItemId());
        stmt.setInt(3, item.getQty());
        stmt.setInt(4, item.getUnitPriceSnapshot());
        stmt.setInt(5, item.getOptionsPriceSnapshot());
        stmt.setString(6, item.getItemHash());
        stmt.setString(7, item.getItemNameSnapshot());
        stmt.setString(8, item.getNote());

        stmt.executeUpdate();

        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            item.setId(generatedKeys.getLong(1));
        }
        generatedKeys.close();
        stmt.close();

        // Insert item options
        for (OrderItemOption option : item.getOptions()) {
            option.setOrderItemId(item.getId());
            saveOrderItemOption(option);
        }
    }

    private void saveOrderItemOption(OrderItemOption option) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_ORDER_ITEM_OPTION, Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, option.getOrderItemId());
        stmt.setLong(2, option.getOptionValueId());
        stmt.setString(3, option.getOptionGroupNameSnapshot());
        stmt.setString(4, option.getOptionValueNameSnapshot());
        stmt.setInt(5, option.getPriceDeltaSnapshot());

        stmt.executeUpdate();

        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            option.setId(generatedKeys.getLong(1));
        }
        generatedKeys.close();
        stmt.close();
    }

    @Override
    public Order findById(long orderId) {
        try {
            PreparedStatement stmt = conn.prepareStatement(SQL_FIND_ORDER_BY_ID);
            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();

            Order order = null;
            if (rs.next()) {
                order = mapOrder(rs);
                order.setItems(findOrderItems(orderId));
            }

            rs.close();
            stmt.close();
            return order;

        } catch (SQLException e) {
            LoggerUtil.error(OrderDAOImpl.class, "Failed to find order by ID: " + orderId, e);
            return null;
        }
    }

    private List<OrderItem> findOrderItems(long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement(SQL_FIND_ORDER_ITEMS);
        stmt.setLong(1, orderId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            OrderItem item = mapOrderItem(rs);
            item.setOptions(findOrderItemOptions(item.getId()));
            items.add(item);
        }

        rs.close();
        stmt.close();
        return items;
    }

    private List<OrderItemOption> findOrderItemOptions(long orderItemId) throws SQLException {
        List<OrderItemOption> options = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement(SQL_FIND_ORDER_ITEM_OPTIONS);
        stmt.setLong(1, orderItemId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            options.add(mapOrderItemOption(rs));
        }

        rs.close();
        stmt.close();
        return options;
    }

    @Override
    public boolean updateStatus(long orderId, String newStatus) {
        try {
            PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_STATUS);
            stmt.setString(1, newStatus);
            stmt.setString(2, newStatus);
            stmt.setLong(3, orderId);

            int affected = stmt.executeUpdate();
            stmt.close();
            return affected > 0;

        } catch (SQLException e) {
            LoggerUtil.error(OrderDAOImpl.class, "Failed to update order status", e);
            return false;
        }
    }

    // Mapping methods
    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setStoreId(rs.getLong("store_id"));

        long tableId = rs.getLong("table_id");
        order.setTableId(rs.wasNull() ? null : tableId);

        long userId = rs.getLong("user_id");
        order.setUserId(rs.wasNull() ? null : userId);

        long bookingId = rs.getLong("booking_id");
        order.setBookingId(rs.wasNull() ? null : bookingId);

        order.setStatus(OrderStatus.fromDbValue(rs.getString("status")));

        Timestamp openedAt = rs.getTimestamp("opened_at");
        if (openedAt != null) {
            order.setOpenedAt(openedAt.toLocalDateTime());
        }

        Timestamp closedAt = rs.getTimestamp("closed_at");
        if (closedAt != null) {
            order.setClosedAt(closedAt.toLocalDateTime());
        }

        order.setSource(OrderSource.fromDbValue(rs.getString("source")));

        return order;
    }

    private OrderItem mapOrderItem(ResultSet rs) throws SQLException {
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
    }

    private OrderItemOption mapOrderItemOption(ResultSet rs) throws SQLException {
        OrderItemOption option = new OrderItemOption();
        option.setId(rs.getLong("id"));
        option.setOrderItemId(rs.getLong("order_item_id"));
        option.setOptionValueId(rs.getLong("option_value_id"));
        option.setOptionGroupNameSnapshot(rs.getString("option_group_name_snapshot"));
        option.setOptionValueNameSnapshot(rs.getString("option_value_name_snapshot"));
        option.setPriceDeltaSnapshot(rs.getInt("price_delta_snapshot"));
        return option;
    }
}
