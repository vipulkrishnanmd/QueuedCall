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

## How to
1. Enable Queued-Call
   Annotate the 