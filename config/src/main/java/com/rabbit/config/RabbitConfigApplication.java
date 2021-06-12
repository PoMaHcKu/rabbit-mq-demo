package com.rabbit.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitConfigApplication.class, args);
    }

    @Bean
    public ConnectionFactory connectionFactory(@Value("${spring.rabbitmq.host}") String rabbitHost,
                                               @Value("${spring.rabbitmq.port}") int rabbitPort) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitHost);
        factory.setPort(rabbitPort);
        return factory;
    }
}