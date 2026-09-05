package com.bookstore.bookstore.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    private Long id;

    private Long productId;

    private String productName;

    private Double price;

    private Double discountPrice;

    private Integer quantity;

    private Double totalPrice;
}