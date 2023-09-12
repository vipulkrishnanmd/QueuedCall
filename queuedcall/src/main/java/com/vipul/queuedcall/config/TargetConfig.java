package com.vipul.queuedcall.config;

import com.vipul.queuedcall.annotation.QueueCalledController;
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
                "com.vipul", // TODO: get from properties file
                Scanners.TypesAnnotated);

        return reflections.getTypesAnnotatedWith(QueueCalledController.class)
                .stream()
                .map(cl -> Arrays.asList(cl.getDeclaredMethods()))
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(m -> m.getName(), m -> m));
    }
}
