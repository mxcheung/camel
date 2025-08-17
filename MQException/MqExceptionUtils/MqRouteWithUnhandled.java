// Connectivity issue → retry with backoff
onException(MQException.class)
    .onWhen(exchange -> {
        MQException ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, MQException.class);
        return ex != null && MqExceptionUtils.isConnectivity(ex);
    })
    .maximumRedeliveries(5)
    .redeliveryDelay(2000)
    .backOffMultiplier(2)
    .useExponentialBackOff()
    .log("Retrying due to MQ connectivity issue: reason=${exception.reason}")
    .handled(false);

// Authorisation issue → DLQ
onException(MQException.class)
    .onWhen(exchange -> {
        MQException ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, MQException.class);
        return ex != null && MqExceptionUtils.isAuthorization(ex);
    })
    .process(new DlqWrapperProcessor())
    .to("kafka:myDlqTopic")
    .log("Authorization failure sent to DLQ: reason=${exception.reason}")
    .handled(true);

// All other MQ errors → DLQ
onException(MQException.class)
    .onWhen(exchange -> {
        MQException ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, MQException.class);
        return ex != null && MqExceptionUtils.isUnhandled(ex);
    })
    .process(new DlqWrapperProcessor())
    .to("kafka:myDlqTopic")
    .log("Unhandled MQ exception sent to DLQ: reason=${exception.reason}")
    .handled(true);
