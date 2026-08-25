package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.AuthResponse;
import com.bookstore.bookstore.dto.ForgotPasswordRequest;
import com.bookstore.bookstore.dto.ResetPasswordRequest;
import com.bookstore.bookstore.dto.UserLoginRequest;
import com.bookstore.bookstore.dto.UserRegistrationRequest;
import com.bookstore.bookstore.dto.UserResponse;
import com.bookstore.bookstore.dto.UserUpdateRequest;

public interface UserService {

    UserResponse register(UserRegistrationRequest request);

    AuthResponse login(UserLoginRequest request);

    void verifyUser(String token);

    UserResponse updateUser(UserUpdateRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}