package com.vipul.queuedcall.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ResultStore {
    public static Map<String, CompletableFuture<Object>> resultMap = new HashMap<>();
}
