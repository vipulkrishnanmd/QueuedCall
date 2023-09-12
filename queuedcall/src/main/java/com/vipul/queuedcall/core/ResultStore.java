package com.vipul.queuedcall.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ResultStore {
    public static Map<String, CompletableFuture<Object>> resultMap = new HashMap<>();
}
