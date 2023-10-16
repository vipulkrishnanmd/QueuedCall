package com.vipul.queuedcall.kafka;

import com.vipul.queuedcall.config.kafka.KafkaTopicConfig;
import com.vipul.queuedcall.model.QueuedCall;
import com.vipul.queuedcall.core.QueuedCallSender;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class KafkaQueuedCallSender implements QueuedCallSender {

    private final KafkaTemplate kafkaTemplate;

    @Override
    public void send(QueuedCall queuedCall) {
        this.kafkaTemplate.send(KafkaTopicConfig.TOPIC_NAME, queuedCall);
    }
}
