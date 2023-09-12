package com.vipul.queuedcall;

import com.vipul.queuedcall.core.QueuedApi;

import java.util.concurrent.CompletableFuture;

@QueuedApi
public interface TestInterface {
    public CompletableFuture<String> getHi(String name);
}
