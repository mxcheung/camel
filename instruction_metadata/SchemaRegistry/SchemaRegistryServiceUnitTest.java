import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SchemaRegistryServiceTest {

    private SchemaRegistryClient mockClient;
    private SchemaRegistryService schemaRegistryService;

    @BeforeEach
    void setup() {
        mockClient = Mockito.mock(SchemaRegistryClient.class);
        schemaRegistryService = new SchemaRegistryService(mockClient);
    }

    @Test
    void testGetDlqSchemaMeta_DelegatesToClient() throws Exception {
        String dlqTopic = "orders-dlq";
        String subject = dlqTopic + "-value";

        SchemaMetadata expectedMeta = new SchemaMetadata(1, 5,
                "{\"type\":\"record\",\"name\":\"OrderDlq\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"}]}");

        when(mockClient.getLatestSchemaMetadata(subject)).thenReturn(expectedMeta);

        SchemaMetadata actualMeta = schemaRegistryService.getDlqSchemaMeta(dlqTopic);

        assertThat(actualMeta.getId()).isEqualTo(1);
        assertThat(actualMeta.getVersion()).isEqualTo(5);
        assertThat(actualMeta.getSchema()).contains("OrderDlq");

        // verify interaction with Schema Registry client
        verify(mockClient, times(1)).getLatestSchemaMetadata(subject);
    }
}
