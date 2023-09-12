package com.vipul.queuedcall.core;

import com.vipul.queuedcall.QueuedCall;
import com.vipul.queuedcall.QueuedCallRequest;
import com.vipul.queuedcall.QueuedCallResponse;
import com.vipul.queuedcall.config.ResultStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

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
