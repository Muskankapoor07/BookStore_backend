package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.ProductRequest;
import com.bookstore.bookstore.dto.ProductResponse;
import com.bookstore.bookstore.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookstore_user")
@Tag(
        name = "Admin-Product",
        description = "APIs for product in the system"
)
@SecurityRequirement(name = "bearerAuth")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/add/book")
    public ResponseEntity<ProductResponse> addBook(
            @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.addBook(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/admin/update/book/{product_id}")
    public ResponseEntity<ProductResponse> updateBook(
            @PathVariable("product_id") Long productId,
            @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.updateBook(productId, request);

        return ResponseEntity.ok(response);
    }
}