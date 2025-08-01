import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndpointResolverTest {

    @Test
    @DisplayName("Should resolve kafkaOnPrem symbolic URI")
    void testKafkaOnPremUriResolution() {
        String input = "kafkaOnPrem:orders";
        String expectedStart = "kafka:orders?brokers=onprem-broker:9093";
        String resolved = EndpointResolver.resolve(input);
        assertTrue(resolved.startsWith(expectedStart));
    }

    @Test
    @DisplayName("Should resolve kafkaCloud symbolic URI")
    void testKafkaCloudUriResolution() {
        String input = "kafkaCloud:events";
        String resolved = EndpointResolver.resolve(input);
        assertTrue(resolved.startsWith("kafka:events?brokers=cloud-broker:9092"));
        assertTrue(resolved.contains("saslMechanism=PLAIN"));
    }

    @Test
    @DisplayName("Should resolve ibmMq symbolic URI")
    void testIbmMqUriResolution() {
        String input = "ibmMq:MY.QUEUE";
        String resolved = EndpointResolver.resolve(input);
        assertEquals("ibmmq:queue:MY.QUEUE?connectionFactory=#mqConnectionFactory", resolved);
    }

    @Test
    @DisplayName("Should pass through standard Camel URI (file:)")
    void testFileUriPassthrough() {
        String input = "file:/tmp/output";
        String resolved = EndpointResolver.resolve(input);
        assertEquals(input, resolved);
    }

    @Test
    @DisplayName("Should pass through standard Camel URI (direct:)")
    void testDirectUriPassthrough() {
        String input = "direct:start";
        String resolved = EndpointResolver.resolve(input);
        assertEquals(input, resolved);
    }

    @Test
    @DisplayName("Should throw for malformed symbolic URI")
    void testInvalidSymbolicUri() {
        String input = "kafkaOnPrem"; // missing `:topic`
        assertThrows(IllegalArgumentException.class, () -> EndpointResolver.resolve(input));
    }

    @Test
    @DisplayName("Should throw for unknown symbolic type")
    void testUnknownSymbolicPrefix() {
        String input = "unknownScheme:something";
        assertEquals("unknownScheme:something", EndpointResolver.resolve(input));
    }
}
