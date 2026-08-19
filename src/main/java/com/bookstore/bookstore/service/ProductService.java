package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();
}