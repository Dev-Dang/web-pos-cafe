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
 * Domain model representing a single line item in a shopping cart.
 * Maps directly to the {@code cart_items} table.
 * </p>
 *
 * <h2>Key Fields:</h2>
 * <ul>
 *     <li>{@code itemHash} - SHA-256 hash of (menuItemId + sorted optionValueIds + note) for deduplication</li>
 *     <li>{@code unitPriceSnapshot} - Product price at time of adding (immutable)</li>
 *     <li>{@code optionsPriceSnapshot} - Sum of option price deltas (immutable)</li>
 *     <li>{@code itemNameSnapshot} - Product name at time of adding (immutable)</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * CartItem item = new CartItem();
 * item.setMenuItemId(9L);
 * item.setQty(2);
 * item.setUnitPriceSnapshot(39000);
 * int lineTotal = item.getLineTotal();
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
public class CartItem {

    private long id;
    private long cartId;
    private long menuItemId;
    private int qty;
    private int unitPriceSnapshot;
    private int optionsPriceSnapshot;
    private String note;
    private String itemHash;
    private String itemNameSnapshot;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** Transient: loaded separately from cart_item_options table */
    private List<CartItemOption> options = new ArrayList<>();

    /**
     * Calculates the total price for this line item.
     * Line total = (unit_price_snapshot + options_price_snapshot) * qty
     *
     * @return line total in VND
     */
    public int getLineTotal() {
        return (unitPriceSnapshot + optionsPriceSnapshot) * qty;
    }

    /**
     * Calculates the unit price including options.
     * Unit with options = unit_price_snapshot + options_price_snapshot
     *
     * @return unit price with options in VND
     */
    public int getUnitPriceWithOptions() {
        return unitPriceSnapshot + optionsPriceSnapshot;
    }

    /**
     * Calculates the sum of all option price deltas from the options list.
     *
     * @return sum of price deltas
     */
    public int calculateOptionsPriceFromList() {
        int total = 0;
        for (CartItemOption option : options) {
            total += option.getPriceDeltaSnapshot();
        }
        return total;
    }
}
