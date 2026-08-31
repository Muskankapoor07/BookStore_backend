package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.ProductRequest;
import com.bookstore.bookstore.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse addProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    void deleteProduct(Long id);

    List<ProductResponse> searchProducts(String keyword);
}