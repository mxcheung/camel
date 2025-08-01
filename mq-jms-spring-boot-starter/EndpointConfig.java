public class EndpointConfig {
    public static final String ON_PREM_KAFKA_URI = "kafka:topic1?brokers=onprem-broker:9093&securityProtocol=SSL&sslTruststoreLocation=/etc/truststore.jks";
    public static final String CLOUD_KAFKA_URI = "kafka:topic1?brokers=cloud-broker:9092&securityProtocol=SASL_SSL"
        + "&saslMechanism=PLAIN"
        + "&saslJaasConfig=org.apache.kafka.common.security.plain.PlainLoginModule required "
        + "username=\"USERNAME\" password=\"PASSWORD\";";
    public static final String IBM_MQ_URI = "ibmmq:queue:QUEUE1?connectionFactory=#mqConnectionFactory";
}
