@Configuration
@EnableConfigurationProperties(MultiMqProperties.class)
public class MqConfig {

    private final MultiMqProperties multiMqProperties;

    public MqConfig(MultiMqProperties multiMqProperties) {
        this.multiMqProperties = multiMqProperties;
    }

    @Bean
    public Map<String, MQQueueConnectionFactory> connectionFactories() throws JMSException {
        Map<String, MQQueueConnectionFactory> factories = new HashMap<>();

        for (Map.Entry<String, MqProperties> entry : multiMqProperties.getManagers().entrySet()) {
            String key = entry.getKey();
            MqProperties props = entry.getValue();

            MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
            factory.setQueueManager(props.getQueueManager());
            factory.setChannel(props.getChannel());
            factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            factory.setConnectionNameList(props.getConnName());

            factories.put(key, factory);
        }

        return factories;
    }
}
