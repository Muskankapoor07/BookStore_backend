package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.AuthResponse;
import com.bookstore.bookstore.dto.UserLoginRequest;
import com.bookstore.bookstore.dto.UserRegistrationRequest;
import com.bookstore.bookstore.dto.UserResponse;

public interface UserService {

    UserResponse register(UserRegistrationRequest request);

    AuthResponse login(UserLoginRequest request);

    void verifyUser(String token);
}