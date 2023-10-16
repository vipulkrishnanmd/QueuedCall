package com.vipul.queuedcall.config;

import com.vipul.queuedcall.annotation.BatchedQueueCalled;
import com.vipul.queuedcall.annotation.QueueCalledController;
import com.vipul.queuedcall.annotation.QueueCalledName;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class TargetConfig {
    @Bean
    public Map<String, Method> queueCalledMethods() {
        Reflections reflections = new Reflections(
                "com", // TODO: get from properties file
                Scanners.TypesAnnotated);

        return reflections.getTypesAnnotatedWith(QueueCalledController.class)
                .stream()
                .map(cl -> Arrays.asList(cl.getDeclaredMethods()))
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(m -> {
                    QueueCalledName annotation = m.getAnnotation(QueueCalledName.class);
                    return annotation == null ? m.getName() : annotation.value();
                }, m -> m));
    }

    @Bean
    public Map<String, Method> batchedQueueCalledMethods() {
        Reflections reflections = new Reflections(
                "com", // TODO: get from properties file
                Scanners.TypesAnnotated);

        return reflections.getTypesAnnotatedWith(QueueCalledController.class)
                .stream()
                .map(cl -> Arrays.asList(cl.getDeclaredMethods()))
                .flatMap(Collection::stream)
                .filter(method -> Arrays.stream(method.getDeclaredAnnotations()).filter(annotation -> annotation instanceof BatchedQueueCalled).collect(Collectors.toList()).size() > 0)
                .collect(Collectors.toMap(m -> {
                    QueueCalledName annotation = m.getAnnotation(QueueCalledName.class);
                    return annotation == null ? m.getName() : annotation.value();
                }, m -> m));
    }
}
