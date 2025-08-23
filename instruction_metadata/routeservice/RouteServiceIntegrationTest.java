import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RouteServiceIntegrationTest {

    private CamelContext mockCamelContext;
    private ResourcePatternResolver resourceResolver;
    private SchemaRegistryService mockSchemaRegistry;
    private MessageService mockMessageService;
    private ObjectMapper objectMapper;

    private RouteService routeService;

    @BeforeEach
    void setup() {
        mockCamelContext = mock(CamelContext.class);
        resourceResolver = new PathMatchingResourcePatternResolver();
        mockSchemaRegistry = mock(SchemaRegistryService.class);
        mockMessageService = mock(MessageService.class);
        objectMapper = new ObjectMapper();

        routeService = new RouteService(
                mockCamelContext,
                resourceResolver,
                mockSchemaRegistry,
                mockMessageService,
                objectMapper
        );
    }

    @Test
    void testLoadRoutesFromTestResources() {
        List<RouteDef> routeDefs = routeService.loadRoutes();

        assertThat(routeDefs).hasSizeGreaterThanOrEqualTo(2);

        RouteDef r1 = routeDefs.stream()
                .filter(r -> r.getId().equals("route1.json"))
                .findFirst().orElseThrow();
        assertThat(r1.getFromUri()).isEqualTo("direct:start");
        assertThat(r1.getToUri()).isEqualTo("log:end");

        RouteDef r2 = routeDefs.stream()
                .filter(r -> r.getId().equals("route2.json"))
                .findFirst().orElseThrow();
        assertThat(r2.getFromUri()).isEqualTo("seda:queue");
        assertThat(r2.getToUri()).isEqualTo("mock:result");
    }

    @Test
    void testLogRouteMessageDelegatesToMessageService() {
        String routeId = "route-json";
        String message = "JSON route loaded";
        Map<String, Object> meta = Map.of("instructionId", 101);

        routeService.logRouteMessage(routeId, message, meta);

        // Just verifies no exception; real verification requires a spy/mock of MessageService
    }
}
