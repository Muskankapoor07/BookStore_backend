package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.UserResponse;
import com.bookstore.bookstore.dto.UserUpdateRequest;
import com.bookstore.bookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookstore_user")
@Tag(
        name = "Customer Details",
        description = "API's for updating customer details in the system"
)
public class CustomerDetailsController {

    private final UserService userService;

    public CustomerDetailsController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/edit_user")
    @Operation(
            summary = "Update customer details",
            description = "Update logged-in customer details"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponse> updateUser(
            @RequestBody UserUpdateRequest request) {

        UserResponse response =
                userService.updateUser(request);

        return ResponseEntity.ok(response);
    }
}