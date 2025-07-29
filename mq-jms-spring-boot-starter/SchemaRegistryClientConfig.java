package com.example.registry;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SchemaRegistryClientConfig {

    @Bean
    public SchemaRegistryClient schemaRegistryClient(SchemaRegistryProperties props) {
        Map<String, String> configs = new HashMap<>();

        if (props.getUsername() != null && props.getPassword() != null) {
            configs.put("basic.auth.credentials.source", "USER_INFO");
            configs.put("schema.registry.basic.auth.user.info", props.getUsername() + ":" + props.getPassword());
        }

        return new CachedSchemaRegistryClient(
                props.getUrl(),
                props.getCapacity(),
                configs
        );
    }

    @Data
    @Builder
    @Component
    @ConfigurationProperties(prefix = "schema-registry")
    public static class SchemaRegistryProperties {
        private String url;
        private String username;
        private String password;
        private int capacity = 10;
    }
}
