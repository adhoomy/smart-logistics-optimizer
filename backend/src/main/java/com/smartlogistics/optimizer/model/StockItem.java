package com.smartlogistics.optimizer.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public class StockItem {
    private String id;

    @NotBlank
    private String sku;

    @NotBlank
    private String location;

    @Min(0)
    private int quantity;

    private Instant updatedAt;

    public StockItem() {
        this.id = UUID.randomUUID().toString();
        this.updatedAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.updatedAt = Instant.now();
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}




