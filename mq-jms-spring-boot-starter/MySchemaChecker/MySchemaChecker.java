import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MySchemaChecker {

    private final SchemaMetadata dlqSchemaMetadata;

    public MySchemaChecker(
            SchemaRegistryClient schemaRegistryClient,
            @Value("${kafka.cloud.topics.dlq}") String dlqTopic) throws Exception {

        // resolve schema at construction
        this.dlqSchemaMetadata = schemaRegistryClient.getLatestSchemaMetadata(dlqTopic);
    }

    public SchemaMetadata getDlqSchemaMetadata() {
        return dlqSchemaMetadata;
    }
}
