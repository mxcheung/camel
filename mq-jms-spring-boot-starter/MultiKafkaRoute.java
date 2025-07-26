@Component
public class MultiKafkaRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("kafka-onprem:my-onprem-topic")
            .log("Received from on-prem: ${body}")
            .to("kafka-cloud:my-cloud-topic");

        from("kafka-cloud:another-cloud-topic")
            .log("Received from cloud: ${body}")
            .to("kafka-onprem:another-onprem-topic");
    }
}
