package com.laptrinhweb.zerostarcafe.domain.admin.dto;

public class ActivityLog {
    private String description;
    private String timeAgo;
    private String type;
    private String icon;

    public ActivityLog(String description, String timeAgo, String type, String icon) {
        this.description = description;
        this.timeAgo = timeAgo;
        this.type = type;
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
