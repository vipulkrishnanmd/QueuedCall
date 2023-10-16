package com.vipul.queuedcall.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Takes a unique name for the target method.
 * If not given, it assumes the target name is same as interface
 * method name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface QueueCalledTarget {
    String value() default "";
}
