package com.maxcheung.camelsimple.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.model.RouteDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxcheung.camelsimple.model.RouteDef;
import com.maxcheung.camelsimple.route.DefaultRouteBuilder;
import com.maxcheung.camelsimple.route.WireTapRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RouteServiceImpl implements RouteService {
	
	private static final Logger LOG = LoggerFactory.getLogger(RouteServiceImpl.class);

	private final CamelContext camelContext;
	private List<RouteDef> routeDefs;
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	public RouteServiceImpl(CamelContext camelContext) throws URISyntaxException, IOException {
		this.camelContext = camelContext;
		this.routeDefs = initRoute();
	}

	@Override
	public List<RouteDef> getRouteDefs() {
		return routeDefs;
	}
	
	@Override
	public List<String> getCamelRoutes() {
		List<String> routes = new ArrayList<String>();
		List<RouteDefinition> camelDefs = camelContext.getRouteDefinitions();
		for (RouteDefinition routeDefinition : camelDefs) {
			routes.add(routeDefinition.toString());
		}
		return routes;
	}
	
	private RoutesBuilder getRouteBuilder(RouteDef routeOptions) {
		RoutesBuilder routesBuilder;
		if ("WIRETAP".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new WireTapRouteBuilder(camelContext,  routeOptions);
		} else {
			 routesBuilder = new DefaultRouteBuilder(camelContext,  routeOptions);
		}
		return  routesBuilder;
	}
	
	public List<RouteDef> initRoute() throws URISyntaxException, IOException {
		List<RouteDef> routes = loadRouteDefs();
		for (RouteDef routeDef : routes) {
			try {
				camelContext.addRoutes(getRouteBuilder(routeDef));
			} catch (Exception e) {
				LOG.error(" Erroring adding route {}", e);
			}
		}
		return routes;

	}
	


	

	private List<RouteDef> loadRouteDefs() throws URISyntaxException, IOException {
		List<RouteDef> routes = new ArrayList<RouteDef>();
		routes.add(getDef("route\\dev\\in3-in2-route.json"));
		return routes;
	}

	private RouteDef getDef(String fileName) throws URISyntaxException, IOException {
		Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
		String content = new String(Files.readAllBytes(path));
		return mapper.readValue(content, RouteDef.class);
	}
	

}