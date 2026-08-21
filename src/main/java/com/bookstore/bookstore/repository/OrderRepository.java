package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.model.Order;
import com.bookstore.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}