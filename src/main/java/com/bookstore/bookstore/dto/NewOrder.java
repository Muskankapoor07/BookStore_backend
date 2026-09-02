package com.bookstore.bookstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class NewOrder {

    private List<NewOrderProperties> orders;
}