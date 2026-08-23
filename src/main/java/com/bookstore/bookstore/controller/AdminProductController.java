package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.ProductRequest;
import com.bookstore.bookstore.dto.ProductResponse;
import com.bookstore.bookstore.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookstore_user/admin")
@Tag(name = "Admin-Product", description = "Admin Product APIs")
@SecurityRequirement(name = "bearerAuth")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {

        this.productService = productService;
    }

    @PostMapping("/add/book")
    @Operation(
            summary = "Add book",
            description = "Admin can add a new book"
    )
    public ResponseEntity<ProductResponse> addBook(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.addProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/update/book/{id}")
    @Operation(
            summary = "Update book",
            description = "Admin can update an existing book"
    )
    public ResponseEntity<ProductResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.updateProduct(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/book/{id}")
    @Operation(
            summary = "Delete book",
            description = "Admin can delete a book by ID"
    )
    public ResponseEntity<String> deleteBook(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                "Book deleted successfully"
        );
    }
}