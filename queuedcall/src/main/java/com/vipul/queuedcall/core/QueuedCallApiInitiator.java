package com.vipul.queuedcall.core;

import com.vipul.queuedcall.QueuedCallRequest;
import com.vipul.queuedcall.annotation.QueueCalledTarget;
import com.vipul.queuedcall.annotation.QueuedCallApi;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component("queuedCallApiInitiator")
@RequiredArgsConstructor
public class QueuedCallApiInitiator {
    private final QueuedCallSender queuedCallSender;
    private final GenericApplicationContext applicationContext;

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
