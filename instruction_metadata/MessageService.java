import software.amazon.lambda.powertools.logging.Logging;
import software.amazon.lambda.powertools.logging.CorrelationId;
import software.amazon.lambda.powertools.logging.PowertoolsLogger;

import java.util.Map;

public class MessageService {

    private static final PowertoolsLogger logger = PowertoolsLogger.builder()
            .serviceName("MyService")
            .build();

    public void sendMessage(String message, Map<String, Object> instructionMeta) {
        // Log structured message with metadata
        logger.appendKeys(instructionMeta)
              .info("Sent message: {}", message);

        // If you want to clear metadata after logging
        logger.clearKeys();
    }
}
