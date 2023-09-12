package com.vipul.queuedcall;

import com.vipul.queuedcall.annotation.QueuedCallApi;

import java.util.concurrent.CompletableFuture;

@QueuedCallApi
public interface TestInterface {
    public CompletableFuture<String> getHi(String name);
}
