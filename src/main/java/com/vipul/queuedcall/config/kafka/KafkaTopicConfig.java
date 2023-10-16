package com.vipul.queuedcall.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static String TOPIC_NAME = "queued-call";

    @Bean
    public NewTopic messagingBusTopic() {
        return TopicBuilder.name(KafkaTopicConfig.TOPIC_NAME).partitions(3).build();
    }
}
