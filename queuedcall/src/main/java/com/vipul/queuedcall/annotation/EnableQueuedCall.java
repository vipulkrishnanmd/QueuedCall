package com.vipul.queuedcall.annotation;

import org.springframework.context.annotation.DependsOn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DependsOn("queuedCallEngine")
public @interface EnableQueuedCall {
}
