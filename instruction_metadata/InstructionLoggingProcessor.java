import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class InstructionLoggingProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(InstructionLoggingProcessor.class);

    @Override
    public void process(Exchange exchange) {
        // Fetch instruction metadata from header
        Map<String, Object> instructionMeta =
                exchange.getIn().getHeader("instruction_metadata", Map.class);

        // Structured logging like Python's extra
        logger.info("Sent message, instruction_metadata={}", instructionMeta);
    }
}
