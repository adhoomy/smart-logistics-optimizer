package com.smartlogistics.optimizer.service;

import com.smartlogistics.optimizer.controller.dto.OrderDtos;
import com.smartlogistics.optimizer.controller.dto.PaginationDtos;
import com.smartlogistics.optimizer.exception.NotFoundException;
import com.smartlogistics.optimizer.mapper.OrderMapper;
import com.smartlogistics.optimizer.model.Order;
import com.smartlogistics.optimizer.repository.OrderRepository;
import com.smartlogistics.optimizer.repository.specification.OrderSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderDtos.Response create(OrderDtos.CreateRequest req) {
        Order entity = orderMapper.fromCreate(req);
        return orderMapper.toResponse(orderRepository.save(entity));
    }

    public OrderDtos.Response get(Long id) {
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order %d not found".formatted(id)));
        return orderMapper.toResponse(o);
    }

    public List<OrderDtos.Response> list() {
        return orderRepository.findAll().stream().map(orderMapper::toResponse).toList();
    }

    public PaginationDtos.PageResponse<OrderDtos.Response> list(Pageable pageable, 
                                                               Order.OrderStatus status, 
                                                               String customerName) {
        Specification<Order> spec = Specification.where(OrderSpecifications.hasStatus(status))
                .and(OrderSpecifications.hasCustomerNameContaining(customerName));
        
        Page<Order> page = orderRepository.findAll(spec, pageable);
        Page<OrderDtos.Response> responsePage = page.map(orderMapper::toResponse);
        
        return PaginationDtos.PageResponse.of(responsePage);
    }

    public OrderDtos.Response update(Long id, OrderDtos.UpdateRequest req) {
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order %d not found".formatted(id)));
        Order saved = orderRepository.save(orderMapper.update(o, req));
        return orderMapper.toResponse(saved);
    }

    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("Order %d not found".formatted(id));
        }
        orderRepository.deleteById(id);
    }
}


