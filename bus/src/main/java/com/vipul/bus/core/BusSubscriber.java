package com.vipul.bus.core;

import com.vipul.bus.EventBase;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.Scanners;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class BusSubscriber {

    private BeanFactory beanFactory;
    private Map<Class, List<Method>> subscriberMethods;

    @Autowired
    public BusSubscriber(BeanFactory beanFactory,
                         @Qualifier("subscriberMethods") Map<Class, List<Method>> subscriberMethods) {
        this.beanFactory = beanFactory;
        this.subscriberMethods = subscriberMethods;
    }

    /**
     * This method is called by the event listener
     * @param event
     */
    public void receiveEvent(EventBase event) {
        // code to receive event.

        // Find the event type

        // Get all subscribers for the event type

        // Execute all methods concurrently

        this.subscriberMethods.get(event.getClass()).stream().forEach(method -> {
            try {
                method.invoke(this.beanFactory.getBean(method.getDeclaringClass()), event);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
