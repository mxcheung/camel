Invoke aws lamabda from camel


# Kafka → Camel → AWS Lambda Integration

This project demonstrates how to use **Apache Camel** with the **AWS2-Lambda component** to process events from **Kafka** by invoking an **AWS Lambda function** written in Python.

---

## 📌 Architecture

1. **Kafka** publishes events (e.g., `orders` topic).
2. **Camel route** consumes events from Kafka.
3. **Camel aws2-lambda component** invokes an AWS Lambda function (`processOrder`).
4. **Lambda** processes the event and returns a response.
5. **Camel** logs or routes the Lambda response.

---

## 🔹 Camel Route (Java DSL)

```java
from("kafka:orders?brokers=localhost:9092&groupId=camel-lambda")
    .routeId("kafka-to-lambda")

    // Prepare Lambda input
    .process(exchange -> {
        String orderEvent = exchange.getIn().getBody(String.class);
        exchange.getIn().setBody("{\"orderEvent\": " + orderEvent + "}");
    })

    // Sync invocation (waits for result)
    .setHeader("CamelAwsLambdaInvocationType", constant("RequestResponse"))

    // Invoke Lambda
    .to("aws2-lambda://processOrder?operation=invokeFunction&region=ap-southeast-2")

    // Log Lambda response
    .log("Lambda response: ${body}");
