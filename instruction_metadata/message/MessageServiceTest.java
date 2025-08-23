import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MessageServiceTest {

    private MessageService messageService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        messageService = new MessageService(objectMapper);
    }

    @Test
    void testSendMessage_PutsMetaInMdcAndLogs() {
        Map<String, Object> meta = Map.of(
                "instructionId", 123,
                "status", "ACK",
                "type", "CPAY"
        );

        // MDC should be empty before
        assertThat(MDC.get("instruction_meta")).isNull();

        messageService.sendMessage("Test message", meta);

        // After sendMessage, MDC should be cleared
