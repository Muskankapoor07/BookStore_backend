package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.NewOrder;
import com.bookstore.bookstore.dto.OrderResponse;
import com.bookstore.bookstore.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponse createOrder(NewOrder request);

    Page<OrderResponse> getAllOrders(Pageable pageable);

    OrderResponse updateOrderStatus(Long id, OrderStatus status);
}