package com.rabbit.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@RestController
public class DemoClientApplication {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Value("${spring.rabbitmq.queue.name}")
    private String queueName;

    public static void main(String[] args) {
        SpringApplication.run(DemoClientApplication.class, args);
    }

    @GetMapping
    public void sendRequest(@RequestParam(required = false) String message) {
        if (message.isBlank()) {
            message = "User!";
        }
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queueName, false, false, false, Collections.emptyMap());
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}