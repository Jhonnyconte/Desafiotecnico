package com.desafio.tecnico.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducer.class);
    @Value("${rabbitmq.queue}")
    private String queueName;
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        logger.info("Enviando mensagem para RabbitMQ: {}", message);
        rabbitTemplate.convertAndSend(queueName, message);
    }
}