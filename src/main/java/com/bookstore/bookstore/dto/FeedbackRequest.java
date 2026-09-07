package com.bookstore.bookstore.dto;

import lombok.Data;

@Data
public class FeedbackRequest {

    private Double rating;

    private String comment;
}
