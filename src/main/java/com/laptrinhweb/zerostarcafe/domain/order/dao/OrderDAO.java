package com.laptrinhweb.zerostarcafe.domain.order.dao;

import com.laptrinhweb.zerostarcafe.domain.order.model.Order;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItem;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItemOption;

import java.sql.SQLException;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Data access interface for order operations.
 * Provides CRUD operations for orders, order items, and order item options.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
public interface OrderDAO {

    /**
     * Saves a new order and returns the generated ID.
     *
     * @param order the order to save
     * @return generated order ID
     * @throws SQLException if database error occurs
     */
    long saveOrder(Order order) throws SQLException;

    /**
     * Saves a new order item and returns the generated ID.
     *
     * @param orderItem the order item to save
     * @return generated order item ID
     * @throws SQLException if database error occurs
     */
    long saveOrderItem(OrderItem orderItem) throws SQLException;

    /**
     * Saves a new order item option.
     *
     * @param orderItemOption the order item option to save
     * @throws SQLException if database error occurs
     */
    void saveOrderItemOption(OrderItemOption orderItemOption) throws SQLException;

    /**
     * Updates the status of an order.
     *
     * @param orderId the order ID
     * @param status the new status
     * @throws SQLException if database error occurs
     */
    void updateOrderStatus(long orderId, String status) throws SQLException;

    /**
     * Updates the closed_at timestamp of an order.
     *
     * @param orderId the order ID
     * @throws SQLException if database error occurs
     */
    void markOrderAsClosed(long orderId) throws SQLException;

    /**
     * Finds an order by ID with full details (items + options).
     *
     * @param orderId the order ID
     * @return optional order with full details if found
     * @throws SQLException if database error occurs
     */
    Optional<Order> findById(long orderId) throws SQLException;
}
