package com.smartlogistics.optimizer.model;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public class Delivery {
    private String id;

    @NotBlank
    private String orderId;

    @NotBlank
    private String status; // PENDING, IN_TRANSIT, DELIVERED

    private Instant createdAt;

    public Delivery() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}




