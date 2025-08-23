import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RouteServiceJsonLoadTest {

    private CamelContext mockCamelContext;
    private ResourcePatternResolver mockResolver;
    private SchemaRegistryService mockSchemaRegistry;
    private MessageService mockMessageService;

    private RouteService routeService;

    @BeforeEach
    void setup() {
        mockCamelContext = mock(CamelContext.class);
        mockResolver = mock(ResourcePatternResolver.class);
        mockSchemaRegistry = mock(SchemaRegistryService.class);
        mockMessageService = mock(MessageService.class);

        routeService = new RouteService(
                mockCamelContext,
                mockResolver,
                mockSchemaRegistry,
                mockMessageService
        );
    }

    @Test
    void testLoadRoutes_ReturnsJsonRouteDefs() throws Exception {
        Resource res1 = mock(Resource.class);
        Resource res2 = mock(Resource.class);

        when(res1.getFilename()).thenReturn("route1.json");
        when(res2.getFilename()).thenReturn("route2.json");

        when(mockResolver.getResources("classpath:routes/*.json"))
                .thenReturn(new Resource[]{res1, res2});

        List<RouteDef> routeDefs = routeService.loadRoutes();

        assertThat(routeDefs).hasSize(2);
        assertThat(routeDefs.get(0).getId()).isEqualTo("route1.json");
        assertThat(routeDefs.get(1).getId()).isEqualTo("route2.json");

        verify(mockResolver, times(1)).getResources("classpath:routes/*.json");
    }

    @Test
    void testLoadRoutes_EmptyResources_ReturnsEmptyList() throws Exception {
        when(mockResolver.getResources("classpath:routes/*.json"))
                .thenReturn(new Resource[]{});

        List<RouteDef> routeDefs = routeService.loadRoutes();

        assertThat(routeDefs).isEmpty();
        verify(mockResolver, times(1)).getResources("classpath:routes/*.json");
    }

    @Test
    void testLogRouteMessage_DelegatesToMessageService() {
        String routeId = "route-json";
        String message = "JSON route loaded";
        Map<String, Object> meta = Map.of("instructionId", 101);

        routeService.logRouteMessage(routeId, message, meta);

        verify(mockMessageService, times(1))
                .sendMessage("Route [route-json]: JSON route loaded", Map.of("instruction_meta", meta));
    }
}
