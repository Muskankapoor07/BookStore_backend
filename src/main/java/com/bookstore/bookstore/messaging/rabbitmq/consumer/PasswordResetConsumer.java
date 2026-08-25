package com.bookstore.bookstore.messaging.rabbitmq.consumer;

import com.bookstore.bookstore.config.RabbitMQConfig;
import com.bookstore.bookstore.dto.event.PasswordResetEvent;
import com.bookstore.bookstore.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetConsumer {

    private final EmailService emailService;

    public PasswordResetConsumer(
            EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(
            queues = RabbitMQConfig.PASSWORD_RESET_QUEUE
    )
    public void consumePasswordResetEvent(
            PasswordResetEvent event) {

        emailService.sendPasswordResetEmail(
                event.getEmail(),
                event.getResetToken()
        );
    }
}