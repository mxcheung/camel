@Component
public class SimpleDynamicRouteBuilder extends RouteBuilder {

    @Autowired
    private List<SimpleRouteDefinition> routeDefinitions;

    @Override
    public void configure() {

        for (SimpleRouteDefinition def : routeDefinitions) {
            from(def.getFromUri())
                // Log that the route started
                .log("Received message from ${routeId}: ${body}")
                
                // Filter & log skipped heartbeats
                .filter(exchange -> {
                    String type = exchange.getIn().getHeader("messageType", String.class);
                    boolean skip = "HEARTBEAT".equalsIgnoreCase(type);
                    if (skip) {
                        log.info("Skipping heartbeat message from route [{}], body: {}",
                                def.getFromUri(),
                                exchange.getIn().getBody(String.class));
                    }
                    return !skip; // Only allow non-heartbeats
                })
                
                // Send to configured destinations
                .to(def.getToUris().toArray(new String[0]));
        }
    }
}
