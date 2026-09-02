package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.CartItemResponse;
import com.bookstore.bookstore.dto.MultipleCartItem;
import com.bookstore.bookstore.dto.MultipleCartItemRequest;
import com.bookstore.bookstore.model.CartItem;
import com.bookstore.bookstore.model.Product;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.CartItemRepository;
import com.bookstore.bookstore.repository.ProductRepository;
import com.bookstore.bookstore.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // ================= ADD SINGLE PRODUCT =================

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

    // ================= ADD MULTIPLE PRODUCTS =================

    @Override
    public List<CartItemResponse> addMultipleCartItems(
            MultipleCartItemRequest request) {

        User user = getCurrentUser();

        if (request == null
                || request.getItems() == null
                || request.getItems().isEmpty()) {

            throw new RuntimeException(
                    "Cart must contain at least one product"
            );
        }

        List<CartItemResponse> responses =
                new ArrayList<>();

        for (MultipleCartItem item : request.getItems()) {

            if (item.getProductId() == null) {
                throw new RuntimeException(
                        "Product id is required"
                );
            }

            if (item.getQuantity() == null
                    || item.getQuantity() <= 0) {

                throw new RuntimeException(
                        "Quantity must be greater than 0"
                );
            }

            Product product =
                    productRepository
                            .findById(item.getProductId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Product not found with id: "
                                                    + item.getProductId()
                                    )
                            );

            CartItem cartItem =
                    cartItemRepository
                            .findByUserAndProduct(user, product)
                            .orElse(null);

            if (cartItem != null) {

                cartItem.setQuantity(
                        cartItem.getQuantity()
                                + item.getQuantity()
                );

            } else {

                cartItem = new CartItem();
                cartItem.setUser(user);
                cartItem.setProduct(product);
                cartItem.setQuantity(item.getQuantity());
            }

            CartItem savedCartItem =
                    cartItemRepository.save(cartItem);

            responses.add(toResponse(savedCartItem));
        }

        return responses;
    }

    // ================= UPDATE QUANTITY =================

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

    // ================= REMOVE CART ITEM =================

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

    // ================= GET CART ITEMS =================

    @Override
    public List<CartItemResponse> getCartItems() {

        User user = getCurrentUser();

        return cartItemRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ================= GET CURRENT USER =================

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

    // ================= CONVERT TO RESPONSE =================

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