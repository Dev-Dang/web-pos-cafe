package com.laptrinhweb.zerostarcafe.domain.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing an item in a shopping cart. Maps to the {@code cart_items} table.
 * Contains product information snapshot and selected options at the time of adding to cart.
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
public class CartItem {
    private Long id;
    private Long cartId;
    private Long menuItemId;
    private Integer qty;
    private Integer unitPriceSnapshot;
    private Integer optionsPriceSnapshot;
    private String note;
    private String itemHash;
    private String itemNameSnapshot; // JSON: {"vi": "...", "en": "..."}
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Related entities
    private List<CartItemOption> options;
}