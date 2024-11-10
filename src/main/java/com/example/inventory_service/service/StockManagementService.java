package com.example.inventory_service.service;

import com.example.inventory_service.dao.StockManagementDaoService;
import com.example.inventory_service.dto.StockManagementDto;
import com.example.inventory_service.entity.StockManagement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockManagementService {

    StockManagementDaoService stockManagementDaoService;
    ProductService productService;

    @Autowired
    public StockManagementService(StockManagementDaoService stockManagementDaoService, ProductService productService) {
        this.stockManagementDaoService = stockManagementDaoService;
        this.productService = productService;
    }

    public StockManagementDto increaseStock(Long productId, int increase) {
        Optional<StockManagement> stockManagementOptional = stockManagementDaoService.getById(productId);
        if (stockManagementOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        StockManagement stockManagement = stockManagementOptional.get();
        if (stockManagement.getMaxStockLevel() == null || stockManagement.getQuantity() + increase < stockManagement.getMaxStockLevel()) {
            stockManagement.setQuantity(stockManagement.getQuantity() + increase);
        } else {
            throw new RuntimeException("Stock level can't exceed the maximum quantity");
        }
        return toDto(stockManagementDaoService.update(stockManagement));
    }

    public StockManagementDto decreaseStock(Long productId, int decrease) {
        Optional<StockManagement> stockManagementOptional = stockManagementDaoService.getById(productId);
        if (stockManagementOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        StockManagement stockManagement = stockManagementOptional.get();
        if (stockManagement.getQuantity() - decrease < 0) {
            throw new RuntimeException("Stock level can't be " + (stockManagement.getQuantity() - decrease));
        } else
            stockManagement.setQuantity(stockManagement.getQuantity() - decrease);
        return toDto(stockManagementDaoService.update(stockManagement));
    }

    public StockManagementDto setMaxQuantity(Long productId, int level){
        Optional<StockManagement> stockManagementOptional = stockManagementDaoService.getById(productId);
        if (stockManagementOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        StockManagement stockManagement = stockManagementOptional.get();
        stockManagement.setMaxStockLevel(level);
        return toDto(stockManagementDaoService.update(stockManagement));
    }

    public StockManagementDto setReorderLevel(Long productId, int level){
        Optional<StockManagement> stockManagementOptional = stockManagementDaoService.getById(productId);
        if (stockManagementOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        StockManagement stockManagement = stockManagementOptional.get();
        stockManagement.setReorderLevel(level);
        return toDto(stockManagementDaoService.update(stockManagement));
    }

    public StockManagementDto getStockByProductId(Long productId) {
        Optional<StockManagement> stockManagementOptional = stockManagementDaoService.getById(productId);
        if (stockManagementOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return toDto(stockManagementOptional.get());
    }

    public boolean checkStockAvailability(Long productId, int quantity) {
        Optional<StockManagement> stockManagementOptional = stockManagementDaoService.getById(productId);
        if (stockManagementOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        StockManagement stockManagement = stockManagementOptional.get();
        return stockManagement.getQuantity() > quantity;
    }

    private StockManagementDto toDto(StockManagement stockManagement) {
        StockManagementDto stockManagementDto = new StockManagementDto();
        stockManagementDto.setId(stockManagement.getId());
        stockManagementDto.setProduct(productService.toDto(stockManagement.getProduct()));
        stockManagementDto.setMaxStockLevel(stockManagement.getMaxStockLevel());
        stockManagementDto.setQuantity(stockManagement.getQuantity());
        stockManagementDto.setReorderLevel(stockManagementDto.getReorderLevel());
        stockManagementDto.setUpdatedAt(stockManagement.getUpdatedAt());
        return stockManagementDto;
    }

    private StockManagement toEntity(StockManagementDto stockManagementDto) {
        StockManagement stockManagement = new StockManagement();
        stockManagement.setId(stockManagementDto.getId());
        stockManagement.setProduct(productService.toEntity(stockManagementDto.getProduct()));
        stockManagement.setQuantity(stockManagementDto.getQuantity());
        stockManagement.setMaxStockLevel(stockManagementDto.getMaxStockLevel());
        stockManagement.setReorderLevel(stockManagementDto.getReorderLevel());
        stockManagement.setUpdatedAt(stockManagementDto.getUpdatedAt());
        return stockManagement;
    }
}
