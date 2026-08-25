package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.AuthResponse;
import com.bookstore.bookstore.dto.ForgotPasswordRequest;
import com.bookstore.bookstore.dto.ResetPasswordRequest;
import com.bookstore.bookstore.dto.UserLoginRequest;
import com.bookstore.bookstore.dto.UserRegistrationRequest;
import com.bookstore.bookstore.dto.UserResponse;
import com.bookstore.bookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookstore_user")
@Tag(name = "User", description = "APIs for user in the system")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ================= REGISTER =================

    @PostMapping("/register")
    @Operation(
            summary = "Register user",
            description = "Create a new customer account"
    )
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody UserRegistrationRequest request) {

        UserResponse response =
                userService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ================= VERIFY USER =================

    @PostMapping("/verification/{token}")
    @Operation(
            summary = "Verify user",
            description = "Verify user account using verification token"
    )
    public ResponseEntity<String> verifyUser(
            @PathVariable String token) {

        userService.verifyUser(token);

        return ResponseEntity.ok(
                "User verified successfully"
        );
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Login user and generate JWT token"
    )
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody UserLoginRequest request) {

        AuthResponse response =
                userService.login(request);

        return ResponseEntity.ok(response);
    }

    // ================= FORGOT PASSWORD =================

    @PostMapping("/forgot-password")
    @Operation(
            summary = "Forgot password",
            description = "Send password reset token to registered email"
    )
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        userService.forgotPassword(request);

        return ResponseEntity.ok(
                "Password reset token has been sent to your email"
        );
    }

    // ================= RESET PASSWORD =================

    @PostMapping("/reset-password")
    @Operation(
            summary = "Reset password",
            description = "Reset password using reset token"
    )
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        userService.resetPassword(request);

        return ResponseEntity.ok(
                "Password reset successfully"
        );
    }
}