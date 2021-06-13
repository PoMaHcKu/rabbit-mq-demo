package com.rabbit.receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@SpringBootApplication
public class RabbitReceiver {

    public static void main(String[] args) {
        SpringApplication.run(RabbitReceiver.class, args);
    }

    @Bean
    public ConnectionFactory connectionFactory(@Value("${spring.rabbitmq.queue.name}") String queueName,
                                               @Value("${spring.rabbitmq.port}") int port,
                                               @Value("${spring.rabbitmq.host}") String host) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
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
            System.out.println(" [x][x][x] Hello, " + message + " [x][x][x]");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
        return factory;
    }
}
