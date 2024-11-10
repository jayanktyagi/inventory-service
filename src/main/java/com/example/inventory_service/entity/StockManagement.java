package com.example.inventory_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class StockManagement {

    @Id
    private Long id;

    private Integer quantity;

    private LocalDateTime updatedAt;

    private Integer reorderLevel;

    private Integer maxStockLevel;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Product product;

    public StockManagement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Integer getMaxStockLevel() {
        return maxStockLevel;
    }

    public void setMaxStockLevel(Integer maxStockLevel) {
        this.maxStockLevel = maxStockLevel;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
