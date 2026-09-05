package com.bookstore.bookstore.dto;

import com.bookstore.bookstore.enums.StockStatus;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String author;
    private Double price;
    private Double discountPrice;
    private Integer quantity;
    private String description;
    private String imageUrl;
    private StockStatus stockStatus;
}