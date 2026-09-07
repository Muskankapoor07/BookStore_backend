package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByProductId(Long productId);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.productId = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    Long countByProductId(Long productId);
}
