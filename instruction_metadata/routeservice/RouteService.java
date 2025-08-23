import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final CamelContext camelContext;
    private final Environment env;
    private final ResourcePatternResolver resourceResolver;
    private final SchemaRegistryService schemaRegistryService;
    private final MessageService messageService;

    public RouteService(
            CamelContext camelContext,
            Environment env,
            ResourcePatternResolver resourceResolver,
            SchemaRegistryService schemaRegistryService,
            MessageService messageService
    ) {
        this.camelContext = camelContext;
        this.env = env;
        this.resourceResolver = resourceResolver;
        this.schemaRegistryService = schemaRegistryService;
        this.messageService = messageService;
    }

    /**
     * Returns all route definitions with from/to URIs.
     */
    public List<RouteDef> getRouteDefs() {
        return camelContext.getRouteDefinitions().stream()
                .map(this::toRouteDef)
                .collect(Collectors.toList());
    }

    /**
     * Returns all Camel route IDs.
     */
    public List<String> getCamelRoutes() {
        return camelContext.getRoutes().stream()
                .map(Route::getId)
                .collect(Collectors.toList());
    }

    /**
     * Logs a route message with instruction metadata.
     */
    public void logRouteMessage(String routeId, String message, Object instructionMeta) {
        messageService.sendMessage("Route [" + routeId + "]: " + message, Map.of("instruction_meta", instructionMeta));
    }

    // --- Private helpers ---
    private RouteDef toRouteDef(RouteDefinition rd) {
        String fromUri = rd.getInputs().isEmpty() ? "" : rd.getInputs().get(0).getEndpointUri();
        String toUri = rd.getOutputs().isEmpty() ? "" : rd.getOutputs().get(0).getEndpointUri();
        return new RouteDef(rd.getId(), fromUri, toUri);
    }
}
