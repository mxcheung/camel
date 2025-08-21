import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class MySchemaChecker {

    private final SchemaRegistryClient schemaRegistryClient;
    private final String dlqTopic;

    // Keep the last known schema in memory as fallback
    private final AtomicReference<SchemaMetadata> lastKnownDlqSchema = new AtomicReference<>();

    public MySchemaChecker(
            SchemaRegistryClient schemaRegistryClient,
            @Value("${kafka.cloud.topics.dlq}") String dlqTopic) {
        this.schemaRegistryClient = schemaRegistryClient;
        this.dlqTopic = dlqTopic;
    }

    @Cacheable("dlqSchemaCache")
    public SchemaMetadata getDlqSchemaMetadata() {
        try {
            SchemaMetadata schema = schemaRegistryClient.getLatestSchemaMetadata(dlqTopic);
            lastKnownDlqSchema.set(schema);
            return schema;
        } catch (Exception e) {
            SchemaMetadata fallback = lastKnownDlqSchema.get();
            if (fallback != null) {
                // log a warning and return cached copy
                System.err.println("⚠️ Failed to fetch schema from registry, using last known copy: " + e.getMessage());
                return fallback;
            }
            // no fallback available → propagate exception
            throw new RuntimeException("Unable to fetch schema and no last known copy available", e);
        }
    }
}
