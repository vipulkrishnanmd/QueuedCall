package com.vipul.bus;

import com.vipul.bus.core.BusSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private BusSubscriber busSubscriber;

    @Autowired
    public KafkaListeners(BusSubscriber busSubscriber) {
        this.busSubscriber = busSubscriber;
    }

    @KafkaListener(
            topics = "messaging-bus",
            groupId = "one",
            containerFactory = "factory"
    )
    void listener(EventBase data) {
        busSubscriber.receiveEvent(data);
    }
}
