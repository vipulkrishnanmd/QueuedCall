package com.vipul.queuedcall.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic messagingBusTopic() {
        return TopicBuilder.name("queued-call").partitions(3).build();
    }

    @Bean
    public NewTopic kafkaStreamTopic() {
        return TopicBuilder.name("queued-call-stream").partitions(3).build();
    }
}
