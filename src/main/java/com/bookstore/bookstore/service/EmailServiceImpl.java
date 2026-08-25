package com.bookstore.bookstore.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(
            String email,
            String resetToken) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Bookstore - Reset Password");

        message.setText(
                "Hello,\n\n"
                        + "You requested to reset your Bookstore password.\n\n"
                        + "Your password reset token is:\n\n"
                        + resetToken
                        + "\n\n"
                        + "This token will expire in 10 minutes.\n\n"
                        + "Use this token in the Reset Password API.\n\n"
                        + "If you did not request this, please ignore this email.\n\n"
                        + "Regards,\n"
                        + "Bookstore Team"
        );

        mailSender.send(message);
    }
}