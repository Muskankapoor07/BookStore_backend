package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.ProductResponse;
import com.bookstore.bookstore.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookstore_user")
@Tag(name = "Product", description = "Product APIs")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ================= GET ALL BOOKS =================

    @GetMapping("/get/book")
    @Operation(
            summary = "Get all books",
            description = "Get all available books"
    )
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> products =
                productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    // ================= GET BOOK BY ID =================

    @GetMapping("/get/book/{id}")
    @Operation(
            summary = "Get book by ID",
            description = "Get a book using its ID"
    )
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id) {

        ProductResponse product =
                productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    // ================= SEARCH BOOKS =================

    @GetMapping("/search/book")
    @Operation(
            summary = "Search books",
            description = "Search books by name or author"
    )
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String keyword) {

        List<ProductResponse> products =
                productService.searchProducts(keyword);

        return ResponseEntity.ok(products);
    }
}