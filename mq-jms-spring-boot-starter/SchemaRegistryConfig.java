import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaRegistryConfig {

    @Bean
    public SchemaRegistryClient schemaRegistryClient(SchemaRegistryProperties properties) {
        return new CachedSchemaRegistryClient(properties.getUrl(), 10);
    }
}
