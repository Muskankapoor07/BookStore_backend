package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.AdminLoginRequest;
import com.bookstore.bookstore.dto.AdminRegistrationRequest;
import com.bookstore.bookstore.dto.AuthResponse;

public interface AdminService {

    AuthResponse registerAdmin(AdminRegistrationRequest request);

    AuthResponse loginAdmin(AdminLoginRequest request);
}