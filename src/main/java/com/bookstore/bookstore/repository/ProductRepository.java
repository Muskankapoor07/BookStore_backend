package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String name,
            String author,
            Pageable pageable
    );
}