package com.example.inventory_service.repository;

import com.example.inventory_service.entity.StockManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockManagementRepository extends JpaRepository<StockManagement, Long> {
}
