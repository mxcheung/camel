import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.apache.avro.Schema;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SchemaRegistryService {

    private final SchemaRegistryClient client;

    public SchemaRegistryService() {
        this.client = new CachedSchemaRegistryClient("http://localhost:8081", 1000);
    }

    @Cacheable(value = "schemas", key = "#schemaId")
    public String getSchema(int schemaId) {
        try {
            Schema schema = client.getById(schemaId);
            return schema.toString(true); // pretty print
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch schema for id=" + schemaId, e);
        }
    }

    public int getSchemaId(String subject, String schema) {
        try {
            Schema avroSchema = new Schema.Parser().parse(schema);
            return client.register(subject, avroSchema);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register schema for subject=" + subject, e);
        }
    }
}
