import com.ibm.mq.MQException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class MqRetryRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Global error handler for all routes
        errorHandler(defaultErrorHandler()
            .maximumRedeliveries(0) // no default retries unless specified
            .redeliveryDelay(0)
        );

        // Targeted handler for MQRC_HOST_NOT_AVAILABLE
        onException(MQException.class)
            .onWhen(exchange -> {
                MQException e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, MQException.class);
                return e != null && e.reasonCode == 2538; // MQRC_HOST_NOT_AVAILABLE
            })
            .maximumRedeliveries(6)    // e.g., retry for ~30 seconds
            .redeliveryDelay(5000)     // 5 seconds between retries
            .retryAttemptedLogLevel(LoggingLevel.WARN)
            .log("Retrying due to MQ host not available...")
            .handled(false);           // still fail if retries exhausted

        from("direct:publish")
            .routeId("PublishToMq")
            .to("jms:queue:MY.QUEUE?requestTimeout=5000")
            .log("Message sent to MQ successfully");

    }
}
