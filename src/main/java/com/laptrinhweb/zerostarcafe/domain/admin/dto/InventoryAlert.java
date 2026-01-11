package com.laptrinhweb.zerostarcafe.domain.admin.dto;

public class InventoryAlert {

    private String name;
    private String sku;
    private String imageUrl;
    private double stock;

    public InventoryAlert(String name, String sku, String imageUrl, double stock) {
        this.name = name;
        this.sku = sku;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }
}
