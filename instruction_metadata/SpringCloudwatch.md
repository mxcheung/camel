Option 1: Using Logback JSON encoder

Add Maven dependency for JSON logging:
```
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>
```

Then, create logback-spring.xml in src/main/resources:
```
<configuration>
    <appender name="CLOUDWATCH" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
    </appender>

    <root level="INFO">
        <appender-ref ref="CLOUDWATCH"/>
    </root>
</configuration>
```





```
<configuration>
    <appender name="CLOUDWATCH" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <!-- Timestamp -->
                <timestamp/>
                <!-- Log level -->
                <logLevel/>
                <!-- Logger name -->
                <loggerName/>
                <!-- Thread -->
                <threadName/>
                <!-- Human-readable message -->
                <message/>
                <!-- Custom structured fields -->
                <jsonProvider class="net.logstash.logback.composite.loggingevent.GlobalCustomFieldsJsonProvider">
                    <customFields>{"service":"springboot-app"}</customFields>
                </jsonProvider>
                <!-- MDC fields for structured metadata -->
                <mdc/>
            </providers>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CLOUDWATCH"/>
    </root>
</configuration>

```



```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InstructionService {

    private static final Logger logger = LoggerFactory.getLogger(InstructionService.class);

    public void processInstruction(String instructionId, String status, String type) {
        try {
            // Put structured metadata into MDC
            MDC.put("instruction_meta", String.format("{\"instructionId\":\"%s\",\"status\":\"%s\",\"type\":\"%s\"}", 
                instructionId, status, type));

            // Log human-readable message
            logger.info("Processed instruction");

        } finally {
            // Clear MDC to avoid leaking metadata to other logs
            MDC.clear();
        }
    }
}
```
