package com.laptrinhweb.zerostarcafe.domain.admin.dto;

public class BestSeller {
    private String name;
    private String categoryName;
    private String imageUrl;
    private long totalSold;

    public BestSeller(String name, String categoryName, String imageUrl, long totalSold) {
        this.name = name;
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;
        this.totalSold = totalSold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(long totalSold) {
        this.totalSold = totalSold;
    }
}
