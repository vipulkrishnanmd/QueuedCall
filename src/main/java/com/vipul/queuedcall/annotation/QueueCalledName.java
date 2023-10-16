package com.vipul.queuedcall.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation takes custom name for the annotated
 * target method. If this annotation is not given, it used the
 * method name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface QueueCalledName {
    String value() default "";
}
