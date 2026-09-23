package com.balajimart.model;

import java.sql.Timestamp;

public class User {
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private String role = "BUYER";
    private String status = "ACTIVE";
    private Timestamp createdAt;

    public User() {}

    public User(Long id, String name, String email, String passwordHash, String role, String status, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role != null ? role : "BUYER";
        this.status = status != null ? status : "ACTIVE";
        this.createdAt = createdAt;
    }

    public User(String name, String email, String passwordHash, String role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role != null ? role : "BUYER";
        this.status = "ACTIVE";
    }

    public User(String name, String email, String passwordHash) {
        this(name, email, passwordHash, "BUYER");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isSeller() {
        return "SELLER".equalsIgnoreCase(role);
    }

    public boolean isBuyer() {
        return "BUYER".equalsIgnoreCase(role);
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
