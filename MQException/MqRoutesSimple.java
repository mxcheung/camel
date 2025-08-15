import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import com.ibm.msg.client.jms.DetailedJMSException;
import com.ibm.mq.MQException;

public class MqRoutesSimple extends RouteBuilder {

    @Override
    public void configure() {

        // Apply centralized MQ error handling
        configureMqExceptionHandler();

        from("direct:send")
            .routeId("OutboundRoute")
            .to("jms:queue:MY.QUEUE");

        from("direct:fallbackOutbound")
            .log(LoggingLevel.ERROR, "MQ unavailable, routing to fallback queue")
            .to("jms:queue:FALLBACK.QUEUE");
    }

    private void configureMqExceptionHandler() {
        onException(DetailedJMSException.class, MQException.class)
            .onWhen(this::isMqHostNotAvailable)
            .maximumRedeliveries(6)         // retry 6 times
            .redeliveryDelay(5000)          // 5s delay between retries
            .retryAttemptedLogLevel(LoggingLevel.WARN)
            .handled(true)
            .to("direct:fallbackOutbound"); // fallback route
    }

    // Helper function to check MQRC_HOST_NOT_AVAILABLE
    private boolean isMqHostNotAvailable(Exchange exchange) {
        Exception ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        if (ex instanceof DetailedJMSException d) {
            return d.getLinkedException() instanceof MQException mq && mq.reasonCode == 2538;
        } else if (ex instanceof MQException mq) {
            return mq.reasonCode == 2538;
        }
        return false;
    }
}
