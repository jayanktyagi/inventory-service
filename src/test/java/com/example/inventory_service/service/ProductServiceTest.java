package com.example.inventory_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.inventory_service.dao.ProductDaoService;
import com.example.inventory_service.dao.StockManagementDaoService;
import com.example.inventory_service.dto.ProductDto;
import com.example.inventory_service.entity.Product;
import com.example.inventory_service.entity.StockManagement;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDaoService productDaoService;

    @Mock
    private StockManagementDaoService stockManagementDaoService;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAll_shouldReturnListOfProductDtos() {
        Product product = createProduct(1L);
        when(productDaoService.getAll()).thenReturn(List.of(product));

        List<ProductDto> result = productService.getAll();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());

        verify(productDaoService).getAll();
    }

    @Test
    void getById_whenProductExists_shouldReturnDto() {
        Product product = createProduct(1L);
        when(productDaoService.getById(1L)).thenReturn(Optional.of(product));

        ProductDto result = productService.getById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());

        verify(productDaoService).getById(1L);
    }

    @Test
    void getById_whenProductNotFound_shouldThrowException() {
        when(productDaoService.getById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> productService.getById(1L));

        verify(productDaoService).getById(1L);
    }

    @Test
    void add_shouldSaveProductAndCreateStockEntry() {
        ProductDto inputDto = createProductDto();
        Product savedProduct = createProduct(1L);

        when(productDaoService.add(any(Product.class))).thenReturn(savedProduct);

        ProductDto result = productService.add(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(productDaoService).add(any(Product.class));
        verify(stockManagementDaoService).add(any(StockManagement.class));
    }

    @Test
    void update_whenProductExists_shouldUpdateAndReturnDto() {
        Product existing = createProduct(1L);
        ProductDto updateDto = createProductDto();
        updateDto.setName("Updated Laptop");

        when(productDaoService.getById(1L)).thenReturn(Optional.of(existing));
        when(productDaoService.update(any(Product.class))).thenReturn(existing);

        ProductDto result = productService.update(updateDto, 1L);

        assertEquals("Updated Laptop", result.getName());

        verify(productDaoService).getById(1L);
        verify(productDaoService).update(any(Product.class));
    }

    @Test
    void update_whenProductNotFound_shouldThrowException() {
        ProductDto dto = createProductDto();
        when(productDaoService.getById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> productService.update(dto, 1L));

        verify(productDaoService).getById(1L);
        verify(productDaoService, never()).update(any());
    }

    private Product createProduct(Long id) {
        Product p = new Product();
        p.setId(id);
        p.setName("Laptop");
        p.setDescription("Gaming Laptop");
        p.setPrice(new BigDecimal("75000"));
        p.setCategory("Electronics");
        return p;
    }

    @Test
    void delete_shouldDelegateToDao() {
        when(productDaoService.delete(1L)).thenReturn(true);

        boolean result = productService.delete(1L);

        assertTrue(result);
        verify(productDaoService).delete(1L);
    }

    private ProductDto createProductDto() {
        ProductDto dto = new ProductDto();
        dto.setName("Laptop");
        dto.setDescription("Gaming Laptop");
        dto.setPrice(new BigDecimal("75000"));
        dto.setCategory("Electronics");
        return dto;
    }

}
