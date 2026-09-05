package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.NewOrder;
import com.bookstore.bookstore.dto.NewOrderProperties;
import com.bookstore.bookstore.dto.OrderResponse;
import com.bookstore.bookstore.dto.event.OrderCreatedEvent;
import com.bookstore.bookstore.enums.OrderStatus;
import com.bookstore.bookstore.messaging.rabbitmq.producer.OrderMessageProducer;
import com.bookstore.bookstore.model.CartItem;
import com.bookstore.bookstore.model.Order;
import com.bookstore.bookstore.model.OrderItem;
import com.bookstore.bookstore.model.Product;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.CartItemRepository;
import com.bookstore.bookstore.repository.OrderItemRepository;
import com.bookstore.bookstore.repository.OrderRepository;
import com.bookstore.bookstore.repository.ProductRepository;
import com.bookstore.bookstore.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMessageProducer orderMessageProducer;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            CartItemRepository cartItemRepository,
            OrderMessageProducer orderMessageProducer) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderMessageProducer = orderMessageProducer;
    }

    // ================= CREATE ORDER =================

    @Override
    @Transactional
    public OrderResponse createOrder(NewOrder request) {

        User user = getCurrentUser();

        // Validate request
        if (request == null
                || request.getOrders() == null
                || request.getOrders().isEmpty()) {

            throw new RuntimeException(
                    "Order must contain at least one product"
            );
        }

        double totalAmount = 0.0;

        // ================= CREATE ORDER =================

        Order order = Order.builder()
                .user(user)
                .totalAmount(0.0)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        // ================= SAVE ORDER ITEMS =================

        for (NewOrderProperties item : request.getOrders()) {

            // Validate product id
            if (item.getProduct_id() == null
                    || item.getProduct_id().isBlank()) {

                throw new RuntimeException(
                        "Product id is required"
                );
            }

            // Validate quantity
            if (item.getProduct_quantity() == null
                    || item.getProduct_quantity() <= 0) {

                throw new RuntimeException(
                        "Product quantity must be greater than 0"
                );
            }

            Long productId;

            try {

                productId =
                        Long.valueOf(item.getProduct_id());

            } catch (NumberFormatException e) {

                throw new RuntimeException(
                        "Invalid product id: "
                                + item.getProduct_id()
                );
            }

            // ================= FIND PRODUCT =================

            Product product =
                    productRepository.findById(productId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Product not found with id: "
                                                    + item.getProduct_id()
                                    )
                            );

            // ================= CHECK STOCK =================

            if (item.getProduct_quantity()
                    > product.getQuantity()) {

                throw new RuntimeException(
                        "Only "
                                + product.getQuantity()
                                + " items available in stock for "
                                + product.getName()
                );
            }

            // ================= EFFECTIVE PRICE =================

            double price =
                    product.getDiscountPrice() != null
                            ? product.getDiscountPrice()
                            : product.getPrice();

            // ================= CALCULATE ITEM TOTAL =================

            double itemTotal =
                    price * item.getProduct_quantity();

            totalAmount += itemTotal;

            // ================= SAVE ORDER ITEM =================

            OrderItem orderItem =
                    OrderItem.builder()
                            .order(savedOrder)
                            .product(product)
                            .productName(product.getName())
                            .quantity(item.getProduct_quantity())
                            .price(price)
                            .build();

            orderItemRepository.save(orderItem);

            // ================= UPDATE STOCK =================

            product.setQuantity(
                    product.getQuantity()
                            - item.getProduct_quantity()
            );

            productRepository.save(product);
        }

        // ================= UPDATE ORDER TOTAL =================

        savedOrder.setTotalAmount(totalAmount);

        Order finalOrder =
                orderRepository.save(savedOrder);

        // ================= CLEAR CART =================

        List<CartItem> cartItems =
                cartItemRepository.findByUser(user);

        cartItemRepository.deleteAll(cartItems);

        // ================= RABBITMQ EVENT =================

        OrderCreatedEvent event =
                OrderCreatedEvent.builder()
                        .orderId(finalOrder.getId())
                        .userEmail(user.getEmail())
                        .totalAmount(
                                finalOrder.getTotalAmount()
                        )
                        .build();

        orderMessageProducer.sendOrderCreatedEvent(event);

        // ================= RESPONSE =================

        return convertToResponse(finalOrder);
    }

    // ================= GET ALL ORDERS =================

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {

        return orderRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    // ================= UPDATE ORDER STATUS =================

    @Override
    public OrderResponse updateOrderStatus(
            Long id,
            OrderStatus status) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found with id: "
                                                + id
                                )
                        );

        order.setStatus(status);

        Order updatedOrder =
                orderRepository.save(order);

        return convertToResponse(updatedOrder);
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

    private OrderResponse convertToResponse(
            Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}