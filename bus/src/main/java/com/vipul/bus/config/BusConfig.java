package com.vipul.bus.config;

import com.vipul.bus.TestInterface;
import com.vipul.bus.core.BusSubscribe;
import com.vipul.bus.core.QueuedApi;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class BusConfig {

    @Autowired
    private GenericApplicationContext applicationContext;

    @Bean
    public Map<Class, List<Method>> subscriberMethods() {
        Reflections reflections = new Reflections(
                "com.vipul",
                Scanners.MethodsAnnotated);



//        TestInterface proxy = (TestInterface) Proxy.newProxyInstance(
//                TestInterface.class.getClassLoader(), new Class<?>[] {TestInterface.class}, (p, method, methodArgs) -> {
//                    System.out.println("I will post this to kafka: " + method.getName());
//                    return null;
//                });
//        this.applicationContext.registerBean("testtttt", TestInterface.class, ()->proxy);



        Reflections reflectionsTwo = new Reflections(
                "com.vipul",
                Scanners.TypesAnnotated);

        reflectionsTwo.getTypesAnnotatedWith(QueuedApi.class).stream().forEach(type -> {
            Class typ = type;
            System.out.println("reaching one with " + typ.getName());
                Object proxy = Proxy.newProxyInstance(
                       typ.getClassLoader(), new Class<?>[] {typ}, (p, method, methodArgs) -> {
                           System.out.println("I will post this to kafka: " + method.getName());
                           return null;
                       });
               this.applicationContext.registerBean(typ.getName(), typ, () -> typ.cast(proxy));
        });

        return reflections.getMethodsAnnotatedWith(BusSubscribe.class)
                .stream()
                .collect(Collectors.groupingBy(method -> method.getParameterTypes()[0]));
    }

//    @Bean
//    public TestInterface testInterface() {
//           TestInterface proxy = (TestInterface) Proxy.newProxyInstance(
//           TestInterface.class.getClassLoader(), new Class<?>[] {TestInterface.class}, (p, method, methodArgs) -> {
//               System.out.println("I will post this to kafka: " + method.getName());
//               return null;
//           });
//           return proxy;
//    }
}
