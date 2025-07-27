import org.apache.camel.CamelContext;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean(name = "kafka-onprem")
    public KafkaComponent kafkaOnPrem(CamelContext context, KafkaClustersConfig clustersConfig) {
        KafkaClusterProperties props = clustersConfig.getOnprem();

        KafkaConfiguration config = new KafkaConfiguration();
        config.setBrokers(props.getBrokers());
        config.setClientId(props.getClientId());
        config.setGroupId(props.getGroupId());

        KafkaComponent component = new KafkaComponent();
        component.setConfiguration(config);
        context.addComponent("kafka-onprem", component);
        return component;
    }

    @Bean(name = "kafka-cloud")
    public KafkaComponent kafkaCloud(CamelContext context, KafkaClustersConfig clustersConfig) {
        KafkaClusterProperties props = clustersConfig.getCloud();

        KafkaConfiguration config = new KafkaConfiguration();
        config.setBrokers(props.getBrokers());
        config.setClientId(props.getClientId());
        config.setGroupId(props.getGroupId());
        config.setSecurityProtocol(props.getSecurityProtocol());
        config.setSaslMechanism(props.getSaslMechanism());
        config.setSaslJaasConfig(props.getSaslJaasConfig());

        KafkaComponent component = new KafkaComponent();
        component.setConfiguration(config);
        context.addComponent("kafka-cloud", component);
        return component;
    }
}
