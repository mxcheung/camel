import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchemaRegistryServiceTest {

    private SchemaRegistryClient mockClient;
    private SchemaRegistryService service;

    @BeforeEach
    void setup() {
        mockClient = mock(SchemaRegistryClient.class);
        service = new SchemaRegistryService(mockClient);
    }

    @Test
    void shouldReturnSchemaMetadataOnSuccess() throws Exception {
        String topic = "dlqTopic";
        String subject = topic + "-value";
        SchemaMetadata expected = new SchemaMetadata(1, 1, "{\"type\":\"string\"}", 1, 0, "avro");

        when(mockClient.getLatestSchemaMetadata(subject)).thenReturn(expected);

        SchemaMetadata result = service.getDlqSchemaMeta(topic);

        assertEquals(expected, result);
        verify(mockClient, times(1)).getLatestSchemaMetadata(subject);
    }

    @Test
    void shouldFallbackToCachedSchemaOnFailure() throws Exception {
        String topic = "feedbackTopic";
        String subject = topic + "-value";
        SchemaMetadata cached = new SchemaMetadata(1, 1, "{\"type\":\"string\"}", 1, 0, "avro");

        // First call succeeds -> populates fallback
        when(mockClient.getLatestSchemaMetadata(subject)).thenReturn(cached);
        assertEquals(cached, service.getFeedbackSchemaMeta(topic));

        // Second call fails -> should fallback to cached
        when(mockClient.getLatestSchemaMetadata(subject))
                .thenThrow(new IOException("Schema registry down"));

        SchemaMetadata result = service.getFeedbackSchemaMeta(topic);
        assertEquals(cached, result);
    }

    @Test
    void shouldThrowIfNoFallbackAvailable() throws Exception {
        String topic = "unknownTopic";
        String subject = topic + "-value";

        when(mockClient.getLatestSchemaMetadata(subject))
                .thenThrow(new RestClientException("not found", 404, 404));

        SchemaRegistryServiceException ex = assertThrows(
                SchemaRegistryServiceException.class,
                () -> service.getDlqSchemaMeta(topic)
        );

        assertTrue(ex.getMessage().contains(subject));
    }
}
