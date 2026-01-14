import com.ibm.msg.client.jakarta.jms.JmsConnectionFactory;
import com.ibm.msg.client.jakarta.jms.JmsConstants;
import com.ibm.msg.client.jakarta.jms.JmsFactoryFactory;
import com.ibm.msg.client.jakarta.wmq.common.CommonConstants;
import jakarta.jms.JMSException;

public class WebsphereMqExample {

    private static final String HOST = "localhost";
    private static final int PORT = 1414;
    private static final String QUEUE_MANAGER = "QM1";
    private static final String CHANNEL = "DEV.APP.SVRCONN";
    private static final String QUEUE_NAME = "DEV.QUEUE.V1";

    public static void main(String[] args) throws JMSException {
        var connectionFactory = createConnectionFactory();
        try (var jmsContext = connectionFactory.createContext()) {
            var queue = jmsContext.createQueue(QUEUE_NAME);

            var producer = jmsContext.createProducer();
            producer.send(queue, "Hello, WebSphere MQ!");

            var consumer = jmsContext.createConsumer(queue);
            var receivedMessage = consumer.receiveBody(String.class, 5000);
            System.out.println("Received: " + receivedMessage);
        }
    }

    private static JmsConnectionFactory createConnectionFactory() throws JMSException {
        JmsFactoryFactory factoryOfFactory = JmsFactoryFactory.getInstance(JmsConstants.JAKARTA_WMQ_PROVIDER);
        JmsConnectionFactory factory = factoryOfFactory.createConnectionFactory();
        factory.setStringProperty(CommonConstants.WMQ_HOST_NAME, HOST);
        factory.setIntProperty(CommonConstants.WMQ_PORT, PORT);
        factory.setStringProperty(CommonConstants.WMQ_CHANNEL, CHANNEL);
        factory.setIntProperty(CommonConstants.WMQ_CONNECTION_MODE, CommonConstants.WMQ_CM_CLIENT);
        factory.setStringProperty(CommonConstants.WMQ_QUEUE_MANAGER, QUEUE_MANAGER);
        factory.setStringProperty(CommonConstants.WMQ_APPLICATIONNAME, "example_app");
        factory.setBooleanProperty(CommonConstants.USER_AUTHENTICATION_MQCSP, true);
        factory.setStringProperty(CommonConstants.USERID, "your_app_user");
        return factory;
    }
}
