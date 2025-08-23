import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    // Pattern for JSON route files
    private static final String ROUTE_JSON_PATTERN = "classpath:routes/*.json";

    public RouteService(
            CamelContext camelContext,
            ResourcePatternResolver resourceResolver,
            SchemaRegistryService schemaRegistryService,
            MessageService messageService
    ) {
        this.camelContext = camelContext;
        this.resourceResolver = resourceResolver;
        this.schemaRegistryService = schemaRegistryService;
        this.messageService = messageService;
    }

    /**
     * Load route definitions from classpath JSON resources.
     */
    public List<RouteDef> loadRoutes() {
        Resource[] resources = getFilesList(ROUTE_JSON_PATTERN);

        return Arrays.stream(resources)
                .map(this::t
