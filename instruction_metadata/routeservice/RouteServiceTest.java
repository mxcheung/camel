import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RouteServiceLoadRoutesTest {

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
    void testLoadRoutes_BuildsRouteDefsFromResources() throws Exception {
        String resourcePath = "classpath:routes/*.xml";

        when(mockEnv.getProperty("camel_route_path")).thenReturn(resourcePath);

        Resource res1 = mock(Resource.class);
        Resource res2 = mock(Resource.class);

        when(res1.getFilename()).thenReturn("route1.xml");
        when(res2.getFilename()).thenReturn("route2.xml");

        when(mockResolver.getResources(resourcePath)).thenReturn(new Resource[]{res1, res2});

        List<RouteDef> routeDefs = routeService.loadRoutes();

        assertThat(routeDefs).hasSize(2);
        assertThat(routeDefs.get(0).getId()).isEqualTo("route1.xml");
        assertThat(routeDefs.get(1).getId()).isEqualTo("route2.xml");

        verify(mockEnv, times(1)).getProperty("camel_route_path");
        verify(mockResolver, times(1)).getResources(resourcePath);
    }

    @Test
    void testLoadRoutes_ThrowsIfPropertyMissing() {
        when(mockEnv.getProperty("camel_route_path")).thenReturn(null);

        try {
            routeService.loadRoutes();
        } catch (IllegalStateException e) {
            assertThat(e).hasMessageContaining("camel_route_path");
        }
    }
}
