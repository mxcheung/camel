package com.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EndpointResolverResolveAllTest {

    @Test
    void resolvesAllUrisInList() {
        // Given
        List<String> inputUris = List.of(
            "kafkaOnPrem:payments",
            "kafkaCloud:analytics",
            "ibmMq:OUTPUT.QUEUE",
            "file:/tmp/archive",
            "direct:notify"
        );

        // When
        List<String> resolved = EndpointResolver.resolveAll(inputUris);

        // Then
        assertThat(resolved)
            .hasSize(5)
            .satisfiesExactly(
                uri -> assertThat(uri)
                    .startsWith("kafka:payments?brokers=onprem-broker:9093")
                    .contains("securityProtocol=SSL"),
                uri -> assertThat(uri)
                    .startsWith("kafka:analytics?brokers=cloud-broker:9092")
                    .contains("saslMechanism=PLAIN"),
                uri -> assertThat(uri)
                    .isEqualTo("ibmmq:queue:OUTPUT.QUEUE?connectionFactory=#mqConnectionFactory"),
                uri -> assertThat(uri)
                    .isEqualTo("file:/tmp/archive"),
                uri -> assertThat(uri)
                    .isEqualTo("direct:notify")
            );
    }

    @Test
    void handlesEmptyList() {
        List<String> resolved = EndpointResolver.resolveAll(List.of());
        assertThat(resolved).isEmpty();
    }

    @Test
    void preservesOrderOfUris() {
        List<String> inputUris = List.of(
            "direct:start",
            "kafkaCloud:log",
            "file:/tmp/logs"
        );

        List<String> resolved = EndpointResolver.resolveAll(inputUris);

        assertThat(resolved)
            .hasSize(3)
            .element(0).isEqualTo("direct:start");
        assertThat(resolved.get(1)).startsWith("kafka:log?brokers=cloud-broker:9092");
        assertThat(resolved.get(2)).isEqualTo("file:/tmp/logs");
    }
}
