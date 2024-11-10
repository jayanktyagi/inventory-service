package com.example.inventory_service.dao;

import com.example.inventory_service.entity.Product;
import com.example.inventory_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDaoService {
    ProductRepository productRepository;

    @Autowired
    public ProductDaoService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product add(Product product) {
        return productRepository.save(product);
    }

    public Product update(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getById(long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public boolean delete(long id) {
        productRepository.deleteById(id);
        return true;
    }


}
