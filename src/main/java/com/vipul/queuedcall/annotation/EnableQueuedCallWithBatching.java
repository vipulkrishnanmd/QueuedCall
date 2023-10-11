package com.vipul.queuedcall.annotation;

import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DependsOn("queuedCallApiInitiator")
@EnableKafkaStreams
public @interface EnableQueuedCallWithBatching {
}
