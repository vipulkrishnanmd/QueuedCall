package com.vipul.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class BusApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, Object> kafkaTemplate) {
        return args -> {
            for (int i = 0; i < 100; i++) {
                kafkaTemplate.send("messaging-bus", new Test("yes", "yes"));
            }
        };
    }
}
