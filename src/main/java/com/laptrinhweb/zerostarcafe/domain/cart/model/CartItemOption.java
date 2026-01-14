package com.laptrinhweb.zerostarcafe.domain.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing selected options for a cart item.
 * Maps to the {@code cart_item_options} table.
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
    private long id;
    private long cartItemId;
    private long optionValueId;
    private String optionGroupNameSnapshot;
    private String optionValueNameSnapshot;
    private int priceDeltaSnapshot;
}