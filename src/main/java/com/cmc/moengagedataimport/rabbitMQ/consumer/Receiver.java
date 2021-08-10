package com.cmc.moengagedataimport.rabbitMQ.consumer;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class Receiver {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

    @RabbitListener(queues = "Moengage_queue")
    public void receive(String in) {
        System.out.println(" [x] Received '" + in + "'");
    }

}
