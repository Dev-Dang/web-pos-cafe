package com.laptrinhweb.zerostarcafe.domain.admin.dto;

public class OrderDetail {
    private String productName;
    private int quantity;
    private long price;
    private String note;

    public OrderDetail(String productName, int quantity, long price, String note) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.note = note;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
