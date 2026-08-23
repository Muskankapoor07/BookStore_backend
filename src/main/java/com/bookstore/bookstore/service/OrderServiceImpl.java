package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.OrderResponse;
import com.bookstore.bookstore.dto.event.OrderCreatedEvent;
import com.bookstore.bookstore.enums.OrderStatus;
import com.bookstore.bookstore.messaging.rabbitmq.producer.OrderMessageProducer;
import com.bookstore.bookstore.model.Order;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.OrderRepository;
import com.bookstore.bookstore.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMessageProducer orderMessageProducer;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            UserRepository userRepository,
            OrderMessageProducer orderMessageProducer) {

        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderMessageProducer = orderMessageProducer;
    }

    @Override
    public OrderResponse createOrder() {

        User user = getCurrentUser();

        Order order = Order.builder()
                .user(user)
                .totalAmount(0.0)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        // Create RabbitMQ event
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(savedOrder.getId())
                .userEmail(user.getEmail())
                .totalAmount(savedOrder.getTotalAmount())
                .build();

        // Send event to RabbitMQ
        orderMessageProducer.sendOrderCreatedEvent(event);

        return OrderResponse.builder()
                .id(savedOrder.getId())
                .totalAmount(savedOrder.getTotalAmount())
                .status(savedOrder.getStatus())
                .createdAt(savedOrder.getCreatedAt())
                .build();
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }
}