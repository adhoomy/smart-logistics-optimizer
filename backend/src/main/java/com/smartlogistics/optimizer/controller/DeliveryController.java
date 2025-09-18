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
    private final Map<Long, Delivery> store = new ConcurrentHashMap<>();

    @GetMapping
    public Collection<Delivery> list() {
        return store.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> get(@PathVariable Long id) {
        Delivery item = store.get(id);
        return item == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<Delivery> create(@RequestBody @Valid Delivery item) {
        store.put(item.getId(), item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Delivery> update(@PathVariable Long id, @RequestBody @Valid Delivery payload) {
        Delivery existing = store.get(id);
        if (existing == null) return ResponseEntity.notFound().build();
        existing.setVehicleId(payload.getVehicleId());
        existing.setRoute(payload.getRoute());
        existing.setEstimatedTime(payload.getEstimatedTime());
        existing.setStatus(payload.getStatus());
        existing.setOrder(payload.getOrder());
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return store.remove(id) == null ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }
}




