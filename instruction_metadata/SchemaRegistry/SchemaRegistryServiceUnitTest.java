import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.apache.avro.Schema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SchemaRegistryServiceUnitTest {

    private SchemaRegistryService schemaRegistryService;
    private SchemaRegistryClient mockClient;
    private CacheManager cacheManager;

    @BeforeEach
    void setup() {
        // Create Spring context with only our test beans
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        schemaRegistryService = context.getBean(SchemaRegistryService.class);
        mockClient = context.getBean(SchemaRegistryClient.class);
        cacheManager = context.getBean(CacheManager.class);
    }

    @Test
    void testSchemaCaching() throws Exception {
        int schemaId = 1;
        Schema schema = new Schema.Parser().parse(
                "{\"type\":\"record\",\"name\":\"Test\",\"fields\":[{\"name\":\"f1\",\"type\":\"string\"}]}"
        );

        Mockito.when(mockClient.getById(schemaId)).thenReturn(schema);

        // First call -> hits mock
        String schema1 = schemaRegistryService.getSchema(schemaId);
        assertThat(schema1).contains("Test");

        // Second call -> should be cached
        String schema2 = schemaRegistryService.getSchema(schemaId);
        assertThat(schema2).contains("Test");

        // Verify Schema Registry called only once
        verify(mockClient, times(1)).getById(schemaId);

        // Verify cache populated
        assertThat(cacheManager.getCache("schemas").get(schemaId).get()).isNotNull();
    }

    @Configuration
    @EnableCaching
    static class TestConfig {

        @Bean
        public SchemaRegistryClient schemaRegistryClient() {
            return Mockito.mock(SchemaRegistryClient.class);
        }

        @Bean
        public SchemaRegistryService schemaRegistryService(SchemaRegistryClient client) {
            return new SchemaRegistryService(client);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("schemas");
        }
    }
}
