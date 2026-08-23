package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.WishlistResponse;

import java.util.List;

public interface WishlistService {

    WishlistResponse addToWishlist(Long productId);

    String removeFromWishlist(Long productId);

    List<WishlistResponse> getWishlist();
}