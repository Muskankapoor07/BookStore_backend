package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.WishlistResponse;
import com.bookstore.bookstore.model.Product;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.model.Wishlist;
import com.bookstore.bookstore.repository.ProductRepository;
import com.bookstore.bookstore.repository.UserRepository;
import com.bookstore.bookstore.repository.WishlistRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishlistServiceImpl(
            WishlistRepository wishlistRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private User getLoggedInUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    @Override
    public WishlistResponse addToWishlist(Long productId) {

        User user = getLoggedInUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        if (wishlistRepository.existsByUserIdAndProductId(
                user.getId(), productId)) {

            throw new RuntimeException(
                    "Product already exists in wishlist"
            );
        }

        Wishlist wishlist = new Wishlist();

        wishlist.setUser(user);
        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);

        return new WishlistResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }

    @Override
    public String removeFromWishlist(Long productId) {

        User user = getLoggedInUser();

        Wishlist wishlist =
                wishlistRepository
                        .findByUserIdAndProductId(
                                user.getId(),
                                productId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found in wishlist"
                                ));

        wishlistRepository.delete(wishlist);

        return "Product removed from wishlist successfully";
    }

    @Override
    public List<WishlistResponse> getWishlist() {

        User user = getLoggedInUser();

        return wishlistRepository
                .findByUserId(user.getId())
                .stream()
                .map(wishlist -> new WishlistResponse(
                        wishlist.getProduct().getId(),
                        wishlist.getProduct().getName(),
                        wishlist.getProduct().getPrice()
                ))
                .toList();
    }
}