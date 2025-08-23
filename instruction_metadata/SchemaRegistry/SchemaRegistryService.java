import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SchemaRegistryService {

    private final SchemaRegistryClient client;

    public SchemaRegistryService(SchemaRegistryClient client) {
        this.client = client;
    }

    /**
     * Get latest schema metadata for a DLQ topic (subject = topic + "-value").
     */
    @Cacheable(value = "dlqSchemas", key = "#dlqTopic")
    public SchemaMetadata getDlqSchemaMeta(String dlqTopic) {
        return getLatestSchemaMetadata(dlqTopic, "dlqSchemas");
    }

    /**
     * Get latest schema metadata for a Feedback topic (subject = topic + "-value").
     */
    @Cacheable(value = "feedbackSchemas", key = "#feedbackTopic")
    public SchemaMetadata getFeedbackSchemaMeta(String feedbackTopic) {
        return getLatestSchemaMetadata(feedbackTopic, "feedbackSchemas");
    }

    // --- Shared helper ---
    private SchemaMetadata getLatestSchemaMetadata(String topic, String cacheName) {
        String subject = topic + "-value";
        try {
            return client.getLatestSchemaMetadata(subject);
        } catch (IOException | RestClientException e) {
            throw new SchemaRegistryServiceException("Failed to fetch latest schema metadata for subject="
