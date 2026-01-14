package com.laptrinhweb.zerostarcafe.domain.admin.dto;

import java.sql.Timestamp;

public class DashboardHandleOrder {
    private long id;
    private String tableName;
    private Timestamp openedAt;
    private String itemSummary;
    private long totalPrice;
    private String status;

    public DashboardHandleOrder() {
        this.id = id;
        this.status = status;
        this.totalPrice = totalPrice;
        this.itemSummary = itemSummary;
        this.openedAt = openedAt;
        this.tableName = tableName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Timestamp getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(Timestamp openedAt) {
        this.openedAt = openedAt;
    }

    public String getItemSummary() {
        return itemSummary;
    }

    public void setItemSummary(String itemSummary) {
        this.itemSummary = itemSummary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
