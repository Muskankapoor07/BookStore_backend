package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.CustomerDetailsRequest;
import com.bookstore.bookstore.dto.UserResponse;
import com.bookstore.bookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookstore_user")
@Tag(
        name = "Customer Details",
        description = "APIs for managing customer profile details"
)
@SecurityRequirement(name = "bearerAuth")
public class CustomerDetailsController {

    private final UserService userService;

    public CustomerDetailsController(UserService userService) {
        this.userService = userService;
    }

    // ================= GET PROFILE =================

    @GetMapping("/profile")
    @Operation(
            summary = "Get user profile",
            description = "Get details of the currently logged-in user"
    )
    public ResponseEntity<UserResponse> getProfile(
            Authentication authentication) {

        UserResponse response =
                userService.getProfile(authentication.getName());

        return ResponseEntity.ok(response);
    }

    // ================= UPDATE CUSTOMER DETAILS =================

    @PutMapping("/edit_user")
    @Operation(
            summary = "Update customer details",
            description = "Update the customer details to place order"
    )
    public ResponseEntity<UserResponse> updateUser(
            @RequestBody CustomerDetailsRequest request) {

        UserResponse response =
                userService.updateCustomerDetails(request);

        return ResponseEntity.ok(response);
    }
}