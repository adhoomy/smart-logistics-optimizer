package com.smartlogistics.optimizer.controller;

import com.smartlogistics.optimizer.model.Delivery;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {
    private final Map<String, Delivery> store = new ConcurrentHashMap<>();

    @GetMapping
    public Collection<Delivery> list() {
        return store.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> get(@PathVariable String id) {
        Delivery item = store.get(id);
        return item == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<Delivery> create(@RequestBody @Valid Delivery item) {
        store.put(item.getId(), item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Delivery> update(@PathVariable String id, @RequestBody @Valid Delivery payload) {
        Delivery existing = store.get(id);
        if (existing == null) return ResponseEntity.notFound().build();
        existing.setOrderId(payload.getOrderId());
        existing.setStatus(payload.getStatus());
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return store.remove(id) == null ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }
}




