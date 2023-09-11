package com.vipul.bus;

import com.vipul.bus.core.BusSubscribe;
import org.springframework.stereotype.Component;

@Component
public class TestListener {

    @BusSubscribe()
    public void test(Test test) {
        System.out.println("Reaching the new listener with: " + test.getField());
    }
}
