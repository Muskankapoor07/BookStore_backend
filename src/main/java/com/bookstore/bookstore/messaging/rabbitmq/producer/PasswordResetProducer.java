package com.bookstore.bookstore.messaging.rabbitmq.producer;

import com.bookstore.bookstore.config.RabbitMQConfig;
import com.bookstore.bookstore.dto.event.PasswordResetEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetProducer {

    private final RabbitTemplate rabbitTemplate;

    public PasswordResetProducer(
            RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPasswordResetEvent(
            String email,
            String resetToken) {

        PasswordResetEvent event =
                new PasswordResetEvent(
                        email,
                        resetToken
                );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PASSWORD_RESET_EXCHANGE,
                RabbitMQConfig.PASSWORD_RESET_ROUTING_KEY,
                event
        );
    }
}