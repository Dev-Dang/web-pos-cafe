package com.laptrinhweb.zerostarcafe.domain.order.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents an order placed by a customer.
 * Contains order items with their options and tracks payment status.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public class Order {

    private long id;
    private long storeId;
    private Long tableId;
    private Long userId;
    private Long bookingId;
    private OrderStatus status;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private OrderSource source;
    private List<OrderItem> items;

    public Order() {
        this.items = new ArrayList<>();
        this.status = OrderStatus.OPEN;
        this.source = OrderSource.WEB;
        this.openedAt = LocalDateTime.now();
    }

    // Calculated totals
    public int getTotalPrice() {
        int total = 0;
        for (OrderItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }

    public int getTotalQty() {
        int total = 0;
        for (OrderItem item : items) {
            total += item.getQty();
        }
        return total;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public OrderSource getSource() {
        return source;
    }

    public void setSource(OrderSource source) {
        this.source = source;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
    }
}
