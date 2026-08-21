package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.OrderResponse;
import com.bookstore.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookstore_user")
@Tag(name = "Order", description = "Order APIs")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

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
}