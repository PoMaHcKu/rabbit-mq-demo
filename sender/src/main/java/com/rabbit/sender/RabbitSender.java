package com.rabbit.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@RestController
public class RabbitSender {

    @Value("${spring.rabbitmq.queue.name}")
    private String queueName;

    @Autowired
    private ConnectionFactory factory;

    public static void main(String[] args) {
        SpringApplication.run(RabbitSender.class, args);
    }

    @Bean
    public ConnectionFactory connectionFactory(@Value("${spring.rabbitmq.port}") int port,
                                               @Value("${spring.rabbitmq.host}") String host) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setPort(port);
        factory.setHost(host);
        return factory;
    }

    @GetMapping
    public void sendRequest(@RequestParam(required = false) String message) {
        if (message == null || message.isBlank()) {
            message = "User!";
        }
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queueName, false, false, false, Collections.emptyMap());
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}