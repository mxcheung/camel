🔹 1. One route, parallel consumers (scale via concurrency)

The JMS component supports concurrentConsumers, and Kafka can handle concurrent producers.

```
from("jms:queue:input"
     + "?concurrentConsumers=5")   // 5 competing consumers on the same JMS queue
  .routeId("jms-to-kafka")
  .to("kafka:my-topic"
      + "?brokers=broker1:9092,broker2:9092");
```


🔹 1. One Kafka → JMS route, scaled with concurrency

Kafka consumers can be scaled by partition count + concurrentConsumers.
```
from("kafka:my-topic"
     + "?brokers=broker1:9092,broker2:9092"
     + "&groupId=my-group"
     + "&consumersCount=5")   // 5 concurrent Kafka consumers
  .routeId("kafka-to-jms")
  .to("jms:queue:output");
```


✅ Rule of thumb:

If it’s one Kafka topic → one JMS queue, but faster, use consumersCount.

If it’s many Kafka→JMS mappings, use route templates.

If you need parallelism within a single consumer, use threads().
