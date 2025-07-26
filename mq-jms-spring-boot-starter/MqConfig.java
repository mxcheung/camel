import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import jakarta.jms.ConnectionFactory;

@Bean
public ConnectionFactory connectionFactory() throws JMSException {
    MQConnectionFactory cf = new MQConnectionFactory();
    cf.setHostName("your-host");
    cf.setPort(1414);
    cf.setQueueManager("QM1");
    cf.setChannel("DEV.APP.SVRCONN");
    cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
    return cf;
}
