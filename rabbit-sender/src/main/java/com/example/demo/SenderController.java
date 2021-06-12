package com.example.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

@RestController
public class SenderController {

    private final String QUEUE_NAME = "http_get";

    @Autowired
    private ConnectionFactory connectionFactory;

    @GetMapping
    public ResponseEntity<String> sendRequest() {

        String message = "Hello";

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, Collections.emptyMap());
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("OK: " + message, HttpStatus.OK);
    }

}