package com.smartlogistics.optimizer.controller;

import com.smartlogistics.optimizer.controller.dto.DeliveryDtos;
import com.smartlogistics.optimizer.controller.dto.PaginationDtos;
import com.smartlogistics.optimizer.model.Delivery;
import com.smartlogistics.optimizer.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    /**
     * GET /deliveries - List deliveries with pagination, sorting, and filtering
     * 
     * Query Parameters:
     * - page: Page number (default: 0)
     * - size: Page size (default: 10)
     * - sort: Sort field and direction (e.g., "status,asc", "estimatedTime,desc")
     * - status: Filter by delivery status (PENDING, IN_TRANSIT, DELIVERED, FAILED, CANCELLED)
     * - vehicleId: Filter by vehicle ID (exact match)
     * 
     * Example requests:
     * - GET /deliveries?page=0&size=5&sort=status,asc
     * - GET /deliveries?status=IN_TRANSIT&vehicleId=TRUCK-42
     * - GET /deliveries?page=1&size=20&sort=estimatedTime,desc&status=PENDING
     * 
     * Example response (200):
     * {
     *   "content": [
     *     {
     *       "id": 1,
     *       "vehicleId": "TRUCK-42",
     *       "route": "A-B-C",
     *       "estimatedTime": "2025-12-01T15:00:00",
     *       "status": "PENDING",
     *       "orderId": 1,
     *       "createdAt": "2025-09-18T12:00:00"
     *     }
     *   ],
     *   "page": 0,
     *   "size": 10,
     *   "totalElements": 15,
     *   "totalPages": 2,
     *   "first": true,
     *   "last": false,
     *   "hasNext": true,
     *   "hasPrevious": false
     * }
     */
    @GetMapping
    public PaginationDtos.PageResponse<DeliveryDtos.Response> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) Delivery.DeliveryStatus status,
            @RequestParam(required = false) String vehicleId) {
        
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1]) 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return deliveryService.list(pageable, status, vehicleId);
    }

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




