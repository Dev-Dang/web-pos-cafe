package com.laptrinhweb.zerostarcafe.domain.order.service;

import com.laptrinhweb.zerostarcafe.core.database.DBAction;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;
import com.laptrinhweb.zerostarcafe.domain.order.dao.OrderDAO;
import com.laptrinhweb.zerostarcafe.domain.order.dao.OrderDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.order.model.*;

/**
 * <h2>Description:</h2>
 * <p>
 * Service layer for order operations.
 * Handles order creation from cart and order status management.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public class OrderService {

    /**
     * Creates an order from a cart.
     * Converts cart items and options to order items with snapshot data.
     *
     * @param cart   the cart to convert
     * @param userId the user ID (can be null for guest orders)
     * @return the created order with ID, or null on failure
     */
    public Order createOrderFromCart(Cart cart, Long userId) {
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            LoggerUtil.warn(OrderService.class, "Cannot create order from empty cart");
            return null;
        }

        Order order = buildOrderFromCart(cart, userId);

        return DBAction.run(conn -> {
            OrderDAO orderDAO = new OrderDAOImpl(conn);
            return orderDAO.save(order);
        });
    }

    /**
     * Finds an order by ID.
     *
     * @param orderId the order ID
     * @return the order with items, or null if not found
     */
    public Order findById(long orderId) {
        return DBAction.run(conn -> {
            OrderDAO orderDAO = new OrderDAOImpl(conn);
            return orderDAO.findById(orderId);
        });
    }

    /**
     * Marks an order as paid.
     *
     * @param orderId the order ID
     * @return true if updated successfully
     */
    public boolean markAsPaid(long orderId) {
        Boolean result = DBAction.run(conn -> {
            OrderDAO orderDAO = new OrderDAOImpl(conn);
            return orderDAO.updateStatus(orderId, OrderStatus.PAID.getDbValue());
        });
        return result != null && result;
    }

    /**
     * Builds an Order object from a Cart.
     * Copies all snapshot data from cart items.
     */
    private Order buildOrderFromCart(Cart cart, Long userId) {
        Order order = new Order();
        order.setStoreId(cart.getStoreId());
        order.setUserId(userId);
        order.setStatus(OrderStatus.PAID); // Assume paid immediately for simple checkout
        order.setSource(OrderSource.WEB);

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = convertCartItemToOrderItem(cartItem);
            order.addItem(orderItem);
        }

        return order;
    }

    /**
     * Converts a CartItem to an OrderItem with all snapshot data.
     */
    private OrderItem convertCartItemToOrderItem(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setMenuItemId(cartItem.getMenuItemId());
        orderItem.setQty(cartItem.getQty());
        orderItem.setUnitPriceSnapshot(cartItem.getUnitPriceSnapshot());
        orderItem.setOptionsPriceSnapshot(cartItem.getOptionsPriceSnapshot());
        orderItem.setItemHash(cartItem.getItemHash());
        orderItem.setItemNameSnapshot(cartItem.getItemNameSnapshot());
        orderItem.setNote(cartItem.getNote());

        // Convert cart item options to order item options
        if (cartItem.getOptions() != null) {
            for (CartItemOption cartOption : cartItem.getOptions()) {
                OrderItemOption orderOption = convertCartOptionToOrderOption(cartOption);
                orderItem.addOption(orderOption);
            }
        }

        return orderItem;
    }

    /**
     * Converts a CartItemOption to an OrderItemOption.
     */
    private OrderItemOption convertCartOptionToOrderOption(CartItemOption cartOption) {
        OrderItemOption orderOption = new OrderItemOption();
        orderOption.setOptionValueId(cartOption.getOptionValueId());
        orderOption.setOptionGroupNameSnapshot(cartOption.getOptionGroupNameSnapshot());
        orderOption.setOptionValueNameSnapshot(cartOption.getOptionValueNameSnapshot());
        orderOption.setPriceDeltaSnapshot(cartOption.getPriceDeltaSnapshot());
        return orderOption;
    }
}
