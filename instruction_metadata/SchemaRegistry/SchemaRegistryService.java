import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SchemaRegistryService {

    private final SchemaRegistryClient client;

    // Local fallback store: subject → SchemaMetadata
    private final Map<String, SchemaMetadata> fallbackSchemas = new ConcurrentHashMap<>();

    public SchemaRegistryService(SchemaRegistryClient client) {
        this.client = client;
    }

    /**
     * Get latest schema metadata for a DLQ topic (subject = topic + "-value").
     */
    @Cacheable(value = "dlqSchemas", key = "#dlqTopic")
    public SchemaMetadata getDlqSchemaMeta(String dlqTopic) {
        return getLatestSchemaMetadata(dlqTopic);
    }

    /**
     * Get latest schema metadata for a Feedback topic (subject = topic + "-value").
     */
    @Cacheable(value = "feedbackSchemas", key = "#feedbackTopic")
    public SchemaMetadata getFeedbackSchemaMeta(String feedbackTopic) {
        return getLatestSchemaMetadata(feedbackTopic);
    }

    // --- Shared helper ---
    private SchemaMetadata getLatestSchemaMetadata(String topic) {
        String subject = topic + "-value";
        try {
            SchemaMetadata schemaMetadata = client.getLatestSchemaMetadata(subject);
            // refresh fallback cache if fetch succeeds
            fallbackSchemas.put(subject, schemaMetadata);
            return schemaMetadata;
        } catch (IOException | RestClientException e) {
            // fall back to last known schema
            SchemaMetadata fallback = fallbackSchemas.get(subject);
            if (fallback != null) {
                return fallback;
            }
            throw new SchemaRegistryServiceException(
                "Failed to fetch schema and no fallback available for subject=" + subject, e
            );
        }
    }
}
