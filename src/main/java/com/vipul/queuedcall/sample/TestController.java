package com.vipul.queuedcall.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/sample")
@RequiredArgsConstructor
public class TestController {

    private final TestInterface testInterface;

    @GetMapping("/simple")
    public Object simple() throws ExecutionException, InterruptedException {
        CompletableFuture<String> result = testInterface.simpleExample("world" );
        Object something = result.get();
        return something;
    }

    @GetMapping("/withDelay")
    public Object withDelay() throws ExecutionException, InterruptedException {
        CompletableFuture<String> result = testInterface.callDelayedExample("world" );
        Object something = result.get();
        return something;
    }

    @GetMapping("/batched")
    public Object withoutDelay() throws ExecutionException, InterruptedException {
        CompletableFuture<String> result = testInterface.callBatchedExample();
        Object something = result.get();
        return something;
    }
}
