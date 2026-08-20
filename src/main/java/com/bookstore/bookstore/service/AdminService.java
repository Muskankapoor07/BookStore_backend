package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.AdminLoginRequest;
import com.bookstore.bookstore.dto.AdminRegistrationRequest;
import com.bookstore.bookstore.dto.AuthResponse;
import com.bookstore.bookstore.dto.UserResponse;

public interface AdminService {

    UserResponse registerAdmin(AdminRegistrationRequest request);

    AuthResponse loginAdmin(AdminLoginRequest request);
}