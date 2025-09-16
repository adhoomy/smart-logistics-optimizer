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
    private final Map<String, StockItem> store = new ConcurrentHashMap<>();

    @GetMapping
    public Collection<StockItem> list() {
        return store.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockItem> get(@PathVariable String id) {
        StockItem item = store.get(id);
        return item == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<StockItem> create(@RequestBody @Valid StockItem item) {
        store.put(item.getId(), item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockItem> update(@PathVariable String id, @RequestBody @Valid StockItem payload) {
        StockItem existing = store.get(id);
        if (existing == null) return ResponseEntity.notFound().build();
        existing.setSku(payload.getSku());
        existing.setLocation(payload.getLocation());
        existing.setQuantity(payload.getQuantity());
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return store.remove(id) == null ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }
}




