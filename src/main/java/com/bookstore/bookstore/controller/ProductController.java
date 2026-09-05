package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.ProductResponse;
import com.bookstore.bookstore.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            description = "Get all available books with pagination and sorting"
    )
    public ResponseEntity<Page<ProductResponse>> getAllProducts(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id,asc") String sort) {

        String[] sortParams = sort.split(",");

        String field = sortParams[0];

        Sort.Direction direction =
                sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        PageRequest pageRequest =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(direction, field)
                );

        Page<ProductResponse> products =
                productService.getAllProducts(pageRequest);

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
            description = "Search books by name or author with pagination and sorting"
    )
    public ResponseEntity<Page<ProductResponse>> searchProducts(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id,asc") String sort) {

        String[] sortParams = sort.split(",");

        String field = sortParams[0];

        Sort.Direction direction =
                sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        PageRequest pageRequest =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(direction, field)
                );

        Page<ProductResponse> products =
                productService.searchProducts(
                        keyword,
                        pageRequest
                );

        return ResponseEntity.ok(products);
    }
}