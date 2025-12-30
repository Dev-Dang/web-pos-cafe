package com.laptrinhweb.zerostarcafe.domain.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing a shopping cart for a user at a specific store.
 * Maps directly to the {@code carts} table. Each user has at most one cart
 * per store (enforced by unique constraint).
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * Cart cart = cartDAO.findByUserIdAndStoreId(userId, storeId);
 * int total = cart.getTotalPrice();
 * int itemCount = cart.getTotalQty();
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 29/12/2025
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    private long id;
    private long storeId;
    private long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** Transient: loaded separately from cart_items table */
    private List<CartItem> items = new ArrayList<>();

    /**
     * Calculates the total price of all items in the cart.
     * Total = sum of (unit_price_snapshot + options_price_snapshot) * qty for each item.
     *
     * @return total price in VND
     */
    public int getTotalPrice() {
        int total = 0;
        for (CartItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }

    /**
     * Calculates the total quantity of items in the cart.
     *
     * @return total quantity
     */
    public int getTotalQty() {
        int total = 0;
        for (CartItem item : items) {
            total += item.getQty();
        }
        return total;
    }
}
