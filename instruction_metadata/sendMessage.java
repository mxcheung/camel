import com.fasterxml.jackson.databind.ObjectMapper;

public void sendMessage(String message, Map<String, Object> instructionMeta) {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> logEntry = new HashMap<>();
    logEntry.put("message", message);
    logEntry.put("instruction_metadata", instructionMeta);

    try {
        String jsonLog = mapper.writeValueAsString(logEntry);
        logger.info(jsonLog);
    } catch (Exception e) {
        logger.error("Failed to log message", e);
    }
}
