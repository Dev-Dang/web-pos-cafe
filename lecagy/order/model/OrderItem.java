package com.laptrinhweb.zerostarcafe.domain.order.model;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents an item within an order.
 * Contains snapshot of prices at time of order for historical accuracy.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
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
    private List<OrderItemOption> options;

    public OrderItem() {
        this.qty = 1;
        this.options = new ArrayList<>();
    }

    // Calculated values
    public int getUnitPriceWithOptions() {
        return unitPriceSnapshot + optionsPriceSnapshot;
    }

    public int getLineTotal() {
        return getUnitPriceWithOptions() * qty;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getUnitPriceSnapshot() {
        return unitPriceSnapshot;
    }

    public void setUnitPriceSnapshot(int unitPriceSnapshot) {
        this.unitPriceSnapshot = unitPriceSnapshot;
    }

    public int getOptionsPriceSnapshot() {
        return optionsPriceSnapshot;
    }

    public void setOptionsPriceSnapshot(int optionsPriceSnapshot) {
        this.optionsPriceSnapshot = optionsPriceSnapshot;
    }

    public String getItemHash() {
        return itemHash;
    }

    public void setItemHash(String itemHash) {
        this.itemHash = itemHash;
    }

    public String getItemNameSnapshot() {
        return itemNameSnapshot;
    }

    public void setItemNameSnapshot(String itemNameSnapshot) {
        this.itemNameSnapshot = itemNameSnapshot;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<OrderItemOption> getOptions() {
        return options;
    }

    public void setOptions(List<OrderItemOption> options) {
        this.options = options != null ? options : new ArrayList<>();
    }

    public void addOption(OrderItemOption option) {
        if (options == null) {
            options = new ArrayList<>();
        }
        options.add(option);
    }
}
