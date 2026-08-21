package com.bookstore.bookstore.dto;

import com.bookstore.bookstore.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private Double totalAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;
}