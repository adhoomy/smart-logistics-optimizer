package com.smartlogistics.optimizer.mapper;

import com.smartlogistics.optimizer.controller.dto.OrderDtos;
import com.smartlogistics.optimizer.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDtos.Response toResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", expression = "java(req.status() != null ? req.status() : order.getStatus())")
    default Order update(Order order, OrderDtos.UpdateRequest req) {
        order.setCustomerName(req.customerName());
        order.setDestinationAddress(req.destinationAddress());
        order.setQuantity(req.quantity());
        order.setDeliveryDate(req.deliveryDate());
        if (req.status() != null) order.setStatus(req.status());
        return order;
    }

    default Order fromCreate(OrderDtos.CreateRequest req) {
        Order o = new Order();
        o.setCustomerName(req.customerName());
        o.setDestinationAddress(req.destinationAddress());
        o.setQuantity(req.quantity());
        o.setDeliveryDate(req.deliveryDate());
        if (req.status() != null) o.setStatus(req.status());
        return o;
    }
}


