package com.vipul.queuedcall.kafka;

import com.vipul.queuedcall.config.kafka.KafkaTopicConfig;
import com.vipul.queuedcall.core.QueuedCallType;
import com.vipul.queuedcall.model.QueuedCall;
import com.vipul.queuedcall.model.QueuedCallBatchedRequest;
import com.vipul.queuedcall.model.QueuedCallRequest;
import com.vipul.queuedcall.core.QueuedCallBatchedListener;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerde;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class KafkaQueuedCallBatchedListener extends QueuedCallBatchedListener {
    private final MessageListenerContainer container;

    @Autowired
    private StreamsBuilder streamsBuilder;

    @Autowired
    @Qualifier("queueCalledMethods")
    private Map<String, Method> queueCalledMethods;

    @Value("${queuedcall.kafka.batched.window-size:1000}")
    private Integer windowSize;

    @PostConstruct
    public void listen() {
        // Initialise stream processing
        KStream<String, Object> stream = streamsBuilder.stream(KafkaTopicConfig.TOPIC_NAME);
        stream.filter((id, x) -> ((QueuedCall) x).getType().equals(QueuedCallType.REQUEST))
                .filter((id, x) -> (queueCalledMethods.keySet().contains(((QueuedCallRequest) x).getName())))
                .groupBy((id, x) -> ((QueuedCallRequest) x).getName())
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(this.windowSize)))
                .aggregate(
                        () -> new HashMap<String, Object>(),
                        (key, value, aggregate) -> {
                            aggregate.put(((QueuedCallRequest) value).getId(), value);
                            return aggregate;
                        },
                        Materialized.with(Serdes.String(), new JsonSerde<>(HashMap.class))
                )
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .map((k,v) -> KeyValue.pair(k.key(),
                        QueuedCallBatchedRequest.builder()
                                .batch((ArrayList) v.values().stream().collect(Collectors.toList()))
                                .name(k.key())
                                .type(QueuedCallType.BATCHED_REQUEST)
                                .build()))
                // note: if we are not doing k.key() here, then in .to() we should give serdes as
                // WindowedSerdes.timeWindowedSerdeFrom(String.class, 10000)
                .to(KafkaTopicConfig.TOPIC_NAME, Produced.with(Serdes.String(), new JsonSerde<>()));

        // set up message listener
        container.setupMessageListener(
                (MessageListener<String, QueuedCall>) (d) -> this.processQueuedCall(d.value()));
    }
}
