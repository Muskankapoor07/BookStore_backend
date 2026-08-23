package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.WishlistResponse;
import com.bookstore.bookstore.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookstore_user")
@Tag(
        name = "WishList",
        description = "API's for wish list items in the system"
)
@SecurityRequirement(name = "user_api_key")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/add_wish_list/{product_id}")
    @Operation(summary = "Add product to wishlist")
    public ResponseEntity<WishlistResponse> addToWishlist(
            @PathVariable("product_id") Long productId) {

        return ResponseEntity.ok(
                wishlistService.addToWishlist(productId)
        );
    }

    @DeleteMapping("/remove_wishlist_item/{product_id}")
    @Operation(summary = "Remove product from wishlist")
    public ResponseEntity<String> removeFromWishlist(
            @PathVariable("product_id") Long productId) {

        return ResponseEntity.ok(
                wishlistService.removeFromWishlist(productId)
        );
    }

    @GetMapping("/get_wishlist_items")
    @Operation(summary = "Get wishlist items")
    public ResponseEntity<List<WishlistResponse>> getWishlist() {

        return ResponseEntity.ok(
                wishlistService.getWishlist()
        );
    }
}