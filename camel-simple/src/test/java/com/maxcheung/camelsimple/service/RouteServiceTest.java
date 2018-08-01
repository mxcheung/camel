package com.maxcheung.camelsimple.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.model.RouteDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxcheung.camelsimple.model.RouteDef;
import com.maxcheung.camelsimple.repo.RouteDefRepository;

@RunWith(MockitoJUnitRunner.class)
public class RouteServiceTest {

	private static final String CAMELSIMPLE_ROUTE_PATH = "camelsimple.route.path";

	private RouteService routeService;

	@Mock
	private ObjectMapper mapper;

	@Mock
	private Environment env;

	@Mock
	private CamelContext camelContext;

	@Mock
	private RouteDefRepository routeDefRepository;

	private List<RouteDefinition> routeDefinitions;
	
	private DefaultResourceLoader resourceLoader;

	@Before
	public void setup() throws Exception {
		resourceLoader = new DefaultResourceLoader();
		this.routeDefinitions = getRouteDefinitions();
		when(env.getProperty(CAMELSIMPLE_ROUTE_PATH)).thenReturn("\\route\\dev\\");
		when(camelContext.getRouteDefinitions()).thenReturn(routeDefinitions);
		mapper = new ObjectMapper();
		routeService = new RouteServiceImpl(env, camelContext, routeDefRepository, resourceLoader);
	}

	@Test
	public void shouldGetRouteDefs() throws JsonProcessingException {
		List<RouteDef> routeDefs = routeService.getRouteDefs();
		assertEquals(2, routeDefs.size());
	}

	@Test
	public void shouldGetCamelRoutes() throws JsonProcessingException {
		List<String> camelRoutes = routeService.getCamelRoutes();
		verify(camelContext, times(1)).getRouteDefinitions();
		assertEquals(1, camelRoutes.size());
	}

	private List<RouteDefinition> getRouteDefinitions() {
		List<RouteDefinition> routeDefinitions = new ArrayList<RouteDefinition>();
		RouteDefinition routeDefinition = new RouteDefinition();
		routeDefinition.from("file://C:/in3/?fileName=MyFile.txt&charset=utf-8");
		routeDefinitions.add(routeDefinition);
		return routeDefinitions;
	}

}
