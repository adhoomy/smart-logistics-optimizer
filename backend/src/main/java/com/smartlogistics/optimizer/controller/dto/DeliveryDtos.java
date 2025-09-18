package com.smartlogistics.optimizer.controller.dto;

import com.smartlogistics.optimizer.model.Delivery;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class DeliveryDtos {

    public record CreateRequest(
            @NotBlank String vehicleId,
            String route,
            LocalDateTime estimatedTime,
            Delivery.DeliveryStatus status,
            Long orderId
    ) {}

    public record UpdateRequest(
            @NotBlank String vehicleId,
            String route,
            LocalDateTime estimatedTime,
            Delivery.DeliveryStatus status,
            Long orderId
    ) {}

    public record Response(
            Long id,
            String vehicleId,
            String route,
            LocalDateTime estimatedTime,
            Delivery.DeliveryStatus status,
            Long orderId,
            LocalDateTime createdAt
    ) {}
}


