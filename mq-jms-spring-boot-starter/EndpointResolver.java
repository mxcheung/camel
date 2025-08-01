public class EndpointTemplates {
    public static final String ON_PREM_KAFKA_TEMPLATE =
        "kafka:{topic}?brokers=onprem-broker:9093"
        + "&securityProtocol=SSL"
        + "&sslTruststoreLocation=/opt/ssl/truststore.jks"
        + "&sslTruststorePassword=changeit";
}


public class EndpointResolver {

    public static String resolve(String symbolicUri) {
        if (symbolicUri.startsWith("kafkaOnPrem:")) {
            String topic = symbolicUri.substring("kafkaOnPrem:".length());
            return EndpointTemplates.ON_PREM_KAFKA_TEMPLATE.replace("{topic}", topic);
        }
        // Add other resolvers as needed
        throw new IllegalArgumentException("Unknown symbolic URI: " + symbolicUri);
    }
}

// Usage
String symbolic = "kafkaOnPrem:payments-topic";
String resolvedUri = EndpointResolver.resolve(symbolic);

// resolvedUri → kafka:payments-topic?brokers=onprem-broker:9093&securityProtocol=SSL...
