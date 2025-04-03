package com.desafio.tecnico.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receiveMessage(String message) {
        logger.info("Mensagem recebida do RabbitMQ: {}", message);
    }
}
