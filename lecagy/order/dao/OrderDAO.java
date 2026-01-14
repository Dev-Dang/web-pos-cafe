package com.laptrinhweb.zerostarcafe.domain.order.dao;

import com.laptrinhweb.zerostarcafe.domain.order.model.Order;

/**
 * <h2>Description:</h2>
 * <p>
 * Data Access Object interface for Order entities.
 * Follows JPA naming conventions for method signatures.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public interface OrderDAO {

    /**
     * Saves a new order with all its items and options.
     *
     * @param order the order to save
     * @return the saved order with generated IDs, or null on failure
     */
    Order save(Order order);

    /**
     * Finds an order by its ID, including items and options.
     *
     * @param orderId the order ID
     * @return the order, or null if not found
     */
    Order findById(long orderId);

    /**
     * Updates the status of an order.
     *
     * @param orderId   the order ID
     * @param newStatus the new status value
     * @return true if updated successfully
     */
    boolean updateStatus(long orderId, String newStatus);
}
