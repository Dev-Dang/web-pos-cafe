package com.laptrinhweb.zerostarcafe.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing an item in an order with full option details.
 * Maps to the {@code order_items} table and includes related options via join.
 * </p>
 *
 * <h2>Key Differences from CartItem:</h2>
 * <ul>
 *     <li>No created_at/updated_at (orders are immutable)</li>
 *     <li>No image_url_snapshot (not needed for order history)</li>
 *     <li>Stores item_name_snapshot as JSON for i18n support</li>
 * </ul>
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
public class OrderItem {
    private long id;
    private long orderId;
    private long menuItemId;
    private int qty;
    private int unitPriceSnapshot;
    private int optionsPriceSnapshot;
    private String itemHash;
    private String itemNameSnapshot;
    private String note;

    private List<OrderItemOption> options = new ArrayList<>();
}
