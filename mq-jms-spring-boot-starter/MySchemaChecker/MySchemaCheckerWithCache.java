import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class MySchemaChecker {

    private final SchemaRegistryClient schemaRegistryClient;
    private final String dlqTopic;

    public MySchemaChecker(
            SchemaRegistryClient schemaRegistryClient,
            @Value("${kafka.cloud.topics.dlq}") String dlqTopic) {
        this.schemaRegistryClient = schemaRegistryClient;
        this.dlqTopic = dlqTopic;
    }

    @Cacheable("dlqSchemaCache")
    public SchemaMetadata getDlqSchemaMetadata() throws Exception {
        // Called only on cache miss / expiry
        return schemaRegistryClient.getLatestSchemaMetadata(dlqTopic);
    }
}
