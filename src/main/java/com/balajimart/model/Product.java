package com.balajimart.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private int stock;
    private String imageUrl;
    private Long sellerId;
    private String sellerName;
    private String status = "APPROVED";
    private Timestamp createdAt;

    public Product() {}

    public Product(Long id, String name, String description, BigDecimal price, String category, int stock, String imageUrl, Long sellerId, String sellerName, String status, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.status = status != null ? status : "APPROVED";
        this.createdAt = createdAt;
    }

    public Product(Long id, String name, String description, BigDecimal price, String category, int stock, String imageUrl, Timestamp createdAt) {
        this(id, name, description, price, category, stock, imageUrl, null, null, "APPROVED", createdAt);
    }

    public Product(String name, String description, BigDecimal price, String category, int stock, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.status = "APPROVED";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
