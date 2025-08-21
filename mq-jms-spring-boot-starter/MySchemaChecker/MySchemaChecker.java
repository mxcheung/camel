@Component
public class MySchemaChecker {

    private final SchemaRegistryClient schemaRegistryClient;

    public MySchemaChecker(
        SchemaRegistryClient schemaRegistryClient,
        @Value("${kafka.cloud.topics.dlq}") String dlqTopic) {

        this.schemaRegistryClient = schemaRegistryClient;

        // resolved here during bean construction
        SchemaMetadata metadata = schemaRegistryClient.getLatestSchemaMetadata(dlqTopic);
        // ... do something with metadata
    }
}
