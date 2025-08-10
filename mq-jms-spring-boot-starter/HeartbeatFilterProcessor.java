import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartbeatFilterProcessor implements Processor {
    private static final Logger log = LoggerFactory.getLogger(HeartbeatFilterProcessor.class);

    @Override
    public void process(Exchange exchange) {
        String type = exchange.getIn().getHeader("messageType", String.class);

        if ("HEARTBEAT".equalsIgnoreCase(type)) {
            log.info("Skipping heartbeat message from route [{}], body: {}",
                    exchange.getFromRouteId(),
                    exchange.getIn().getBody(String.class));

            // New way to stop further processing in Camel 3.20+
            exchange.setRouteStop(true);
        }
    }
}
