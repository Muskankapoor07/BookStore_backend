package com.bookstore.bookstore.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String author;
    private Double price;
    private Integer quantity;
    private String description;
}