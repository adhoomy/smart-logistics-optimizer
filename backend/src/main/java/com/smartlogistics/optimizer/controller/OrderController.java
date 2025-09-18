package com.smartlogistics.optimizer.controller;

import com.smartlogistics.optimizer.model.Order;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final Map<Long, Order> store = new ConcurrentHashMap<>();

    @GetMapping
    public Collection<Order> list() {
        return store.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable Long id) {
        Order order = store.get(id);
        return order == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody @Valid Order order) {
        store.put(order.getId(), order);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody @Valid Order payload) {
        Order existing = store.get(id);
        if (existing == null) return ResponseEntity.notFound().build();
        existing.setCustomerName(payload.getCustomerName());
        existing.setDestinationAddress(payload.getDestinationAddress());
        existing.setQuantity(payload.getQuantity());
        existing.setDeliveryDate(payload.getDeliveryDate());
        existing.setStatus(payload.getStatus());
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return store.remove(id) == null ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }
}




