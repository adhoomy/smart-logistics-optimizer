package com.smartlogistics.optimizer.service;

import com.smartlogistics.optimizer.controller.dto.DeliveryDtos;
import com.smartlogistics.optimizer.exception.NotFoundException;
import com.smartlogistics.optimizer.mapper.DeliveryMapper;
import com.smartlogistics.optimizer.model.Delivery;
import com.smartlogistics.optimizer.model.Order;
import com.smartlogistics.optimizer.repository.DeliveryRepository;
import com.smartlogistics.optimizer.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final DeliveryMapper deliveryMapper;

    public DeliveryService(DeliveryRepository deliveryRepository, OrderRepository orderRepository, DeliveryMapper deliveryMapper) {
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.deliveryMapper = deliveryMapper;
    }

    public DeliveryDtos.Response create(DeliveryDtos.CreateRequest req) {
        Order order = orderRepository.findById(req.orderId())
                .orElseThrow(() -> new NotFoundException("Order %d not found".formatted(req.orderId())));
        Delivery saved = deliveryRepository.save(deliveryMapper.fromCreate(req, order));
        return deliveryMapper.toResponse(saved);
    }

    public DeliveryDtos.Response get(Long id) {
        Delivery d = deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery %d not found".formatted(id)));
        return deliveryMapper.toResponse(d);
    }

    public List<DeliveryDtos.Response> list() {
        return deliveryRepository.findAll().stream().map(deliveryMapper::toResponse).toList();
    }

    public DeliveryDtos.Response update(Long id, DeliveryDtos.UpdateRequest req) {
        Delivery d = deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery %d not found".formatted(id)));
        Order order = null;
        if (req.orderId() != null) {
            order = orderRepository.findById(req.orderId())
                    .orElseThrow(() -> new NotFoundException("Order %d not found".formatted(req.orderId())));
        }
        Delivery saved = deliveryRepository.save(deliveryMapper.update(d, req, order));
        return deliveryMapper.toResponse(saved);
    }

    public void delete(Long id) {
        if (!deliveryRepository.existsById(id)) {
            throw new NotFoundException("Delivery %d not found".formatted(id));
        }
        deliveryRepository.deleteById(id);
    }
}


