import com.ibm.mq.MQException;
import com.ibm.msg.client.jms.DetailedJMSException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class MqRetryFallbackRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Store first failure time for timeout-based retry
        onException(DetailedJMSException.class)
            .onWhen(exchange -> {
                Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                if (e instanceof DetailedJMSException d) {
                    if (d.getLinkedException() instanceof MQException mq) {
                        return mq.reasonCode == 2538; // MQRC_HOST_NOT_AVAILABLE
                    }
                }
                return false;
            })
            .process(exchange -> {
                Long firstFailure = exchange.getProperty("firstFailureTime", Long.class);
                if (firstFailure == null) {
                    firstFailure = System.currentTimeMillis();
                    exchange.setProperty("firstFailureTime", firstFailure);
                }

                long elapsed = System.currentTimeMillis() - firstFailure;
                if (elapsed > 30000) { // >30 seconds
                    exchange.setProperty("useFallback", true);
                }
            })
            .maximumRedeliveries(1000)  // high enough so we rely on time check
            .redeliveryDelay(2000)      // 2s between retries
            .retryAttemptedLogLevel(LoggingLevel.WARN)
            .handled(true)              // prevent exception after final decision
            .choice()
                .when(exchange -> Boolean.TRUE.equals(exchange.getProperty("useFallback")))
                    .to("direct:fallback")
                .otherwise()
                    .to("direct:retry")
            .end();

        // Main JMS send route
        from("direct:sendToMq")
            .routeId("SendToMq")
            .log("Sending to MQ: ${body}")
            .to("jms:queue:MY.QUEUE?requestTimeout=5000");

        // Retry path - sends back to MQ
        from("direct:retry")
            .log("Retrying to send to MQ...")
            .to("jms:queue:MY.QUEUE?requestTimeout=5000");

        // Fallback path
        from("direct:fallback")
            .log(LoggingLevel.ERROR, "MQ unavailable after 30s, sending to fallback queue")
            .to("jms:queue:FALLBACK.QUEUE");
    }
}
