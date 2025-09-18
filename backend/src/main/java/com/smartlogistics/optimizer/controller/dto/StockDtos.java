package com.smartlogistics.optimizer.controller.dto;

import com.smartlogistics.optimizer.model.StockItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StockDtos {

    public record CreateRequest(
            @NotBlank String warehouseId,
            @NotBlank String productName,
            @Min(0) Integer quantity,
            String sku,
            String location
    ) {}

    public record UpdateRequest(
            @NotBlank String warehouseId,
            @NotBlank String productName,
            @Min(0) Integer quantity,
            String sku,
            String location
    ) {}

    public record Response(
            Long id,
            String warehouseId,
            String productName,
            Integer quantity,
            String sku,
            String location
    ) {}
}


