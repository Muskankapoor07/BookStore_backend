package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.FeedbackRequest;
import com.bookstore.bookstore.dto.FeedbackResponse;

import java.util.List;

public interface FeedbackService {

    FeedbackResponse addFeedback(
            Long productId,
            FeedbackRequest request
    );

    List<FeedbackResponse> getFeedback(
            Long productId
    );
}