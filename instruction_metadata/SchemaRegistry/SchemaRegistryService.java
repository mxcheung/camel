import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SchemaRegistryServiceTest {

    private SchemaRegistryClient mockClient;
    private SchemaRegistryService schemaRegistryService;

    @BeforeEach
    void setup() {
        mockClient = mock(SchemaRegistryClient.class);
        schemaRegistryService = new SchemaRegistryService(mockClient);
    }

    @Test
    void testGetDlqSchemaMeta() throws Exception {
        when(mockClient.getLatestSchemaMetadata("orders-dlq-value"))
                .thenReturn(new SchemaMetadata(1, 1, "{\"type\":\"record\",\"name\":\"DlqOrder\"}"));

        SchemaMetadata meta = schemaRegistryService.getDlqSchemaMeta("orders-dlq");

        assertThat(meta.getId()).isEqualTo(1);
        assertThat(meta.getSchema()).contains("DlqOrder");

        verify(mockClient, times(1)).getLatestSchemaMetadata("orders-dlq-value");
    }

    @Test
    void testGetFeedbackSchemaMeta() throws Exception {
        when(mockClient.getLatestSchemaMetadata("orders-feedback-value"))
                .thenReturn(new SchemaMetadata(2, 3, "{\"type\":\"record\",\"name\":\"FeedbackOrder\"}"));

        SchemaMetadata meta = schemaRegistryService.getFeedbackSchemaMeta("orders-feedback");

        assertThat(meta.getId()).isEqualTo(2);
        assertThat(meta.getVersion()).isEqualTo(3);
        assertThat(meta.getSchema()).contains("FeedbackOrder");

        verify(mockClient, times(1)).getLatestSchemaMetadata("orders-feedback-value");
    }
}
