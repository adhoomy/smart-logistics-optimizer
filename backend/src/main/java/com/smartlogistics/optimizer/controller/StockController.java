package com.smartlogistics.optimizer.controller;

import com.smartlogistics.optimizer.controller.dto.StockDtos;
import com.smartlogistics.optimizer.controller.dto.PaginationDtos;
import com.smartlogistics.optimizer.service.StockService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) { this.stockService = stockService; }

    /** POST /stock */
    @PostMapping
    public ResponseEntity<StockDtos.Response> create(@RequestBody @Valid StockDtos.CreateRequest req) {
        var res = stockService.create(req);
        return ResponseEntity.created(URI.create("/stock/" + res.id())).body(res);
    }

    /** GET /stock/{id} */
    @GetMapping("/{id}")
    public StockDtos.Response get(@PathVariable("id") Long id) { return stockService.get(id); }

    /**
     * GET /stock - List stock items with pagination, sorting, and filtering
     * 
     * Query Parameters:
     * - page: Page number (default: 0)
     * - size: Page size (default: 10)
     * - sort: Sort field and direction (e.g., "quantity,asc", "updatedAt,desc")
     * - warehouseId: Filter by warehouse ID (exact match)
     * - productName: Filter by product name (partial match, case-insensitive)
     * 
     * Example requests:
     * - GET /stock?page=0&size=5&sort=quantity,asc
     * - GET /stock?warehouseId=W1&productName=Widget
     * - GET /stock?page=1&size=20&sort=updatedAt,desc&warehouseId=W2
     * 
     * Example response (200):
     * {
     *   "content": [
     *     {
     *       "id": 1,
     *       "warehouseId": "W1",
     *       "productName": "Widget",
     *       "quantity": 100,
     *       "sku": "SKU-001",
     *       "location": "A1",
     *       "updatedAt": "2025-09-18T12:00:00"
     *     }
     *   ],
     *   "page": 0,
     *   "size": 10,
     *   "totalElements": 50,
     *   "totalPages": 5,
     *   "first": true,
     *   "last": false,
     *   "hasNext": true,
     *   "hasPrevious": false
     * }
     */
    @GetMapping
    public PaginationDtos.PageResponse<StockDtos.Response> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) String warehouseId,
            @RequestParam(required = false) String productName) {
        
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1]) 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return stockService.list(pageable, warehouseId, productName);
    }

    /** PUT /stock/{id} */
    @PutMapping("/{id}")
    public StockDtos.Response update(@PathVariable("id") Long id, @RequestBody @Valid StockDtos.UpdateRequest req) { return stockService.update(id, req); }

    /** DELETE /stock/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) { stockService.delete(id); return ResponseEntity.noContent().build(); }
}




