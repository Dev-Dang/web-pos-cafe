package com.laptrinhweb.zerostarcafe.domain.order.service;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.exception.BusinessException;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.cart.dao.CartDAO;
import com.laptrinhweb.zerostarcafe.domain.cart.dao.CartDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.order.dao.OrderDAO;
import com.laptrinhweb.zerostarcafe.domain.order.dao.OrderDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.order.model.Order;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderConstants;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItem;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItemOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Service layer for order business logic.
 * Handles order creation from cart, order status management.
 * </p>
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderService {

    private static final OrderService INSTANCE = new OrderService();
    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final CartDAO cartDAO = new CartDAOImpl();

    public static OrderService getInstance() {
        return INSTANCE;
    }

    /**
     * Creates an order from cart data and saves it atomically.
     * CRITICAL: Loads Cart entities (not DTO) to preserve multilingual JSON snapshots.
     *
     * @param cart    the cart DTO (for validation)
     * @param userId  the user ID (can be null for guest orders)
     * @param storeId the store ID
     * @return generated order ID
     */
    public long createOrderFromCart(@NonNull CartDTO cart, Long userId, @NonNull Long storeId) {

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BusinessException("Cannot create order from empty cart");
        }

        try {
            Optional<Cart> cartEntityOpt = cartDAO.findByUserIdAndStoreId(userId, storeId);
            if (cartEntityOpt.isEmpty()) {
                throw new BusinessException("Cart not found for user=" + userId + ", store=" + storeId);
            }

            Cart cartEntity = cartEntityOpt.get();
            if (cartEntity.getItems() == null || cartEntity.getItems().isEmpty()) {
                throw new BusinessException("Cannot create order from empty cart");
            }

            // 1. Create Order header using mapper
            Order order = OrderMapper.toOrder(cart, userId, storeId);
            long orderId = orderDAO.saveOrder(order);

            LoggerUtil.info(OrderService.class,
                    String.format("Created order ID=%d for user=%d, store=%d",
                            orderId, userId, storeId));

            // 2. Create Order Items using Cart ENTITIES (with JSON snapshots)
            for (CartItem cartItem : cartEntity.getItems()) {
                OrderItem orderItem = OrderMapper.toOrderItem(cartItem, orderId);
                long orderItemId = orderDAO.saveOrderItem(orderItem);

                // 3. Create Order Item Options using Cart ENTITIES (with JSON snapshots)
                List<OrderItemOption> orderOptions = OrderMapper.toOrderItemOptions(
                        cartItem.getOptions(), orderItemId
                );

                for (OrderItemOption orderOption : orderOptions) {
                    orderDAO.saveOrderItemOption(orderOption);
                }
            }

            return orderId;

        } catch (SQLException e) {
            throw new AppException("Failed to create order from cart", e);
        }
    }

    /**
     * Marks an order as paid and closes it.
     *
     * @param orderId the order ID
     */
    public void markOrderAsPaid(@NonNull Long orderId) {
        try {
            orderDAO.updateOrderStatus(orderId, OrderConstants.Status.PAID);
            orderDAO.markOrderAsClosed(orderId);

            LoggerUtil.info(OrderService.class,
                    String.format("Marked order ID=%d as PAID and closed", orderId));

        } catch (SQLException e) {
            throw new AppException("Failed to mark order as paid: " + orderId, e);
        }
    }

    /**
     * Marks an order as void (cancelled).
     *
     * @param orderId the order ID
     */
    public void markOrderAsVoid(@NonNull Long orderId) {
        try {
            orderDAO.updateOrderStatus(orderId, OrderConstants.Status.VOID);
            orderDAO.markOrderAsClosed(orderId);

            LoggerUtil.info(OrderService.class,
                    String.format("Marked order ID=%d as VOID", orderId));

        } catch (SQLException e) {
            throw new AppException("Failed to mark order as void: " + orderId, e);
        }
    }
}

