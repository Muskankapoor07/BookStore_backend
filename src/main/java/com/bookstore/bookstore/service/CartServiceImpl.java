package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.CartItemResponse;
import com.bookstore.bookstore.model.CartItem;
import com.bookstore.bookstore.model.Product;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.CartItemRepository;
import com.bookstore.bookstore.repository.ProductRepository;
import com.bookstore.bookstore.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CartItemResponse addCartItem(Long productId) {

        User user = getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        CartItem cartItem = cartItemRepository
                .findByUserAndProduct(user, product)
                .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity() + 1
            );

        } else {

            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
        }

        CartItem savedCartItem =
                cartItemRepository.save(cartItem);

        return toResponse(savedCartItem);
    }


    @Override
    public CartItemResponse updateQuantity(
            Long cartItemId,
            int quantity) {

        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cart item not found"
                        )
                );

        User user = getCurrentUser();

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You cannot update this cart item"
            );
        }

        if (quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than 0"
            );
        }

        cartItem.setQuantity(quantity);

        CartItem updatedCartItem =
                cartItemRepository.save(cartItem);

        return toResponse(updatedCartItem);
    }

    @Override
    public void removeCartItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cart item not found"
                        )
                );

        User user = getCurrentUser();

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You cannot remove this cart item"
            );
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<CartItemResponse> getCartItems() {

        User user = getCurrentUser();

        return cartItemRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }

    private CartItemResponse toResponse(
            CartItem cartItem) {

        Product product = cartItem.getProduct();

        double totalPrice =
                product.getPrice() * cartItem.getQuantity();

        return CartItemResponse.builder()
                .id(cartItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .quantity(cartItem.getQuantity())
                .totalPrice(totalPrice)
                .build();
    }
}