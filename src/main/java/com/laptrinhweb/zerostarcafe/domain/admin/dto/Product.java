package com.laptrinhweb.zerostarcafe.domain.admin.dto;

public class Product {
    private int id;
    private String name;
    private int categoryId;
    private String categoryName;
    private String picUrl;
    private double price;
    private String description;
    private double inventory;
    private boolean active;
    private String unit;

    public Product() {
    }

    public Product(int id, String unit, double inventory, String description, double price, String picUrl, boolean active, String name) {
        this.id = id;
        this.unit = unit;
        this.inventory = inventory;
        this.description = description;
        this.price = price;
        this.picUrl = picUrl;
        this.name = name;
        this.active = active;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getInventory() {
        return inventory;
    }

    public void setInventory(double inventory) {
        this.inventory = inventory;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
