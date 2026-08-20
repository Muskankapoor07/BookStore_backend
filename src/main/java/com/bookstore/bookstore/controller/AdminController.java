package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.AdminLoginRequest;
import com.bookstore.bookstore.dto.AdminRegistrationRequest;
import com.bookstore.bookstore.dto.AuthResponse;
import com.bookstore.bookstore.dto.UserResponse;
import com.bookstore.bookstore.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookstore_user/admin")
@Tag(name = "Admin", description = "APIs for admin authentication")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserResponse> registerAdmin(
            @Valid @RequestBody AdminRegistrationRequest request) {

        UserResponse response =
                adminService.registerAdmin(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginAdmin(
            @Valid @RequestBody AdminLoginRequest request) {

        AuthResponse response =
                adminService.loginAdmin(request);

        return ResponseEntity.ok(response);
    }
}