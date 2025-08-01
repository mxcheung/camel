import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EndpointResolverUnitTest {

    @Test
    void resolvesArrayOfToUris() {
        String[] inputUris = {
            "kafkaOnPrem:payments",
            "kafkaCloud:analytics",
            "ibmMq:OUTPUT.QUEUE",
            "file:/tmp/archive",
            "direct:log"
        };

        String[] resolvedUris = new String[inputUris.length];
        for (int i = 0; i < inputUris.length; i++) {
            resolvedUris[i] = EndpointResolver.resolve(inputUris[i]);
        }

        assertThat(resolvedUris)
            .hasSize(5)
            .satisfiesExactly(
                uri -> assertThat(uri).startsWith("kafka:payments?brokers=onprem-broker:9093"),
                uri -> assertThat(uri).startsWith("kafka:analytics?brokers=cloud-broker:9092"),
                uri -> assertThat(uri).isEqualTo("ibmmq:queue:OUTPUT.QUEUE?connectionFactory=#mqConnectionFactory"),
                uri -> assertThat(uri).isEqualTo("file:/tmp/archive"),
                uri -> assertThat(uri).isEqualTo("direct:log")
            );
    }
}
