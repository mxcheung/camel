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
    void testGetRouteDefs() throws E
