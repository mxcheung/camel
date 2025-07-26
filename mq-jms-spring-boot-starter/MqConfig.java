package com.example.demo.config;

import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    @Bean
    public ConnectionFactory mqConnectionFactory() throws JMSException {
        MQConnectionFactory cf = new MQConnectionFactory();

        cf.setHostName("localhost");                  // 🔁 Your MQ host
        cf.setPort(1414);                             // 🔁 Your MQ port
        cf.setQueueManager("QM1");                    // 🔁 Your queue manager
        cf.setChannel("DEV.APP.SVRCONN");             // 🔁 Your channel
        cf.setTransportType(WMQConstants.WMQ_CM_CLIENT); // Client mode
        cf.setStringProperty(WMQConstants.USERID, "app");
        cf.setStringProperty(WMQConstants.PASSWORD, "app");

        return cf;
    }
}
