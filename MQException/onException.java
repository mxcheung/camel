
onException(IOException.class, JMSException.class, TimeoutException.class)
    .maximumRedeliveries(3)
    .redeliveryDelay(2000)
    .handled(true)
    .to("jms:queue:ERRORS");


onException(IOException.class)
    .maximumRedeliveries(5)
    .redeliveryDelay(1000)
    .handled(true)
    .to("jms:queue:IO_ERROR");

onException(JMSException.class)
    .maximumRedeliveries(2)
    .redeliveryDelay(5000)
    .handled(true)
    .to("jms:queue:JMS_ERROR");
