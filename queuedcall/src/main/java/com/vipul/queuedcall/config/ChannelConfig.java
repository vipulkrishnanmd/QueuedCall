package com.vipul.queuedcall.config;

import com.vipul.queuedcall.core.QueuedCallSender;
import com.vipul.queuedcall.kafka.KafkaQueuedCallSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChannelConfig {

    @Value("${queued-call.channel-type:kafka}")
    private String channelType;
    private final KafkaQueuedCallSender kafkaQueuedCallSender;

    @Bean
    public QueuedCallSender queuedCallSender() throws Exception {
        System.out.println(channelType);
        switch (channelType) {
            case "kafka":
                return kafkaQueuedCallSender;
            default:
                throw new Exception("Sender not found for the given channel");
        }
    }
}
