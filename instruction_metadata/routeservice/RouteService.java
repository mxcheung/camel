import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
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
     * Load route definitions from configured resource path.
     */
    public List<RouteDef> loadRoutes() {
        String resourcePath = env.getProperty("camel_route_path");
        if (resourcePath == null || resourcePath.isBlank()) {
            throw new IllegalStateException("Property 'camel_route_path' is not configured");
        }

        Resource[] resources = getFilesList(resourcePath);

        return Arrays.stream(resources)
                .map(this::toRouteDefFromResource)
                .collect(Collectors.toList());
    }

    // --- Public methods to access Camel routes ---
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

    public void logRo
