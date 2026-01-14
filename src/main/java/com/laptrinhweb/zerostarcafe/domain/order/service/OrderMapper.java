package com.laptrinhweb.zerostarcafe.domain.order.service;

import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartItemDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartItemOptionDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;
import com.laptrinhweb.zerostarcafe.domain.order.model.Order;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderConstants;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItem;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItemOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Mapper for order domain conversions.
 * Handles CartItem → Order/OrderItem/OrderItemOption transformations.
 * IMPORTANT: Uses CartItem (entity) not CartItemDTO to preserve JSON snapshots.
 * </p>
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderMapper {

    /**
     * Converts CartDTO to Order header (without items).
     *
     * @param cart the cart data
     * @param userId the user ID (can be null for guest)
     * @param storeId the store ID
     * @return Order header ready to save
     */
    public static Order toOrder(CartDTO cart, Long userId, Long storeId) {
        Order order = new Order();
        order.setStoreId(storeId);
        order.setTableId(null);  // Web orders have no table
        order.setUserId(userId);
        order.setBookingId(null);  // Instant orders have no booking
        order.setStatus(OrderConstants.Status.OPEN);
        order.setOpenedAt(LocalDateTime.now());
        order.setClosedAt(null);
        order.setSource(OrderConstants.Source.WEB);
        return order;
    }

    /**
     * Converts CartItem (entity) to OrderItem.
     * CRITICAL: Uses CartItem entity (not DTO) to preserve multilingual JSON snapshots.
     *
     * @param cartItem the cart item ENTITY (with JSON snapshots)
     * @param orderId the order ID to associate with
     * @return OrderItem ready to save with multilingual JSON
     */
    public static OrderItem toOrderItem(CartItem cartItem, long orderId) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setMenuItemId(cartItem.getMenuItemId());
        orderItem.setQty(cartItem.getQty());
        orderItem.setUnitPriceSnapshot(cartItem.getUnitPriceSnapshot());
        orderItem.setOptionsPriceSnapshot(cartItem.getOptionsPriceSnapshot());
        orderItem.setItemHash(cartItem.getItemHash());
        
        // ✅ FIX: Use JSON snapshot from entity, not localized string from DTO
        orderItem.setItemNameSnapshot(cartItem.getItemNameSnapshot());  // JSON: {"vi": "...", "en": "..."}
        orderItem.setNote(cartItem.getNote());
        return orderItem;
    }

    /**
     * Converts CartItemOption (entity) to OrderItemOption.
     * CRITICAL: Uses CartItemOption entity to preserve multilingual JSON snapshots.
     *
     * @param cartOption the cart option ENTITY (with JSON snapshots)
     * @param orderItemId the order item ID to associate with
     * @return OrderItemOption ready to save with multilingual JSON
     */
    public static OrderItemOption toOrderItemOption(CartItemOption cartOption, long orderItemId) {
        OrderItemOption orderOption = new OrderItemOption();
        orderOption.setOrderItemId(orderItemId);
        orderOption.setOptionValueId(cartOption.getOptionValueId());
        
        // ✅ FIX: Use JSON snapshots from entity, not localized strings from DTO
        orderOption.setOptionGroupNameSnapshot(cartOption.getOptionGroupNameSnapshot());  // JSON
        orderOption.setOptionValueNameSnapshot(cartOption.getOptionValueNameSnapshot());  // JSON
        orderOption.setPriceDeltaSnapshot(cartOption.getPriceDeltaSnapshot());
        return orderOption;
    }

    /**
     * Converts list of CartItemOption (entities) to list of OrderItemOption.
     *
     * @param cartOptions the cart option ENTITIES (with JSON snapshots)
     * @param orderItemId the order item ID to associate with
     * @return List of OrderItemOption ready to save
     */
    public static List<OrderItemOption> toOrderItemOptions(List<CartItemOption> cartOptions, long orderItemId) {
        if (cartOptions == null || cartOptions.isEmpty()) {
            return new ArrayList<>();
        }

        List<OrderItemOption> orderOptions = new ArrayList<>();
        for (CartItemOption cartOption : cartOptions) {
            orderOptions.add(toOrderItemOption(cartOption, orderItemId));
        }
        return orderOptions;
    }
}
