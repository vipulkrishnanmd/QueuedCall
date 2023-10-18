# QueuedCall
A Java Spring-boot library for enabling micro-services to communicate easily through Kafka and other message queues.

## Features
### 1. Enables duplex communication between Java backend services through message queues
Java services can use this library to call other backend services. The request and response are sent through the message queues (e.g. Kafka). This enables asynchronous communication without having the complexity of publishing and subscribing.
   
### 2. Abstracts the message queue interfaces with Feign-like annotated interfaces
The library abstracts the complex message queue interfaces into simple method interfaces, just like Feign simplifies HTTP calls into method calls. Create interfaces in the calling service with library's annotations.
On the target service, annotate the target controller and method. Then the developers can call the annotated interface methods in source service and the library communicates with the service on the other side through the queue.
```
// Interface in the calling service
@QueuedCallApi
public interface RemoteService {
    @QueueCalledTarget("remoteServiceTargetMethodIdentifier")
    public CompletableFuture<String> targetMethodRepresentative(String name);
}
```
```
// Controller in the remote service
@QueueCalledController
@Controller
public class TargetController {
    @QueueCalledName("remoteServiceTargetMethodIdentifier")
    public String targetMethod(String name) {
        return "hello " + name + ", from simpleExample";
    }
```
### 3. Provides optional batched call feature to reduce the number of calls to the target method
Imagine you have a target method in a target service that is called a large number of times by multiple services. For example, a `String getTemperature(String city)` method.
If n calls are coming simultaneously, the library can do internal batching for the request and call the target method only once. If the target method has some params, library passes the params as a `Map<requestId, paramsFromSource>`,
so that they can be processed as batches in the target.
```
// Interface in the calling service
@QueuedCallApi
public interface RemoteService {
    @QueueCalledTarget("getTemperatureForCity")
    public CompletableFuture<String> getTemperature(String city);
}
```
```
// Controller in the remote service
@QueueCalledController
@Controller
public class TargetController {
    @QueueCalledName("getTemperatureForCity")
    public String getTemperatureTargetMethod(Map<String, Object> cities) {
        Map<String, String> results = new HashMap<String, String>;
        for (String id: cities.keySet()) {
            results.put(id, weatherService.getTemp(cities.get(id)));
        }
        return results;
   }
}
```

## How to use
### 1. Enable Queued-Call
Annotate the Spring boot app class with `@EnableQueuedCall`. To enable batching, use `@EnableQueuedCallWithBatching` instead

### 2. Configuration
Do the below configuration in both source and target apps.
- In the application.properties, set value for `queuedcall.root-package` as your applications root package.
- If batching is enabled, set the batch window size using the property `queuedcall.kafka.batched.window-size`. Window size is the time period to which the app will wait aggregating the requests to create a batched request.
- Currently, only Kafka implementation is supported in the library and this implementation uses Spring boot kafka library. Set `spring.kafka.bootstrap-servers` with kafka bootstrap server urls.
- Create a config class and set up listener and sender. Currently, only Kafka implementation is available.
```
@Configuration
@RequiredArgsConstructor
public class SampleConfig {
    private final MessageListenerContainer messageListenerContainer;
    private final KafkaTemplate kafkaTemplate;

    @Bean
    @ConditionalOnMissingBean(QueuedCallSender.class)
    // Auto-configured to use the kafka template
    // But the user can override this config.
    public QueuedCallSender queuedCallSender() {
        return new KafkaQueuedCallSender(kafkaTemplate);
    }
    
    @Bean
    // Note: For batched, use the below commented config instead
    public QueuedCallListener queuedCallListener() {
        return new KafkaQueuedCallListener(messageListenerContainer);
    }

//    @Bean
//    public QueuedCallListener queuedCallListener() {
//        return new KafkaQueuedCallBatchedListener(messageListenerContainer);
//    }
}
```
### 3. Usage
Please see the samples in `src/main/java/com/vipul/queuedcall/sample`. In this example, for simplcity, both source and targets are in the same service.
- TargetController contains the target methods. This controller class and the methods are annotated to mark them as queue called.
- TestController is a controller in the caller side.
- TestInterface is an annotated interface. Annotations clearly identifies the target service to be called and the method.
- When TestController calls methods in TestInterface, the library communicates to the TargetController and returns the result trhough Kafka.

## Building and adding to project
- Build a fat jar using the command `./gradlew fatJar`.
- Copy the jar to your project and add it to you app's gradle dependencies.
- Note: Sample app is not included in the jar file

## Running the sample app
- Run the command `./gradlew bootRun` to run the sample app.
