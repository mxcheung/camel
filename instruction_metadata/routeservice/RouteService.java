import org.apache.camel.CamelContext;
import org.apache.camel.model.RouteDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final CamelContext camelContext;

    public RouteService(CamelContext camelContext) {
        this.camelContext = camelContext;
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
                .map(route -> route.getId())
                .collect(Collectors.toList());
    }

    private RouteDef toRouteDef(RouteDefinition rd) {
        String fromUri = rd.getInputs().isEmpty() ? "" : rd.getInputs().get(0).getEndpointUri();
        String toUri = rd.getOutputs().isEmpty() ? "" : rd.getOutputs().get(0).getEndpointUri();
        return new RouteDef(rd.getId(), fromUri, toUri);
    }
}
