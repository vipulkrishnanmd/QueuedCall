package com.vipul.queuedcall.core;

import com.vipul.queuedcall.model.QueuedCall;
import com.vipul.queuedcall.model.QueuedCallBatchedRequest;
import com.vipul.queuedcall.model.QueuedCallResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class QueuedCallBatchedListener extends QueuedCallListener{
    @Autowired
    @Qualifier("batchedQueueCalledMethods")
    private Map<String, Method> batchedQueueCalledMethods;

    protected void listen(QueuedCall data) {
        if (QueuedCallType.BATCHED_REQUEST.equals(data.getType())) {
            QueuedCallBatchedRequest request = (QueuedCallBatchedRequest) data;
            this.processBatchedRequest(request);
        }

        if (QueuedCallType.RESPONSE.equals(data.getType())) {
            QueuedCallResponse response = (QueuedCallResponse) data;
            this.processResponse(response);
        }
    }

    protected void processBatchedRequest(QueuedCallBatchedRequest request) {
        System.out.println("reaching QueuedCallBatchedRequest processing");
        Method method = batchedQueueCalledMethods.get(request.getName());

        if (method != null) {
            Map<String, Object[]> requests = request.getBatch().stream()
                    .collect(Collectors.toMap(r -> r.getId(), r -> r.getArgs()));
            try {
                Map<String, Object> result = (Map<String, Object>) method.invoke(
                        this.beanFactory.getBean(method.getDeclaringClass()), requests);
                result.keySet().stream().forEach(id ->
                    queuedCallSender.send(
                            QueuedCallResponse.builder()
                                    .response(result.get(id))
                                    .id(id)
                                    .type(QueuedCallType.RESPONSE)
                                    .build()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            request.getBatch().stream().forEach(r -> this.processRequest(r));
        }
    }
}
