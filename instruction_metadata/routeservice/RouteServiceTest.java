import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.FromDefinition;
import org.apache.camel.model.ToDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RouteServiceTest {

    private CamelContext mockCamelContext;
    private Environment mockEnv;
    private ResourcePatternResolver mockResolver;
    private SchemaRegistryService mockSchemaRegistry;
    private MessageService mockMessageService;

    private RouteService routeService;

    @BeforeEach
    void setup() {
        mockCamelContext = mock(CamelContext.class);
        mockEnv = mock(Environment.class);
        mockResolver = mock(ResourcePatternResolver.class);
        mockSchemaRegistry = mock(SchemaRegistryService.class);
        mockMessageService = mock(MessageService.class);

        routeService = new RouteService(
                mockCamelContext,
                mockEnv,
                mockResolver,
                mockSchemaRegistry,
                mockMessageService
        );
    }

    @Test
    void testGetRouteDefs() throws Exception {
        RouteDefinition rd = new RouteDefinition();
        rd.setId("route-1");
        rd.addInput(new FromDefinition("direct:start"));
        rd.addOutput(new ToDefinition("log:output"));

        when(mockCamelContext.getRouteDefinitions()).thenReturn(List.of(rd));

        List<RouteDef> defs = routeService.getRouteDefs();

        assertThat(defs).hasSize(1);
        RouteDef def = defs.get(0);
        assertThat(def.getId()).isEqualTo("route-1");
        assertThat(def.getFromUri()).isEqualTo("direct:start");
        assertThat(def.getToUri()).isEqualTo("log:output");

        verify(mockCamelContext, times(1)).getRouteDefinitions();
    }

    @Test
    void testGetCamelRoutes() throws Exception {
        Route route = mock(Route.class);
        when(route.getId()).thenReturn("route-2");

        when(mockCamelContext.getRoutes()).thenReturn(List.of(route));

        List<String> routeIds = routeService.getCamelRoutes();

        assertThat(routeIds).containsExactly("route-2");

        verify(mockCamelContext, times(1)).getRoutes();
    }

    @Test
    void testLogRouteMessage_DelegatesToMessageService() {
        String routeId = "route-1";
        String message = "Processing started";
        Map<String, Object> meta = Map.of("instructionId", 123);

        routeService.logRouteMessage(routeId, message, meta);

        verify(mockMessageService, times(1))
                .sendMessage("Route [route-1]: Processing started", Map.of("instruction_meta", meta));
    }
}
