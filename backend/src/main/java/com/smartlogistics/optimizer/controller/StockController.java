package com.smartlogistics.optimizer.controller;

import com.smartlogistics.optimizer.controller.dto.StockDtos;
import com.smartlogistics.optimizer.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    /** GET /stock */
    @GetMapping
    public List<StockDtos.Response> list() { return stockService.list(); }

    /** PUT /stock/{id} */
    @PutMapping("/{id}")
    public StockDtos.Response update(@PathVariable("id") Long id, @RequestBody @Valid StockDtos.UpdateRequest req) { return stockService.update(id, req); }

    /** DELETE /stock/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) { stockService.delete(id); return ResponseEntity.noContent().build(); }
}




