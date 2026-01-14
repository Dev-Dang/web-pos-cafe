package com.laptrinhweb.zerostarcafe.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing selected options for an order item.
 * Maps to the {@code order_item_options} table.
 * </p>
 *
 * <h2>Snapshot Design:</h2>
 * <p>
 * Stores names as JSON for i18n (e.g., {"vi": "Lớn", "en": "Large"})
 * to preserve exact values at time of order, even if menu changes later.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemOption {
    private long id;
    private long orderItemId;
    private long optionValueId;
    private String optionGroupNameSnapshot;
    private String optionValueNameSnapshot;
    private int priceDeltaSnapshot;
}
