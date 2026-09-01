package com.bookstore.bookstore.dto;

import lombok.Data;

@Data
public class ProductRequest {

    private String name;

    private String author;

    private Double price;

    private Double discountPrice;

    private Integer quantity;

    private String description;
}