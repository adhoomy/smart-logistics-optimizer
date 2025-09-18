package com.smartlogistics.optimizer.controller;

import com.smartlogistics.optimizer.controller.dto.OrderDtos;
import com.smartlogistics.optimizer.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * POST /orders - Create a new order
     * Example request:
     * {
     *   "customerName": "Acme Corp",
     *   "destinationAddress": "123 Market St, SF",
     *   "quantity": 10,
     *   "deliveryDate": "2025-12-01T10:00:00",
     *   "status": "PENDING"
     * }
     * Example response (201):
     * {
     *   "id": 1,
     *   "customerName": "Acme Corp",
     *   "destinationAddress": "123 Market St, SF",
     *   "quantity": 10,
     *   "deliveryDate": "2025-12-01T10:00:00",
     *   "status": "PENDING",
     *   "createdAt": "2025-09-18T12:00:00"
     * }
     */
    @PostMapping
    public ResponseEntity<OrderDtos.Response> create(@RequestBody @Valid OrderDtos.CreateRequest req) {
        var res = orderService.create(req);
        return ResponseEntity.created(URI.create("/orders/" + res.id()))
                .body(res);
    }

    /** GET /orders/{id} */
    @GetMapping("/{id}")
    public OrderDtos.Response get(@PathVariable("id") Long id) { return orderService.get(id); }

    /** GET /orders */
    @GetMapping
    public List<OrderDtos.Response> list() { return orderService.list(); }

    /** PUT /orders/{id} */
    @PutMapping("/{id}")
    public OrderDtos.Response update(@PathVariable("id") Long id, @RequestBody @Valid OrderDtos.UpdateRequest req) { return orderService.update(id, req); }

    /** DELETE /orders/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) { orderService.delete(id); return ResponseEntity.noContent().build(); }
}




