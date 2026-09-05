package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.ProductRequest;
import com.bookstore.bookstore.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse addProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    ProductResponse getProductById(Long id);

    void deleteProduct(Long id);

    Page<ProductResponse> searchProducts(String keyword, Pageable pageable);
}