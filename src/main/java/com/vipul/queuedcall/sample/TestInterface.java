package com.vipul.queuedcall.sample;

import com.vipul.queuedcall.annotation.QueueCalledTarget;
import com.vipul.queuedcall.annotation.QueuedCallApi;

import java.util.concurrent.CompletableFuture;

@QueuedCallApi
public interface TestInterface {

    // no @QueueCalledTarget, so it takes this method name as target name
    public CompletableFuture<String> simpleExample(String name);

    @QueueCalledTarget("delayedExample")
    public CompletableFuture<String> callDelayedExample(String name);

    @QueueCalledTarget("batchedExample")
    public CompletableFuture<String> callBatchedExample();
}
