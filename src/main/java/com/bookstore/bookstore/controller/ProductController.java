package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.ProductResponse;
import com.bookstore.bookstore.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookstore_user")
@Tag(name = "Product", description = "Product APIs")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/get/book")
    @Operation(
            summary = "Get all books",
            description = "Get all available books"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> products =
                productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/book")
    @Operation(
            summary = "Search books",
            description = "Search books by name or author"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String keyword) {

        List<ProductResponse> products =
                productService.searchProducts(keyword);

        return ResponseEntity.ok(products);
    }
}