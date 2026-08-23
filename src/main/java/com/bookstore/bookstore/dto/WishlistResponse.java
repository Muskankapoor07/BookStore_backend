package com.bookstore.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WishlistResponse {

    private Long productId;
    private String productName;
    private Double price;
}