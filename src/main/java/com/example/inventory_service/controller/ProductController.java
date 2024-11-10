package com.example.inventory_service.controller;

import com.example.inventory_service.dto.ProductDto;
import com.example.inventory_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productService/v1/products")
public class ProductController {

    ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductDto add(@RequestBody ProductDto productDto) {
        return productService.add(productDto);
    }

    @GetMapping("/{id}")
    public ProductDto getbyId(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping
    public List<ProductDto> get() {
        return productService.getAll();
    }

    @PutMapping("/{id}")
    public ProductDto update(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.update(productDto, id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(Long id) {
        return productService.delete(id);
    }


}
