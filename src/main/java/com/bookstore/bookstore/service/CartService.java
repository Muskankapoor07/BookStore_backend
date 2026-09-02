package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.CartItemResponse;
import com.bookstore.bookstore.dto.MultipleCartItemRequest;

import java.util.List;

public interface CartService {

    CartItemResponse addCartItem(Long productId);

    CartItemResponse updateQuantity(Long cartItemId, int quantity);

    void removeCartItem(Long cartItemId);

    List<CartItemResponse> getCartItems();

    List<CartItemResponse> addMultipleCartItems(
            MultipleCartItemRequest request);
}