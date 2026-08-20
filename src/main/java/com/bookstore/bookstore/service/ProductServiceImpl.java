package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.ProductRequest;
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
    public ProductResponse addBook(ProductRequest request) {

        Product product = productMapper.toEntity(request);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse updateBook(
            Long productId,
            ProductRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setAuthor(request.getAuthor());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());

        Product updatedProduct =
                productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }
}