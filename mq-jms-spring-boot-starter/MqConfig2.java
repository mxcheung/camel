@Configuration
@EnableConfigurationProperties
public class MqConfig {
  
  @Autowired
  private Map<String, MqProperties> managers;

  @Bean
  public Map<String, MQQueueConnectionFactory> connectionFactories() {
    return managers.entrySet().stream()
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        e -> {
          MqProperties props = e.getValue();
          MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
          cf.setQueueManager(props.getQueueManager());
          cf.setChannel(props.getChannel());
          cf.setConnName(props.getConnName());
          return cf;
        }));
  }
  // Similarly configure JMS ListenerContainerFactory, JMS templates per manager...
}
