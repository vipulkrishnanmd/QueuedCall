package com.vipul.queuedcall.core;

import com.vipul.queuedcall.model.QueuedCall;
import com.vipul.queuedcall.model.QueuedCallRequest;
import com.vipul.queuedcall.model.QueuedCallResponse;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class QueuedCallListener {
    @Autowired
    @Qualifier("queueCalledMethods")
    private Map<String, Method> queueCalledMethods;
    @Autowired
    protected QueuedCallSender queuedCallSender;
    @Autowired
    protected BeanFactory beanFactory;

    public abstract void listen();

    protected void processQueuedCall(QueuedCall data) {
        this.listen(data);
    }

    protected void listen(QueuedCall data) {
        if (QueuedCallType.REQUEST.equals(data.getType())) {
            QueuedCallRequest request = (QueuedCallRequest) data;
            this.processRequest(request);
        }

        if (QueuedCallType.RESPONSE.equals(data.getType())) {
            QueuedCallResponse response = (QueuedCallResponse) data;
            this.processResponse(response);
        }
    }

    protected void processRequest(QueuedCallRequest request) {
        Method method = queueCalledMethods.get(request.getName());

        try {
            Object result = method.invoke(this.beanFactory.getBean(method.getDeclaringClass()), request.getArgs());
            queuedCallSender.send(
                    QueuedCallResponse.builder()
                            .response(result)
                            .id(request.getId())
                            .type(QueuedCallType.RESPONSE)
                            .build());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected void processResponse(QueuedCallResponse response) {
        CompletableFuture<Object> result = ResultStore.resultMap.get(response.getId());
        if (result != null) {
            result.complete(response.getResponse());
        }
    }
}
