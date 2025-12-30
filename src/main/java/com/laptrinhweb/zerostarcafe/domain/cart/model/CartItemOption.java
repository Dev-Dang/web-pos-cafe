package com.laptrinhweb.zerostarcafe.domain.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing a selected option for a cart item.
 * Maps directly to the {@code cart_item_options} table.
 * All snapshot fields are captured at the time the item is added to cart
 * and remain immutable even if the source data changes.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * CartItemOption option = new CartItemOption();
 * option.setOptionValueId(16L);
 * option.setOptionGroupNameSnapshot("Size");
 * option.setOptionValueNameSnapshot("L");
 * option.setPriceDeltaSnapshot(6000);
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
public class CartItemOption {

    private long id;
    private long cartItemId;
    private long optionValueId;
    private String optionGroupNameSnapshot;
    private String optionValueNameSnapshot;
    private int priceDeltaSnapshot;
}
