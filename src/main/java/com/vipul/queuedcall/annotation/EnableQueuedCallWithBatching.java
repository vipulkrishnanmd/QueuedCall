package com.vipul.queuedcall.annotation;

import com.vipul.queuedcall.config.LibraryConfig;
import com.vipul.queuedcall.config.TargetConfig;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate the SpringBootApplication class.
 * Enables Queued Call with batching for the app.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DependsOn("queuedCallApiInitiator")
@EnableKafkaStreams
// This config class has the ComponentScan annotation
@Import(LibraryConfig.class)
public @interface EnableQueuedCallWithBatching {
}
