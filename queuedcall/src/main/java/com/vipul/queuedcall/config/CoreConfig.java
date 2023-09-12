package com.vipul.queuedcall.config;

import com.vipul.queuedcall.QueuedCallRequest;
import com.vipul.queuedcall.core.QueueCalledController;
import com.vipul.queuedcall.core.QueuedApi;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class CoreConfig {
    private final GenericApplicationContext applicationContext;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Bean
    public Map<String, Method> queueCalledMethods() {
        Reflections reflections = new Reflections(
                "com.vipul", // TODO: get from properties file
                Scanners.TypesAnnotated);

        return reflections.getTypesAnnotatedWith(QueueCalledController.class)
                .stream()
                .map(cl -> Arrays.asList(cl.getDeclaredMethods()))
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(m -> m.getName(), m -> m));
    }

    @PostConstruct
    public void registerQueuedApis() {
        Reflections reflectionsTwo = new Reflections(
                "com.vipul", // TODO: get from properties file
                Scanners.TypesAnnotated);

        reflectionsTwo.getTypesAnnotatedWith(QueuedApi.class).stream().forEach(type -> {
            Class typ = type;
                Object proxy = Proxy.newProxyInstance(
                       typ.getClassLoader(), new Class<?>[] {typ}, (p, method, methodArgs) -> {
                           String id = UUID.randomUUID().toString();
                            QueuedCallRequest request = QueuedCallRequest.builder()
                                    .id(id)
                                    .type("request")
                                    .name(method.getName())
                                    .paramTypes(method.getParameterTypes())
                                    .args(methodArgs)
                                    .build();
                            kafkaTemplate.send("queued-call", request);
                            CompletableFuture<Object> result = new CompletableFuture<>();
                            ResultStore.resultMap.put(id, result);
                            return result;
                       });
               this.applicationContext.registerBean(typ.getName(), typ, () -> typ.cast(proxy));
        });
    }
}
