package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.CartItemResponse;
import com.bookstore.bookstore.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookstore_user")
@Tag(name = "Cart", description = "Cart APIs")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add_cart_item/{product_id}")
    @Operation(
            summary = "Add product to cart",
            description = "Add a product to the user's cart"
    )
    public ResponseEntity<CartItemResponse> addCartItem(
            @PathVariable("product_id") Long productId) {

        CartItemResponse response =
                cartService.addCartItem(productId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/cart_item_quantity/{cartItem_id}")
    @Operation(
            summary = "Update cart item quantity",
            description = "Update quantity of a cart item"
    )
    public ResponseEntity<CartItemResponse> updateQuantity(
            @PathVariable("cartItem_id") Long cartItemId,
            @RequestParam int quantity) {

        CartItemResponse response =
                cartService.updateQuantity(
                        cartItemId,
                        quantity
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove_cart_item/{cartItem_id}")
    @Operation(
            summary = "Remove cart item",
            description = "Remove a product from the cart"
    )
    public ResponseEntity<String> removeCartItem(
            @PathVariable("cartItem_id") Long cartItemId) {

        cartService.removeCartItem(cartItemId);

        return ResponseEntity.ok(
                "Cart item removed successfully"
        );
    }

    @GetMapping("/get_cart_items")
    @Operation(
            summary = "Get cart items",
            description = "Get all cart items of the logged-in user"
    )
    public ResponseEntity<List<CartItemResponse>> getCartItems() {

        List<CartItemResponse> cartItems =
                cartService.getCartItems();

        return ResponseEntity.ok(cartItems);
    }
}