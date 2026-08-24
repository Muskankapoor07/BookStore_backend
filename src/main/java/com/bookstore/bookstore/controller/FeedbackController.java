package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.dto.FeedbackRequest;
import com.bookstore.bookstore.dto.FeedbackResponse;
import com.bookstore.bookstore.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookstore_user")
@Tag(
        name = "Feedback",
        description = "API's for user feedbacks on product in the system"
)
@SecurityRequirement(name = "bearerAuth")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(
            FeedbackService feedbackService) {

        this.feedbackService = feedbackService;
    }

    // ================= ADD FEEDBACK =================

    @PostMapping("/add/feedback/{product_id}")
    @Operation(
            summary = "Add feedback",
            description = "Add feedback for a product"
    )
    public ResponseEntity<FeedbackResponse> addFeedback(
            @PathVariable("product_id") Long productId,
            @RequestBody FeedbackRequest request) {

        FeedbackResponse response =
                feedbackService.addFeedback(
                        productId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // ================= GET FEEDBACK =================

    @GetMapping("/get/feedback/{product_id}")
    @Operation(
            summary = "Get product feedback",
            description = "Get all feedback for a product"
    )
    public ResponseEntity<List<FeedbackResponse>> getFeedback(
            @PathVariable("product_id") Long productId) {

        List<FeedbackResponse> response =
                feedbackService.getFeedback(productId);

        return ResponseEntity.ok(response);
    }
}