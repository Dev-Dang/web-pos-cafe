package com.laptrinhweb.zerostarcafe.domain.admin.dto;

import java.sql.Timestamp;

public class Log {
    private long id;
    private String userName;
    private String action;
    private String description;
    private String ipAddress;
    private String level;
    private Timestamp createdAt;

    public Log() {
    }

    public Log(long id, String userName, String action, String description, String ipAddress, String level, Timestamp createdAt) {
        this.id = id;
        this.userName = userName;
        this.action = action;
        this.description = description;
        this.ipAddress = ipAddress;
        this.level = level;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
