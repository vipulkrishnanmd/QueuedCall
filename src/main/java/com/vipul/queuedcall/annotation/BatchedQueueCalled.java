package com.vipul.queuedcall.annotation;

/**
 * Used to annotate the target method to show that the method
 * works with Queued Call in batched mode.
 * Annotated method should take should take a Map<String, Object[]> as argument
 * (if it has any arguments) and return Map<String, Object>, where the key of the
 * maps is the id of the request.
 */
public @interface BatchedQueueCalled {
}
