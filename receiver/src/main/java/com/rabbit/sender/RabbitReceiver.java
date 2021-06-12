package com.rabbit.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@SpringBootApplication
public class RabbitReceiver {

    @Autowired
    private ConnectionFactory factory;

    public static void main(String[] args) {
        SpringApplication.run(RabbitReceiver.class, args);
    }

    @Bean
    public ConnectionFactory connectionFactory(@Value("${spring.rabbitmq.queue.name}") String queueName) throws Exception {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, false, false, false, Collections.emptyMap());
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                //imitate of long work
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
        return factory;
    }
}
