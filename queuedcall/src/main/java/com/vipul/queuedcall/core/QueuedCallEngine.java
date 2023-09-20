package com.vipul.queuedcall.core;

import com.vipul.queuedcall.QueuedCall;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component("queuedCallEngine")
@RequiredArgsConstructor
public class QueuedCallEngine {
    @Qualifier("queueCalledMethods")
    private final Map<String, Method> queueCalledMethods;
    private final QueuedCallSender queuedCallSender;
    private final BeanFactory beanFactory;
    private final GenericApplicationContext applicationContext;

    private void processRequest(QueuedCallRequest request) {
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

    private void processResponse(QueuedCallResponse response) {
        CompletableFuture<Object> result = ResultStore.resultMap.get(response.getId());
        result.complete(response.getResponse());
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
        if ("request".equals(data.getType())) {
            QueuedCallRequest request = (QueuedCallRequest) data;
            this.processRequest(request);
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
