package com.bookstore.bookstore.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {

    private String firstName;
    private String lastName;
    private String email;
}