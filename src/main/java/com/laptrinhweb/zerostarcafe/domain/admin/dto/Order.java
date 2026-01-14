package com.laptrinhweb.zerostarcafe.domain.admin.dto;

import java.sql.Timestamp;

public class Order {
    private long id;
    private String customerName;
    private String tableName;
    private Timestamp openedAt;
    private long totalPrice;
    private String status;

    public Order(long id, String customerName, String tableName, Timestamp openedAt, long totalPrice, String status) {
        this.id = id;
        this.customerName = customerName;
        this.tableName = tableName;
        this.openedAt = openedAt;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

