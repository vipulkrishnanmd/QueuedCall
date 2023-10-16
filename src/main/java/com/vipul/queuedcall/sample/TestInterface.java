package com.vipul.queuedcall;

import com.vipul.queuedcall.annotation.QueueCalledTarget;
import com.vipul.queuedcall.annotation.QueuedCallApi;

import java.util.concurrent.CompletableFuture;

@QueuedCallApi
public interface TestInterface {
    @QueueCalledTarget("getHi")
    public CompletableFuture<String> getHiiiii(String name);

    @QueueCalledTarget("todaysHeadlines")
    public CompletableFuture<String> headlines();
}
