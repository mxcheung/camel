package com.maxcheung.camelsimple.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class RouteServiceCamelTest {

	private static final String CAMELSIMPLE_ROUTE_PATH = "camelsimple.route.path";
	private static final long DURATION_MILIS = 10000;

	private RouteService routeService;

	@Mock
	private ObjectMapper mapper;

	@Mock
	private Environment env;

	private CamelContext camelContext;


	@Before
	public void setup() throws Exception {
		camelContext = new DefaultCamelContext();
		when(env.getProperty(CAMELSIMPLE_ROUTE_PATH)).thenReturn("route\\test\\");
		mapper = new ObjectMapper();
		routeService = new RouteServiceImpl(env, camelContext);
	}

	@Test
	public void shouldGetCamelRoutes() throws Exception {
		List<String> camelRoutes = routeService.getCamelRoutes();
		assertEquals(2, camelRoutes.size());
//		camelContext.start();
//		Thread.sleep(DURATION_MILIS);
		camelContext.stop();
	}

}
