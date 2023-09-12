package com.vipul.queuedcall.kafka;

import com.vipul.queuedcall.QueuedCall;
import com.vipul.queuedcall.core.QueuedCallListener;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaQueuedCallListener {

    private final QueuedCallListener listener;

    @KafkaListener(
            topics = "queued-call",
            groupId = "one",
            containerFactory = "factory"
    )
    void listen(QueuedCall data) {
        listener.listen(data);
    }
}
