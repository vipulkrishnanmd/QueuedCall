package com.vipul.queuedcall.core;

import com.vipul.queuedcall.QueuedCall;
import com.vipul.queuedcall.QueuedCallBatchedRequest;
import com.vipul.queuedcall.QueuedCallRequest;
import com.vipul.queuedcall.QueuedCallResponse;
import com.vipul.queuedcall.annotation.QueueCalledTarget;
import com.vipul.queuedcall.annotation.QueuedCallApi;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component("queuedCallEngine")
@RequiredArgsConstructor
public class QueuedCallEngine {
    @Qualifier("queueCalledMethods")
    private final Map<String, Method> queueCalledMethods;
    @Qualifier("batchedQueueCalledMethods")
    private final Map<String, Method> batchedQueueCalledMethods;
    private final QueuedCallSender queuedCallSender;
    private final BeanFactory beanFactory;
    private final GenericApplicationContext applicationContext;

    private void processRequest(QueuedCallRequest request) {

        // TODO-for-streams - Check the config. if streams is enabled, somebody will be batching the requests
        // each method should have an annotation to tell if it supports the batched requests.
        // if yes, call the method with batched params - eq [[param1, param2], [param1, param2]]
        // ---- or probably map, like [id1 -> [param1, param2], id2 -> [param1, param2]]
        // ---- and return a map like [id1 -> result1, id2 -> result2]
        // if no, probably call one by one.
        // ALTERNATIVELY, probably the batching code (streaming code) can check this annotation and batch it only,
        // if it supports it.

        // once executed, the results can be sent independently.
        Method method = queueCalledMethods.get(request.getName());

        try {
            Object result = method.invoke(this.beanFactory.getBean(method.getDeclaringClass()), request.getArgs());
            queuedCallSender.send(
                    QueuedCallResponse.builder()
                            .response(result)
                            .id(request.getId())
                            .type("response")
                            .build());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void processBatchedRequest(QueuedCallBatchedRequest request) {
        System.out.println("reaching QueuedCallBatchedRequest processing");
        Method method = batchedQueueCalledMethods.get(request.getName());

        if (method != null) {
            Map<String, Object[]> requests = request.getBatch().stream().collect(Collectors.toMap(r -> r.getId(), r -> r.getArgs()));
            try {
                Map<String, Object> result = (Map<String, Object>) method.invoke(this.beanFactory.getBean(method.getDeclaringClass()), requests);
                result.keySet().stream().forEach(id -> {
                    queuedCallSender.send(
                            QueuedCallResponse.builder()
                                    .response(result.get(id))
                                    .id(id)
                                    .type("response")
                                    .build());
                });
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            request.getBatch().stream().forEach(r -> this.processRequest(r));
        }
    }

    private void processResponse(QueuedCallResponse response) {
        CompletableFuture<Object> result = ResultStore.resultMap.get(response.getId());
        if (result != null) {
            result.complete(response.getResponse());
        }
    }

    public CompletableFuture<Object> sendRequest(Method method, Object[] methodArgs) {
        String id = UUID.randomUUID().toString();
        QueueCalledTarget annotation = method.getAnnotation(QueueCalledTarget.class);

        QueuedCallRequest request = QueuedCallRequest.builder()
                .id(id)
                .type("request")
                .name(annotation != null ? annotation.value() : method.getName())
                .paramTypes(method.getParameterTypes())
                .args(methodArgs)
                .build();

        queuedCallSender.send(request);
        CompletableFuture<Object> result = new CompletableFuture<>();
        ResultStore.resultMap.put(id, result);
        return result;
    }

    public void listen(QueuedCall data) {
        // TODO: Uncomment this - add some way to only process the respective type of request
//        if ("request".equals(data.getType())) {
//            QueuedCallRequest request = (QueuedCallRequest) data;
//            this.processRequest(request);
//        }

        if ("batched-request".equals(data.getType())) {
            QueuedCallBatchedRequest request = (QueuedCallBatchedRequest) data;
            this.processBatchedRequest(request);
        }

        if ("response".equals(data.getType())) {
            QueuedCallResponse response = (QueuedCallResponse) data;
            this.processResponse(response);
        }
    }

    @PostConstruct
    public void registerQueuedApis() {
        Reflections reflectionsTwo = new Reflections(
                "com.vipul", // TODO: get from properties file
                Scanners.TypesAnnotated);

        reflectionsTwo.getTypesAnnotatedWith(QueuedCallApi.class).stream().forEach(type -> {
            Class typ = type;
            Object proxy = Proxy.newProxyInstance(
                    typ.getClassLoader(),
                    new Class<?>[] {typ},
                    (p, method, methodArgs) -> this.sendRequest(method, methodArgs));
            this.applicationContext.registerBean(typ.getName(), typ, () -> typ.cast(proxy));
        });
    }
}
