package com.vipul.queuedcall.core;

import com.vipul.queuedcall.QueuedCall;
import com.vipul.queuedcall.QueuedCallRequest;
import com.vipul.queuedcall.QueuedCallResponse;
import com.vipul.queuedcall.config.ResultStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class QueuedCallListener {
    @Qualifier("queueCalledMethods")
    private final Map<String, Method> queueCalledMethods;
    private final KafkaTemplate kafkaTemplate;

    private final BeanFactory beanFactory;
    void listen(QueuedCall data) {
        if ("request".equals(data.getType())) {
            QueuedCallRequest request = (QueuedCallRequest) data;
            Method method = queueCalledMethods.get(request.getName());
            try {
                Object result = method.invoke(this.beanFactory.getBean(method.getDeclaringClass()), request.getArgs());
                kafkaTemplate.send("queued-call", QueuedCallResponse.builder().response(result).id(request.getId()).type("response").build());

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if ("response".equals(data.getType())) {
            QueuedCallResponse response = (QueuedCallResponse) data;
            CompletableFuture<Object> result = ResultStore.resultMap.get(response.getId());
            result.complete(response.getResponse());
        }
    }
}
