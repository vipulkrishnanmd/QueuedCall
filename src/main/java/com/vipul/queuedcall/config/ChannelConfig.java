package com.vipul.queuedcall.config;

import com.vipul.queuedcall.core.QueuedCallListener;
import com.vipul.queuedcall.core.QueuedCallSender;
import com.vipul.queuedcall.kafka.KafkaQueuedCallBatchedListener;
import com.vipul.queuedcall.kafka.KafkaQueuedCallListener;
import com.vipul.queuedcall.kafka.KafkaQueuedCallSender;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class ChannelConfig {
    private final KafkaTemplate kafkaTemplate;
    private final MessageListenerContainer messageListenerContainer;

    @Bean
    @ConditionalOnMissingBean(QueuedCallSender.class)
    // Auto-configured to use the kafka template
    // But the user can override this config.
    public QueuedCallSender queuedCallSender() {
        return new KafkaQueuedCallSender(kafkaTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(QueuedCallListener.class)
    public QueuedCallListener queuedCallListener() {
        return new KafkaQueuedCallListener(messageListenerContainer);
    }
}
