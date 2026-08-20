package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.ProductRequest;
import com.bookstore.bookstore.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse addBook(ProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse updateBook(Long productId, ProductRequest request);
}