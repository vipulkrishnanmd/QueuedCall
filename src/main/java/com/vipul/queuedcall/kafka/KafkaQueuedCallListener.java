package com.vipul.queuedcall.kafka;

import com.vipul.queuedcall.model.QueuedCall;
import com.vipul.queuedcall.core.QueuedCallListener;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

import javax.annotation.PostConstruct;
@RequiredArgsConstructor
public class KafkaQueuedCallListener extends QueuedCallListener {
    private final MessageListenerContainer container;

    @PostConstruct
    public void listen() {
        container.setupMessageListener(
                (MessageListener<String, QueuedCall>) (d) -> this.processQueuedCall(d.value()));
    }
}
