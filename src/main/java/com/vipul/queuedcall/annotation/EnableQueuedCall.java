package com.vipul.queuedcall.annotation;

import com.vipul.queuedcall.config.LibraryConfig;
import com.vipul.queuedcall.config.TargetConfig;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate the SpringBootApplication class.
 * Enables Queued Call for the app.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DependsOn("queuedCallApiInitiator")
@Import(LibraryConfig.class)
public @interface EnableQueuedCall {
}
