import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class MetadataLoggingProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(MetadataLoggingProcessor.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("instructionId", exchange.getIn().getHeader("instructionId"));
        metadata.put("status", exchange.getIn().getHeader("status"));
        metadata.put("type", exchange.getIn().getHeader("type"));
        metadata.put("clientId", exchange.getIn().getHeader("clientId"));

        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("@timestamp", java.time.Instant.now().toString());
        logEntry.put("@message", "Processed instruction: " + metadata.get("instructionId"));
        logEntry.put("instruction_metadata", metadata);

        // Log JSON for CloudWatch
        log.info(mapper.writeValueAsString(logEntry));
    }
}
