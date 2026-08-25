package com.bookstore.bookstore.service;

public interface EmailService {

    void sendPasswordResetEmail(
            String email,
            String resetToken
    );
}