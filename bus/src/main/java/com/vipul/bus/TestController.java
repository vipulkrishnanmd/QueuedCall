package com.vipul.bus;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bus")
@RequiredArgsConstructor
public class TestController {

    @Autowired
    private TestInterface testInterface;

    @GetMapping("/hello")
    public String hello() {
        testInterface.getHi("hi");
        return "Hello";
    }
}
