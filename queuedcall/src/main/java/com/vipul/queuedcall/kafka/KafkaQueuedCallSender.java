package com.vipul.queuedcall.kafka;

import com.vipul.queuedcall.QueuedCall;
import com.vipul.queuedcall.core.QueuedCallSender;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaQueuedCallSender implements QueuedCallSender {

    private final KafkaTemplate kafkaTemplate;

    @Override
    public void send(QueuedCall queuedCall) {
        this.kafkaTemplate.send("queued-call", queuedCall);
    }
}
