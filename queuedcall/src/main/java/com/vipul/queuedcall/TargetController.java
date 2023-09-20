package com.vipul.queuedcall;

import com.vipul.queuedcall.annotation.QueueCalledController;
import com.vipul.queuedcall.annotation.QueueCalledName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;

@QueueCalledController
@Controller
public class TargetController {

    @Autowired
    private Map<String, Method> resultMap;

    public String testMethod(String name) {
        return "hello " + name;
    }

    @QueueCalledName("getHi")
    public String getHiooo(String name) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello from target, " + name;
    }
}
