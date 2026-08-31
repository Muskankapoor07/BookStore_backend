package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.OrderResponse;
import com.bookstore.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookstore_user")
@Tag(name = "Order", description = "Order APIs")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ================= CREATE ORDER =================

    @PostMapping("/add/order")
    @Operation(
            summary = "Create order",
            description = "Create a new order for the logged-in user"
    )
    public ResponseEntity<OrderResponse> createOrder() {

        OrderResponse response =
                orderService.createOrder();

        return ResponseEntity.ok(response);
    }

    // ================= ADMIN GET ALL ORDERS =================

    @GetMapping("/admin/orders")
    @Operation(
            summary = "Get all orders",
            description = "Get all orders in the system for admin"
    )
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> orders =
                orderService.getAllOrders();

        return ResponseEntity.ok(orders);
    }
}