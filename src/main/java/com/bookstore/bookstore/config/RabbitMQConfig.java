package com.bookstore.bookstore.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ================= ORDER =================

    public static final String ORDER_EXCHANGE =
            "bookstore.order.exchange";

    public static final String ORDER_QUEUE =
            "bookstore.order.queue";

    public static final String ORDER_ROUTING_KEY =
            "order.created";

    // ================= PASSWORD RESET =================

    public static final String PASSWORD_RESET_EXCHANGE =
            "bookstore.password.reset.exchange";

    public static final String PASSWORD_RESET_QUEUE =
            "bookstore.password.reset.queue";

    public static final String PASSWORD_RESET_ROUTING_KEY =
            "password.reset";

    // ================= ORDER CONFIG =================

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public Binding orderBinding(
            Queue orderQueue,
            DirectExchange orderExchange) {

        return BindingBuilder
                .bind(orderQueue)
                .to(orderExchange)
                .with(ORDER_ROUTING_KEY);
    }

    // ================= PASSWORD RESET CONFIG =================

    @Bean
    public DirectExchange passwordResetExchange() {
        return new DirectExchange(PASSWORD_RESET_EXCHANGE);
    }

    @Bean
    public Queue passwordResetQueue() {
        return new Queue(PASSWORD_RESET_QUEUE, true);
    }

    @Bean
    public Binding passwordResetBinding(
            Queue passwordResetQueue,
            DirectExchange passwordResetExchange) {

        return BindingBuilder
                .bind(passwordResetQueue)
                .to(passwordResetExchange)
                .with(PASSWORD_RESET_ROUTING_KEY);
    }

    // ================= MESSAGE CONVERTER =================

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}