package com.vipul.queuedcall.sample;

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
public class SampleConfig {
    private final MessageListenerContainer messageListenerContainer;

    @Bean
    // Note: For batched, use the below commented config instead
    public QueuedCallListener queuedCallListener() {
        return new KafkaQueuedCallListener(messageListenerContainer);
    }

//    @Bean
//    public QueuedCallListener queuedCallListener() {
//        return new KafkaQueuedCallBatchedListener(messageListenerContainer);
//    }
}
