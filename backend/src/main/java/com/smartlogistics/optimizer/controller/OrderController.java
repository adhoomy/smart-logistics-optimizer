package com.smartlogistics.optimizer.controller;

import com.smartlogistics.optimizer.controller.dto.OrderDtos;
import com.smartlogistics.optimizer.controller.dto.PaginationDtos;
import com.smartlogistics.optimizer.model.Order;
import com.smartlogistics.optimizer.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    /**
     * GET /orders - List orders with pagination, sorting, and filtering
     * 
     * Query Parameters:
     * - page: Page number (default: 0)
     * - size: Page size (default: 10)
     * - sort: Sort field and direction (e.g., "status,asc", "deliveryDate,desc")
     * - status: Filter by order status (PENDING, CONFIRMED, IN_PROGRESS, DELIVERED, CANCELLED)
     * - customerName: Filter by customer name (partial match, case-insensitive)
     * 
     * Example requests:
     * - GET /orders?page=0&size=5&sort=status,asc
     * - GET /orders?status=SHIPPED&customerName=Acme
     * - GET /orders?page=1&size=20&sort=deliveryDate,desc&status=PENDING
     * 
     * Example response (200):
     * {
     *   "content": [
     *     {
     *       "id": 1,
     *       "customerName": "Acme Corp",
     *       "destinationAddress": "123 Market St, SF",
     *       "quantity": 10,
     *       "deliveryDate": "2025-12-01T10:00:00",
     *       "status": "PENDING",
     *       "createdAt": "2025-09-18T12:00:00"
     *     }
     *   ],
     *   "page": 0,
     *   "size": 10,
     *   "totalElements": 25,
     *   "totalPages": 3,
     *   "first": true,
     *   "last": false,
     *   "hasNext": true,
     *   "hasPrevious": false
     * }
     */
    @GetMapping
    public PaginationDtos.PageResponse<OrderDtos.Response> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) Order.OrderStatus status,
            @RequestParam(required = false) String customerName) {
        
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1]) 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return orderService.list(pageable, status, customerName);
    }

    /** PUT /orders/{id} */
    @PutMapping("/{id}")
    public OrderDtos.Response update(@PathVariable("id") Long id, @RequestBody @Valid OrderDtos.UpdateRequest req) { return orderService.update(id, req); }

    /** DELETE /orders/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) { orderService.delete(id); return ResponseEntity.noContent().build(); }
}




