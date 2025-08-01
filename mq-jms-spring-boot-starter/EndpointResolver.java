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





import java.util.List;
import java.util.stream.Collectors;

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

    private static final Set<String> SYMBOLIC_SCHEMES = Set.of("kafkaOnPrem", "kafkaCloud", "ibmMq");

    public static String resolve(String uri) {
        String[] parts = uri.split(":", 2);
        if (parts.length != 2 || !SYMBOLIC_SCHEMES.contains(parts[0])) {
            return uri; // passthrough
        }

        String type = parts[0];
        String value = parts[1];

        return switch (type) {
            case "kafkaOnPrem" -> String.format(ON_PREM_KAFKA_TEMPLATE, value);
            case "kafkaCloud" -> String.format(CLOUD_KAFKA_TEMPLATE, value);
            case "ibmMq"      -> String.format(IBM_MQ_TEMPLATE, value);
            default           -> uri;
        };
    }

    // ✅ New method to resolve a List of URIs
    public static List<String> resolveAll(List<String> uris) {
        return uris.stream()
            .map(EndpointResolver::resolve)
            .collect(Collectors.toList());
    }
}




String from = EndpointResolver.resolve("kafkaOnPrem:orders");
String to1 = EndpointResolver.resolve("ibmMq:ORDER.Q");
String to2 = EndpointResolver.resolve("kafkaCloud:analytics");














import java.util.List;
import java.util.Set;

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

    private static final Set<String> SYMBOLIC_SCHEMES = Set.of("kafkaOnPrem", "kafkaCloud", "ibmMq");

    public static String resolve(String uri) {
        String[] schemeAndRest = uri.split(":", 2);
        if (schemeAndRest.length != 2 || !SYMBOLIC_SCHEMES.contains(schemeAndRest[0])) {
            return uri; // passthrough
        }

        String scheme = schemeAndRest[0];
        String rest = schemeAndRest[1];

        String baseValue;
        String extraParams = "";

        int ampIndex = rest.indexOf('&');
        if (ampIndex >= 0) {
            baseValue = rest.substring(0, ampIndex);
            extraParams = rest.substring(ampIndex + 1);
        } else {
            baseValue = rest;
        }

        String resolved = switch (scheme) {
            case "kafkaOnPrem" -> String.format(ON_PREM_KAFKA_TEMPLATE, baseValue);
            case "kafkaCloud"  -> String.format(CLOUD_KAFKA_TEMPLATE, baseValue);
            case "ibmMq"       -> String.format(IBM_MQ_TEMPLATE, baseValue);
            default            -> uri;
        };

        if (!extraParams.isBlank()) {
            resolved += "&" + extraParams;
        }

        return resolved;
    }

    public static List<String> resolveAll(List<String> uris) {
        return uris.stream()
            .map(EndpointResolver::resolve)
            .toList();
    }
}
