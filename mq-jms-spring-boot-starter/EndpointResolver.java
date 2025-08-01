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







public class EndpointResolver {

    private static final String ON_PREM_KAFKA_TEMPLATE =
        "kafka:%s?brokers=onprem-broker:9093"
        + "&securityProtocol=SSL"
        + "&sslTruststoreLocation=/opt/ssl/truststore.jks"
        + "&sslTruststorePassword=changeit";

    private static final String CLOUD_KAFKA_TEMPLATE =
        "kafka:%s?brokers=cloud-broker:9092"
        + "&securityProtocol=SASL_SSL"
        + "&saslMechanism=PLAIN"
        + "&saslJaasConfig=org.apache.kafka.common.security.plain.PlainLoginModule required "
        + "username=\"user\" password=\"pass\";";

    private static final String IBM_MQ_TEMPLATE =
        "ibmmq:queue:%s?connectionFactory=#mqConnectionFactory";

    public static String resolve(String symbolicUri) {
        String[] parts = symbolicUri.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid symbolic URI: " + symbolicUri);
        }

        String type = parts[0];
        String value = parts[1];

        return switch (type) {
            case "kafkaOnPrem" -> String.format(ON_PREM_KAFKA_TEMPLATE, value);
            case "kafkaCloud" -> String.format(CLOUD_KAFKA_TEMPLATE, value);
            case "ibmMq" -> String.format(IBM_MQ_TEMPLATE, value);
            default -> throw new IllegalArgumentException("Unknown symbolic URI type: " + type);
        };
    }
}


String from = EndpointResolver.resolve("kafkaOnPrem:orders");
String to1 = EndpointResolver.resolve("ibmMq:ORDER.Q");
String to2 = EndpointResolver.resolve("kafkaCloud:analytics");

