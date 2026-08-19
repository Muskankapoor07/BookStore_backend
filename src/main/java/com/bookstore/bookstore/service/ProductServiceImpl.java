package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.ProductResponse;
import com.bookstore.bookstore.mapper.ProductMapper;
import com.bookstore.bookstore.model.Product;
import com.bookstore.bookstore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductMapper productMapper) {

        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }
}