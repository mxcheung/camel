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

    @Cacheable(value = "dlqSchemas", key = "#dlqTopic")
    public SchemaMetadata getDlqSchemaMeta(String dlqTopic) {
        String subject = dlqTopic + "-value";
        try {
            return client.getLatestSchemaMetadata(subject);
        } catch (IOException | RestClientException e) {
            throw new SchemaRegistryServiceException("Failed to fetch latest schema metadata for subject=" + subject, e);
        }
    }
}
