package com.bookstore.bookstore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackResponse {

    private Long id;

    private Long productId;

    private String userEmail;

    private Integer rating;

    private String comment;
}