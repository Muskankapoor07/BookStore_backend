package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.FeedbackRequest;
import com.bookstore.bookstore.dto.FeedbackResponse;
import com.bookstore.bookstore.model.Feedback;
import com.bookstore.bookstore.repository.FeedbackRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(
            FeedbackRepository feedbackRepository) {

        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public FeedbackResponse addFeedback(
            Long productId,
            FeedbackRequest request) {

        String userEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Feedback feedback = Feedback.builder()
                .productId(productId)
                .userEmail(userEmail)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Feedback savedFeedback =
                feedbackRepository.save(feedback);

        return mapToResponse(savedFeedback);
    }

    @Override
    public List<FeedbackResponse> getFeedback(
            Long productId) {

        return feedbackRepository
                .findByProductId(productId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private FeedbackResponse mapToResponse(
            Feedback feedback) {

        return FeedbackResponse.builder()
                .id(feedback.getId())
                .productId(feedback.getProductId())
                .userEmail(feedback.getUserEmail())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .build();
    }
}