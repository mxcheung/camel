import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void shouldUpdateFallbackOnEverySuccess() throws Exception {
        String topic = "dlqTopic2";
        String subject = topic + "-value";

        SchemaMetadata first = new SchemaMetadata(1, 1, "{\"type\":\"string1\"}", 1, 0, "avro");
        SchemaMetadata second = new SchemaMetadata(2, 2, "{\"type\":\"string2\"}", 2, 0, "avro");

        // First fetch -> store first schema in fallback
        when(mockClient.getLatestSchemaMetadata(subject)).thenReturn(first);
        assertEquals(first, service.getDlqSchemaMeta(topic));

        // Second fetch -> should update fallback with second schema
        when(mockClient.getLatestSchemaMetadata(subject)).thenReturn(second);
        assertEquals(second, service.getDlqSchemaMeta(topic));

        // Simulate registry failure -> should return the updated fallback (second schema)
        when(mockClient.getLatestSchemaMetadata(subject)).thenThrow(new IOException("down"));
        assertEquals(second, service.getDlqSchemaMeta(topic));
    }
}
