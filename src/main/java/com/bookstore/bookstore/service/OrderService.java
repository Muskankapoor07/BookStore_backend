package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.OrderResponse;
import com.bookstore.bookstore.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder();

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrderStatus(Long id, OrderStatus status);
}