package com.smartlogistics.optimizer.controller;

import com.smartlogistics.optimizer.model.StockItem;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/stock")
public class StockController {
    private final Map<Long, StockItem> store = new ConcurrentHashMap<>();

    @GetMapping
    public Collection<StockItem> list() {
        return store.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockItem> get(@PathVariable Long id) {
        StockItem item = store.get(id);
        return item == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<StockItem> create(@RequestBody @Valid StockItem item) {
        store.put(item.getId(), item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockItem> update(@PathVariable Long id, @RequestBody @Valid StockItem payload) {
        StockItem existing = store.get(id);
        if (existing == null) return ResponseEntity.notFound().build();
        existing.setWarehouseId(payload.getWarehouseId());
        existing.setProductName(payload.getProductName());
        existing.setQuantity(payload.getQuantity());
        existing.setSku(payload.getSku());
        existing.setLocation(payload.getLocation());
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return store.remove(id) == null ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }
}




