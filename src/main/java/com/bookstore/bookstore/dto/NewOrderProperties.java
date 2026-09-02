package com.bookstore.bookstore.dto;

import lombok.Data;

@Data
public class NewOrderProperties {

    private String product_id;
    private String product_name;
    private Integer product_quantity;
    private Double product_price;
}