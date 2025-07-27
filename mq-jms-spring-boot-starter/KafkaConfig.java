import org.apache.camel.CamelContext;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    @ConfigurationProperties(prefix = "camel.component.kafka-onprem")
    public KafkaConfiguration kafkaOnPremConfiguration() {
        return new KafkaConfiguration();
    }

    @Bean(name = "kafka-onprem")
    public KafkaComponent kafkaOnPrem(CamelContext context, KafkaConfiguration kafkaOnPremConfiguration) {
        KafkaComponent kafkaComponent = new KafkaComponent();
        kafkaComponent.setConfiguration(kafkaOnPremConfiguration);
        context.addComponent("kafka-onprem", kafkaComponent);
        return kafkaComponent;
    }

    @Bean
    @ConfigurationProperties(prefix = "camel.component.kafka-cloud")
    public KafkaConfiguration kafkaCloudConfiguration() {
        return new KafkaConfiguration();
    }

    @Bean(name = "kafka-cloud")
    public KafkaComponent kafkaCloud(CamelContext context, KafkaConfiguration kafkaCloudConfiguration) {
        KafkaComponent kafkaComponent = new KafkaComponent();
        kafkaComponent.setConfiguration(kafkaCloudConfiguration);
        context.addComponent("kafka-cloud", kafkaComponent);
        return kafkaComponent;
    }
}
