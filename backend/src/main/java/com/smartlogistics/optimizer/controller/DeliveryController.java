package com.smartlogistics.optimizer.controller;

import com.smartlogistics.optimizer.controller.dto.DeliveryDtos;
import com.smartlogistics.optimizer.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) { this.deliveryService = deliveryService; }

    /** POST /deliveries */
    @PostMapping
    public ResponseEntity<DeliveryDtos.Response> create(@RequestBody @Valid DeliveryDtos.CreateRequest req) {
        var res = deliveryService.create(req);
        return ResponseEntity.created(URI.create("/deliveries/" + res.id())).body(res);
    }

    /** GET /deliveries/{id} */
    @GetMapping("/{id}")
    public DeliveryDtos.Response get(@PathVariable("id") Long id) { return deliveryService.get(id); }

    /** GET /deliveries */
    @GetMapping
    public List<DeliveryDtos.Response> list() { return deliveryService.list(); }

    /** PUT /deliveries/{id} */
    @PutMapping("/{id}")
    public DeliveryDtos.Response update(@PathVariable("id") Long id, @RequestBody @Valid DeliveryDtos.UpdateRequest req) {
        return deliveryService.update(id, req);
    }

    /** DELETE /deliveries/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}




