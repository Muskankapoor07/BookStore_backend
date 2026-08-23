package com.bookstore.bookstore.dto;

import lombok.Data;

@Data
public class FeedbackRequest {

    private Integer rating;

    private String comment;
}