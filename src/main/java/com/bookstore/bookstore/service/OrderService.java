package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.OrderResponse;

import java.util.List;

public interface OrderService {


    OrderResponse createOrder();

    List<OrderResponse> getAllOrders();
}