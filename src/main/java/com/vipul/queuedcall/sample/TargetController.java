package com.vipul.queuedcall.sample;

import com.vipul.queuedcall.annotation.BatchedQueueCalled;
import com.vipul.queuedcall.annotation.QueueCalledController;
import com.vipul.queuedcall.annotation.QueueCalledName;
import org.springframework.stereotype.Controller;

@QueueCalledController
@Controller
public class TargetController {

    @QueueCalledName("simpleExample")
    public String simpleExample(String name) {
        return "hello " + name + ", from simpleExample";
    }

    @QueueCalledName("delayedExample")
    public String thisNameDoesntMatter(String name) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello " + name + ", from delayedExample.";
    }

    // No @QueueCalledName, so it takes the method name.
    @BatchedQueueCalled
    public String batchedExample() {
        return "reaching batched example";
    }
}
