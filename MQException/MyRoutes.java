import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import com.ibm.msg.client.jms.DetailedJMSException;
import com.ibm.mq.MQException;

public class MyRoutes extends RouteBuilder {

    @Override
    public void configure() {
        // Apply custom MQ exception handling for outbound routes
        configureMqErrorHandling("outbound");

        from("direct:send")
            .routeId("outboundSender")
            .to("jms:queue:MY.QUEUE");
    }

    private void configureMqErrorHandling(String routeType) {

        onException(DetailedJMSException.class, MQException.class)
            .onWhen(exchange -> shouldHandleMqError(exchange, routeType))
            .maximumRedeliveries(routeType.equals("outbound") ? 6 : 2)
            .redeliveryDelay(5000)
            .retryAttemptedLogLevel(org.apache.camel.LoggingLevel.WARN)
            .handled(true)
            .to(routeType.equals("outbound") ? "direct:fallbackOutbound" : "direct:fallbackInbound");
    }

    private boolean shouldHandleMqError(Exchange exchange, String routeType) {
        Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        if (e instanceof DetailedJMSException d) {
            if (d.getLinkedException() instanceof MQException mq) {
                // MQRC_HOST_NOT_AVAILABLE
                if (mq.reasonCode == 2538) {
                    return true;
                }
            }
        } else if (e instanceof MQException mq) {
            if (mq.reasonCode == 2538) {
                return true;
            }
        }
        return false;
    }
}
