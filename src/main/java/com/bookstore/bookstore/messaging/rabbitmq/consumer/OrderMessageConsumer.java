package com.bookstore.bookstore.messaging.rabbitmq.consumer;

import com.bookstore.bookstore.config.RabbitMQConfig;
import com.bookstore.bookstore.dto.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageConsumer {

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void receiveOrderCreatedEvent(
            OrderCreatedEvent event) {

        System.out.println(
                "Order Created Event Received: " + event
        );
    }
}