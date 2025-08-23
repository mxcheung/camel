import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final CamelContext camelContext;
    private final ResourcePatternResolver resourceResolver;
    private final SchemaRegistryService schemaRegistryService;
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    private static final String ROUTE_JSON_PATTERN = "classpath:routes/*.json";

    public RouteService(
            CamelContext camelContext,
            ResourcePatternResolver resourceResolver,
            SchemaRegistryService schemaRegistryService,
            MessageService messageService,
            ObjectMapper objectMapper
    ) {
        this.camelContext = camelContext;
        this.resourceResolver = resourceResolver;
        this.schemaRegistryService = schemaRegistryService;
        this.messageService = messageService;
        this.objectMapper = objectMapper;
    }

    /**
     * Load route definitions from JSON resources and parse content.
     */
    public List<RouteDef> loadRoutes() {
        Resource[] resources = getFilesList(ROUTE_JSON_PATTERN);

        return Arrays.stream(resources)
                .map(this::toRouteDefFromResource)
                .collect(Collectors.toList());
    }

    public List<RouteDef> getRouteDefs() {
        return camelContext.getRouteDefinitions().stream()
                .map(this::toRouteDef)
                .collect(Collectors.toList());
    }

    public List<String> getCamelRoutes() {
        return camelContext.getRoutes().stream()
                .map(Route::getId)
                .collect(Collectors.toList());
    }

    public void logRouteMessage(String routeId, String message, Object instructionMeta) {
        messageService.sendMessage("Route [" + routeId + "]: " + message,
                Map.of("instruction_meta", instructionMeta));
    }

    // --- Private helpers ---

    private Resource[] getFilesList(String resourcePattern) {
        try {
            return resourceResolver.getResources(resourcePattern);
        } catch (IOException e) {
            throw new RuntimeException("Failed to list resources from classpath: " + resourcePattern, e);
        }
    }

    private RouteDef toRouteDef(RouteDefinition rd) {
        String fromUri = rd.getInputs().isEmpty() ? "" : rd.getInputs().get(0).getEndpointUri();
        String toUri = rd.getOutputs().isEmpty() ? "" : rd.getOutputs().get(0).getEndpointUri();
        return new RouteDef(rd.getId(), fromUri, toUri);
    }

    private RouteDef toRouteDefFromResource(Resource resource) {
        String routeId = resource.getFilename();
        try {
            // Read JSON content
            String jsonStr = Resources.toString(resource.getURL(), StandardCharsets.UTF_8);
            JsonNode node = objectMapper.readTree(jsonStr);

            String fromUri = node.has("from") ? node.get("from").asText() : "from-dummy";
            String toUri = node.has("to") ? node.get("to").asText() : "to-dummy";

            return new RouteDef(routeId, fromUri, toUri);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read/parse resource: " + routeId, e);
        }
    }
}
