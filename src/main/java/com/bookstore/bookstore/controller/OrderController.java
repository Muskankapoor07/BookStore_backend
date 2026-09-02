package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.NewOrder;
import com.bookstore.bookstore.dto.OrderResponse;
import com.bookstore.bookstore.enums.OrderStatus;
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
            summary = "Add new order",
            description = "Create a new order for the logged-in user"
    )
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody NewOrder request) {

        OrderResponse response =
                orderService.createOrder(request);

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

    // ================= ADMIN UPDATE ORDER STATUS =================

    @PutMapping("/admin/order/{id}/status")
    @Operation(
            summary = "Update order status",
            description = "Update the status of an order by admin"
    )
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        OrderResponse response =
                orderService.updateOrderStatus(id, status);

        return ResponseEntity.ok(response);
    }
}