import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.lambda.AWS2LambdaConstants;

public class KafkaToLambdaRoute extends RouteBuilder {
    @Override
    public void configure() {

        // Kafka → AWS Lambda → Log response
        from("kafka:orders?brokers=localhost:9092&groupId=camel-lambda")
            .routeId("kafka-to-lambda")

            // Prepare Lambda input (could also just pass the Kafka message directly)
            .process(exchange -> {
                String orderEvent = exchange.getIn().getBody(String.class);
                exchange.getIn().setBody("{\"orderEvent\": " + orderEvent + "}");
            })

            // Set invocation type (sync, waits for result)
            .setHeader(AWS2LambdaConstants.INVOCATION_TYPE, constant("RequestResponse"))

            // Call Lambda function
            .to("aws2-lambda://processOrder?operation=invokeFunction&region=ap-southeast-2")

            // Log Lambda response
            .log("Lambda response: ${body}");
    }
}
