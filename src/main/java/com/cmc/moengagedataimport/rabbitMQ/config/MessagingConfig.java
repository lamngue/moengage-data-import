package com.cmc.moengagedataimport.rabbitMQ.config;

import com.cmc.moengagedataimport.rabbitMQ.consumer.Receiver;
import com.cmc.moengagedataimport.rabbitMQ.consumer.Sender;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MessagingConfig {

    public static final String QUEUE = "Moengage_queue";

    @Bean
    Queue queue() {
        return new Queue(QUEUE);
    }

    @Profile("receiver")
    @Bean
    public Receiver receiver() {
        return new Receiver();
    }

    @Profile("sender")
    @Bean
    public Sender sender() {
        return new Sender();
    }

}