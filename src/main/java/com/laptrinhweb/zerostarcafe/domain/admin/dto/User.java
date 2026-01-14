package com.laptrinhweb.zerostarcafe.domain.admin.dto;

import java.sql.Timestamp;

public class User {
    private long id;
    private String username;
    private String email;
    private String passwordHash;
    private String status;
    private boolean isSuperAdmin;
    private Timestamp createdAt;

    public User() {
    }

    public User(long id, String username, String email, String status, boolean isSuperAdmin, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.isSuperAdmin = isSuperAdmin;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}