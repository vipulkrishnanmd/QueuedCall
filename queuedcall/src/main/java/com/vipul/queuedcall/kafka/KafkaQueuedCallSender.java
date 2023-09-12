package com.vipul.queuedcall.kafka;

import com.vipul.queuedcall.QueuedCall;
import com.vipul.queuedcall.core.QueuedCallSender;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class KafkaQueuedCallSender implements QueuedCallSender {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public void send(QueuedCall queuedCall) {
        this.kafkaTemplate.send("queued-call", queuedCall);
    }
}
