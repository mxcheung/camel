import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.FromDefinition;
import org.apache.camel.model.ToDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RouteServiceTest {

    private CamelContext mockCamelContext;
    private RouteService routeService;

    @BeforeEach
    void setup() {
        mockCamelContext = mock(CamelContext.class);
        routeService = new RouteService(mockCamelContext);
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

        verify(mockCamelCo
