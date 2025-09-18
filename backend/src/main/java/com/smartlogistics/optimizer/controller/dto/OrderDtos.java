package com.smartlogistics.optimizer.controller.dto;

import com.smartlogistics.optimizer.model.Order;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class OrderDtos {

    public record CreateRequest(
            @NotBlank String customerName,
            @NotBlank String destinationAddress,
            @NotNull @Min(1) Integer quantity,
            LocalDateTime deliveryDate,
            Order.OrderStatus status
    ) {}

    public record UpdateRequest(
            @NotBlank String customerName,
            @NotBlank String destinationAddress,
            @NotNull @Min(1) Integer quantity,
            LocalDateTime deliveryDate,
            Order.OrderStatus status
    ) {}

    public record Response(
            Long id,
            String customerName,
            String destinationAddress,
            Integer quantity,
            LocalDateTime deliveryDate,
            Order.OrderStatus status,
            LocalDateTime createdAt
    ) {}
}


