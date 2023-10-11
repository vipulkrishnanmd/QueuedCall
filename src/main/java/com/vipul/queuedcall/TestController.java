package com.vipul.queuedcall;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/bus")
@RequiredArgsConstructor
public class TestController {

    private final TestInterface testInterface;

    @GetMapping("/hello")
    public Object hello() throws ExecutionException, InterruptedException {
        CompletableFuture<String> result = testInterface.getHiiiii("hi" );
        System.out.println("reaching final");
        Object something = result.get();
        return something;
    }

    @GetMapping("/headlines")
    public Object headlines() throws ExecutionException, InterruptedException {
        CompletableFuture<String> result = testInterface.headlines();
        System.out.println("reaching final 2");
        Object something = result.get();
        return something;
    }
}
