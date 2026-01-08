package com.laptrinhweb.zerostarcafe.domain.order.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents an option selected for an order item.
 * Contains snapshot of option names and prices at time of order.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public class OrderItemOption {

    private long id;
    private long orderItemId;
    private long optionValueId;
    private String optionGroupNameSnapshot;
    private String optionValueNameSnapshot;
    private int priceDeltaSnapshot;

    public OrderItemOption() {
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public long getOptionValueId() {
        return optionValueId;
    }

    public void setOptionValueId(long optionValueId) {
        this.optionValueId = optionValueId;
    }

    public String getOptionGroupNameSnapshot() {
        return optionGroupNameSnapshot;
    }

    public void setOptionGroupNameSnapshot(String optionGroupNameSnapshot) {
        this.optionGroupNameSnapshot = optionGroupNameSnapshot;
    }

    public String getOptionValueNameSnapshot() {
        return optionValueNameSnapshot;
    }

    public void setOptionValueNameSnapshot(String optionValueNameSnapshot) {
        this.optionValueNameSnapshot = optionValueNameSnapshot;
    }

    public int getPriceDeltaSnapshot() {
        return priceDeltaSnapshot;
    }

    public void setPriceDeltaSnapshot(int priceDeltaSnapshot) {
        this.priceDeltaSnapshot = priceDeltaSnapshot;
    }
}
