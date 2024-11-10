package com.example.inventory_service.dao;

import com.example.inventory_service.entity.StockManagement;
import com.example.inventory_service.repository.StockManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class StockManagementDaoService {

    StockManagementRepository stockManagementRepository;

    @Autowired
    public StockManagementDaoService(StockManagementRepository stockManagementRepository) {
        this.stockManagementRepository = stockManagementRepository;
    }

    public Optional<StockManagement> getById(long id) {
        return stockManagementRepository.findById(id);
    }

    public StockManagement add(StockManagement stockManagement){
        return stockManagementRepository.save(stockManagement);
    }

    public StockManagement update(StockManagement stockManagement){
        return stockManagementRepository.save(stockManagement);
    }
}
