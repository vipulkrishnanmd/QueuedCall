package com.vipul.queuedcall.config.kafka;

import com.vipul.queuedcall.QueuedCall;
import com.vipul.queuedcall.QueuedCallBatchedRequest;
import com.vipul.queuedcall.QueuedCallRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.apache.kafka.streams.kstream.internals.TimeWindow;
import org.apache.kafka.streams.kstream.internals.WindowedSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;

@Configuration(proxyBeanMethods = false)
@EnableKafkaStreams
public class KafkaStreamConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(APPLICATION_ID_CONFIG, "streams-app5");
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
         props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
         props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
         props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Object.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KStream<String, Object> kStream(StreamsBuilder streamsBuilder) {
        KStream<String, Object> stream = streamsBuilder.stream("queued-call");
        stream.filter((id, x) -> ((QueuedCall) x).getType().equals("request")).mapValues(x -> {
            System.out.println("reaching here 1"); return x;
        }).groupBy((id, x) -> ((QueuedCallRequest) x).getName())
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(10)))
                //.count()
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
                .map((k,v) -> KeyValue.pair(k.key(), QueuedCallBatchedRequest.builder().batch((ArrayList) v.values().stream().collect(Collectors.toList())).name(k.key()).type("batched-request").build()))
                // note: if we are not doing k.key() here, then in .to() we should give serdes as WindowedSerdes.timeWindowedSerdeFrom(String.class, 10000)
                .mapValues(x -> {
            System.out.println("reaching here 2"); return x;
        })
                .to("queued-call", Produced.with(Serdes.String(), new JsonSerde<>())); //, Produced.with(Serdes.String(), new JsonSerde<>()));
        return stream;
    }
}
