package com.laptrinhweb.zerostarcafe.domain.admin.dto;

public class Overview {
    private long totalRevenue;
    private long totalOrders;
    private long sellingProducts;
    private int newCustomer;

    public Overview(long totalRevenue, long totalOrders, long sellingProducts, int newCustomer) {
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.sellingProducts = sellingProducts;
        this.newCustomer = newCustomer;
    }

    public long getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(long totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getSellingProducts() {
        return sellingProducts;
    }

    public void setSellingProducts(long sellingProducts) {
        this.sellingProducts = sellingProducts;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(int newCustomer) {
        this.newCustomer = newCustomer;
    }
}
