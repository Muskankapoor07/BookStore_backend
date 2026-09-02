package com.bookstore.bookstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class MultipleCartItemRequest {

    private List<MultipleCartItem> items;
}