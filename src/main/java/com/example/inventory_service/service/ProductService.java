package com.example.inventory_service.service;

import com.example.inventory_service.dao.ProductDaoService;
import com.example.inventory_service.dao.StockManagementDaoService;
import com.example.inventory_service.dto.ProductDto;
import com.example.inventory_service.entity.Product;
import com.example.inventory_service.entity.StockManagement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    ProductDaoService productDaoService;
    StockManagementDaoService stockManagementDaoService;

    @Autowired
    public ProductService(ProductDaoService productDaoService, StockManagementDaoService stockManagementDaoService) {
        this.productDaoService = productDaoService;
        this.stockManagementDaoService = stockManagementDaoService;
    }

    public List<ProductDto> getAll() {
        return toDto(productDaoService.getAll());
    }

    public ProductDto getById(long id) {
        Optional<Product> productOptional = productDaoService.getById(id);
        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return toDto(productOptional.get());
    }

    public ProductDto add(ProductDto productDto) {
        Product product = toEntity(productDto);
        Product added = productDaoService.add(product);

        StockManagement stockManagement = new StockManagement();
        stockManagement.setId(added.getId());
        stockManagement.setProduct(added);
        stockManagement.setQuantity(0);
        stockManagement.setMaxStockLevel(100);
        stockManagement.setReorderLevel(10);
        stockManagement.setUpdatedAt(LocalDateTime.now());

        stockManagementDaoService.add(stockManagement);

        return toDto(added);
    }

    public ProductDto update(ProductDto productDto, long id) {
        Optional<Product> productOptional = productDaoService.getById(id);
        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Product product = productOptional.get();
        product.setId(id);
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());

        return toDto(productDaoService.update(product));
    }

    public boolean delete(long id) {
        return productDaoService.delete(id);
    }

    public Product toEntity(ProductDto productDto) {
        Product product = new Product();

        product.setName(productDto.getName());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        return product;
    }

    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();

        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setCategory(product.getCategory());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());

        return productDto;
    }

    private List<ProductDto> toDto(List<Product> products) {
        return products.stream().map(this::toDto).collect(Collectors.toList());
    }

    private List<Product> toEntity(List<ProductDto> productDtos) {
        return productDtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

}
