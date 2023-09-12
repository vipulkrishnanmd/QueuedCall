package com.vipul.bus.config;

import com.vipul.bus.core.BusSubscribe;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class BusConfig {

    @Bean
    public Map<Class, List<Method>> subscriberMethods() {
        Reflections reflections = new Reflections(
                "com.vipul",
                Scanners.MethodsAnnotated);

        return reflections.getMethodsAnnotatedWith(BusSubscribe.class)
                .stream()
                .collect(Collectors.groupingBy(method -> method.getParameterTypes()[0]));
    }
}
