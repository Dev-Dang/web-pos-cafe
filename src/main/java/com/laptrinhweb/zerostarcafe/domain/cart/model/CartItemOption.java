package com.laptrinhweb.zerostarcafe.domain.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing an option selected for a cart item. Maps to the {@code cart_item_options} table.
 * Stores snapshot of option information at the time of adding to cart for price consistency.
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
public class CartItemOption {
    private Long id;
    private Long cartItemId;
    private Long optionValueId;
    private String optionGroupNameSnapshot; // JSON: {"vi": "...", "en": "..."}
    private String optionValueNameSnapshot; // JSON: {"vi": "...", "en": "..."}
    private Integer priceDeltaSnapshot;
}