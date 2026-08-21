package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.model.CartItem;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(User user);
}