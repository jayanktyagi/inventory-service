package com.example.inventory_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.inventory_service.dao.StockManagementDaoService;
import com.example.inventory_service.dto.ProductDto;
import com.example.inventory_service.dto.StockManagementDto;
import com.example.inventory_service.entity.Product;
import com.example.inventory_service.entity.StockManagement;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class StockManagementServiceTest {

    @Mock
    StockManagementDaoService stockManagementDaoService;

    @Mock
    ProductService productService;

    @InjectMocks
    private StockManagementService stockManagementService;

    private StockManagement createStock(int quantity, Integer max) {
        StockManagement sm = new StockManagement();
        sm.setId(1L);
        sm.setQuantity(quantity);
        sm.setMaxStockLevel(max);
        sm.setReorderLevel(10);
        sm.setUpdatedAt(LocalDateTime.now());

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        sm.setProduct(product);

        return sm;
    }

    private ProductDto createProductDto() {
        ProductDto dto = new ProductDto();
        dto.setId(1L);
        dto.setName("Laptop");
        return dto;
    }

    @Test
    void increaseStock_whenWithinMax_shouldIncreaseQuantity() {
        StockManagement stock = createStock(10, 50);

        when(stockManagementDaoService.getById(1L))
                .thenReturn(Optional.of(stock));
        when(stockManagementDaoService.update(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(productService.toDto(any(Product.class)))
                .thenReturn(createProductDto());

        StockManagementDto result = stockManagementService.increaseStock(1L, 5);

        assertEquals(15, result.getQuantity());
    }

    @Test
    void increaseStock_whenExceedsMax_shouldThrowException() {
        StockManagement stock = createStock(45, 50);

        when(stockManagementDaoService.getById(1L))
                .thenReturn(Optional.of(stock));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> stockManagementService.increaseStock(1L, 10));

        assertEquals("Stock level can't exceed the maximum quantity",
                ex.getMessage());
    }

    @Test
    void increaseStock_whenStockNotFound_shouldThrowEntityNotFound() {
        when(stockManagementDaoService.getById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> stockManagementService.increaseStock(1L, 5));
    }

    @Test
    void decreaseStock_whenEnoughQuantity_shouldDecrease() {
        StockManagement stock = createStock(20, 50);

        when(stockManagementDaoService.getById(1L))
                .thenReturn(Optional.of(stock));
        when(stockManagementDaoService.update(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(productService.toDto(any(Product.class)))
                .thenReturn(createProductDto());

        StockManagementDto result = stockManagementService.decreaseStock(1L, 5);

        assertEquals(15, result.getQuantity());
    }

    @Test
    void decreaseStock_whenNegative_shouldThrowException() {
        StockManagement stock = createStock(3, 50);

        when(stockManagementDaoService.getById(1L))
                .thenReturn(Optional.of(stock));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> stockManagementService.decreaseStock(1L, 5));

        assertTrue(ex.getMessage().contains("Stock level can't be"));
    }

    @Test
    void setMaxQuantity_shouldUpdateMaxStockLevel() {
        StockManagement stock = createStock(10, 50);

        when(stockManagementDaoService.getById(1L))
                .thenReturn(Optional.of(stock));
        when(stockManagementDaoService.update(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(productService.toDto(any(Product.class)))
                .thenReturn(createProductDto());

        StockManagementDto result = stockManagementService.setMaxQuantity(1L, 200);

        assertEquals(200, result.getMaxStockLevel());
    }

    @Test
    void setReorderLevel_shouldUpdateReorderLevel() {
        StockManagement stock = createStock(10, 50);

        when(stockManagementDaoService.getById(1L))
                .thenReturn(Optional.of(stock));
        when(stockManagementDaoService.update(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(productService.toDto(any(Product.class)))
                .thenReturn(createProductDto());

        StockManagementDto result = stockManagementService.setReorderLevel(1L, 5);

        assertEquals(5, result.getReorderLevel());
    }

    @Test
    void getStockByProductId_shouldReturnDto() {
        StockManagement stock = createStock(10, 50);

        when(stockManagementDaoService.getById(1L))
                .thenReturn(Optional.of(stock));
        when(productService.toDto(any(Product.class)))
                .thenReturn(createProductDto());

        StockManagementDto result = stockManagementService.getStockByProductId(1L);

        assertEquals(10, result.getQuantity());
    }

    @Test
    void checkStockAvailability_whenEnoughStock_shouldReturnTrue() {
        StockManagement stock = createStock(20, 50);

        when(stockManagementDaoService.getById(1L))
                .thenReturn(Optional.of(stock));

        assertTrue(stockManagementService
                .checkStockAvailability(1L, 10));
    }

}
