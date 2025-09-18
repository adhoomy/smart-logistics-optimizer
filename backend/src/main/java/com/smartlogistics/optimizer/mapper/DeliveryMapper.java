package com.smartlogistics.optimizer.mapper;

import com.smartlogistics.optimizer.controller.dto.DeliveryDtos;
import com.smartlogistics.optimizer.model.Delivery;
import com.smartlogistics.optimizer.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    @Mapping(target = "orderId", expression = "java(d.getOrder() != null ? d.getOrder().getId() : null)")
    DeliveryDtos.Response toResponse(Delivery d);

    default Delivery fromCreate(DeliveryDtos.CreateRequest req, Order order) {
        Delivery d = new Delivery();
        d.setVehicleId(req.vehicleId());
        d.setRoute(req.route());
        d.setEstimatedTime(req.estimatedTime());
        if (req.status() != null) d.setStatus(req.status());
        d.setOrder(order);
        return d;
    }

    default Delivery update(Delivery d, DeliveryDtos.UpdateRequest req, Order order) {
        d.setVehicleId(req.vehicleId());
        d.setRoute(req.route());
        d.setEstimatedTime(req.estimatedTime());
        if (req.status() != null) d.setStatus(req.status());
        if (order != null) d.setOrder(order);
        return d;
    }
}


