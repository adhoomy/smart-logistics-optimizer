package com.smartlogistics.optimizer.mapper;

import com.smartlogistics.optimizer.controller.dto.StockDtos;
import com.smartlogistics.optimizer.model.StockItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {
    StockDtos.Response toResponse(StockItem s);

    default StockItem fromCreate(StockDtos.CreateRequest req) {
        StockItem s = new StockItem();
        s.setWarehouseId(req.warehouseId());
        s.setProductName(req.productName());
        s.setQuantity(req.quantity());
        s.setSku(req.sku());
        s.setLocation(req.location());
        return s;
    }

    default StockItem update(StockItem s, StockDtos.UpdateRequest req) {
        s.setWarehouseId(req.warehouseId());
        s.setProductName(req.productName());
        s.setQuantity(req.quantity());
        s.setSku(req.sku());
        s.setLocation(req.location());
        return s;
    }
}


