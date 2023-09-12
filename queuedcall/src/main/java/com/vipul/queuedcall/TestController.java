package com.vipul.queuedcall;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/bus")
@RequiredArgsConstructor
public class TestController {

    @Autowired
    private TestInterface testInterface;

    @GetMapping("/hello")
    public Object hello() throws ExecutionException, InterruptedException {
        CompletableFuture<String> result = testInterface.getHi("hi" );
        System.out.println("reaching final");
        Object something = result.get();
        return something;
    }
}
