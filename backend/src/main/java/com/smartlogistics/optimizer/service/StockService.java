package com.smartlogistics.optimizer.service;

import com.smartlogistics.optimizer.controller.dto.StockDtos;
import com.smartlogistics.optimizer.exception.NotFoundException;
import com.smartlogistics.optimizer.mapper.StockMapper;
import com.smartlogistics.optimizer.model.StockItem;
import com.smartlogistics.optimizer.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    public StockService(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    public StockDtos.Response create(StockDtos.CreateRequest req) {
        StockItem saved = stockRepository.save(stockMapper.fromCreate(req));
        return stockMapper.toResponse(saved);
    }

    public StockDtos.Response get(Long id) {
        StockItem s = stockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock %d not found".formatted(id)));
        return stockMapper.toResponse(s);
    }

    public List<StockDtos.Response> list() {
        return stockRepository.findAll().stream().map(stockMapper::toResponse).toList();
    }

    public StockDtos.Response update(Long id, StockDtos.UpdateRequest req) {
        StockItem s = stockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock %d not found".formatted(id)));
        StockItem saved = stockRepository.save(stockMapper.update(s, req));
        return stockMapper.toResponse(saved);
    }

    public void delete(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new NotFoundException("Stock %d not found".formatted(id));
        }
        stockRepository.deleteById(id);
    }
}


