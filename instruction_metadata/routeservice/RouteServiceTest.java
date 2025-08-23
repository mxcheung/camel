import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RouteServiceJsonParseTest {

    private CamelContext mockCamelContext;
    private ResourcePatternResolver mockResolver;
    private SchemaRegistryService mockSchemaRegistry;
    private MessageService mockMessageService;
    private ObjectMapper objectMapper;

    private RouteService routeService;

    @BeforeEach
    void setup() {
        mockCamelContext = mock(CamelContext.class);
        mockResolver = mock(ResourcePatternResolver.class);
        mockSchemaRegistry = mock(SchemaRegistryService.class);
        mockMessageService = mock(MessageService.class);
        objectMapper = new ObjectMapper();

        routeService = new RouteService(
                mockCamelContext,
                mockResolver,
                mockSchemaRegistry,
                mockMessageService,
                objectMapper
        );
    }

    @Test
    void testLoadRoutes_ParsesJsonContent() throws Exception {
        Resource res1 = mock(Resource.class);
        Resource res2 = mock(Resource.class);

        String json1 = "{\"routeId\": \"r1\", \"from\": \"direct:start\", \"to\": \"log:end\"}";
        String json2 = "{\"routeId\": \"r2\", \"from\": \"seda:queue\", \"to\": \"mock:result\"}";

        when(res1.getFilename()).thenReturn("route1.json");
        when(res2.getFilename()).thenReturn("route2.json");

        when(res1.getURL()).thenReturn(
                new ByteArrayInputStream(json1.getBytes(StandardCharsets.UTF_8)).toString().getClass().getResource("/").toURI().toURL()
        );
        when(res2.getURL()).thenReturn(
                new ByteArrayInputStream(json2.getBytes(StandardCharsets.UTF_8)).toString().getClass().getResource("/").toURI().toURL()
        );

        when(mockResolver.getResources("classpath:routes/*.json"))
                .thenReturn(new Resource[]{res1, res2});

        List<RouteDef> routeDefs = routeService.loadRoutes();

        assertThat(routeDefs).hasSize(2);

        RouteDef r1 = routeDefs.get(0);
        assertThat(r1.getId()).isEqualTo("route1.json");
        assertThat(r1.getFromUri()).isEqualTo("direct:start");
        assertThat(r1.getToUri()).isEqualTo("log:end");

        RouteDef r2 = routeDefs.get(1);
        assertThat(r2.getId()).isEqualTo("route2.json");
        assertThat(r2.getFromUri()).isEqualTo("seda:queue");
        assertThat(r2.getToUri()).isEqualTo("mock:result");

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
