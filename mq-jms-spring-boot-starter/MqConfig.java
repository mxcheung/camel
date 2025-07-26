import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import jakarta.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {
        MQConnectionFactory factory = new MQConnectionFactory();
        factory.setQueueManager("QM1");
        factory.setChannel("DEV.APP.SVRCONN");
        factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        factory.setHostName("localhost");
        factory.setPort(1414);
        factory.setStringProperty(WMQConstants.USERID, "app");
        factory.setStringProperty(WMQConstants.PASSWORD, "passw0rd");
        return factory;
    }
}
