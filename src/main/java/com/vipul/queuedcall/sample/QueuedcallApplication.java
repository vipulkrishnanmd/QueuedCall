package com.vipul.queuedcall.sample;

import com.vipul.queuedcall.annotation.EnableQueuedCall;
import com.vipul.queuedcall.annotation.EnableQueuedCallWithBatching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// use @EnableQueuedCallWithBatching instead of @EnableQueuedCall for batching
@EnableQueuedCall
public class QueuedcallApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueuedcallApplication.class, args);
    }

}
