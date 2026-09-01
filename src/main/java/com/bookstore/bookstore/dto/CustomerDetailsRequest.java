package com.bookstore.bookstore.dto;

import lombok.Data;

@Data
public class CustomerDetailsRequest {

    private String addressType;

    private String fullAddress;

    private String city;

    private String state;
}