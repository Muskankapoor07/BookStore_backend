package com.bookstore.bookstore.mapper;

import com.bookstore.bookstore.dto.ProductResponse;
import com.bookstore.bookstore.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .author(product.getAuthor())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .description(product.getDescription())
                .build();
    }
}