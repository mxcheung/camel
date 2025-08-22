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
