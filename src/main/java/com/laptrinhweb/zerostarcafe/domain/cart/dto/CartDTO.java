package com.laptrinhweb.zerostarcafe.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO for cart data transfer containing cart information and computed totals.
 * Supports both guest carts (id = null) and logged-in user carts.
 * Focused on cart data only, without payment processing information.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDTO {
    private Long id;                    // null for guest carts
    private Long storeId;
    private Long userId;               // null for guest carts
    private List<CartItemDTO> items;
    private Integer totalQty;          // computed: sum of all item quantities
    private BigDecimal subtotal;       // computed: sum of all line totals
    private BigDecimal total;          // computed: subtotal (no payment calculations here)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}