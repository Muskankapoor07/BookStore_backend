package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.UserLoginRequest;
import com.bookstore.bookstore.dto.UserRegistrationRequest;
import com.bookstore.bookstore.dto.UserResponse;

public interface UserService {

    UserResponse register(UserRegistrationRequest request);

    UserResponse login(UserLoginRequest request);
}