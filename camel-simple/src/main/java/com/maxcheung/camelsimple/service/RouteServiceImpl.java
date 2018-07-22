package com.maxcheung.camelsimple.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxcheung.camelsimple.model.RouteDef;
import com.maxcheung.camelsimple.route.DefaultRouteBuilder;
import com.maxcheung.camelsimple.route.WireTapRouteBuilder;

@Service
public class RouteServiceImpl implements RouteService {

	private static final String CAMELSIMPLE_ROUTE_PATH = "camelsimple.route.path";

	private static final Logger LOG = LoggerFactory.getLogger(RouteServiceImpl.class);

	private final CamelContext camelContext;
	private final Environment env;
	private List<RouteDef> routeDefs;
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	public RouteServiceImpl(Environment env, CamelContext camelContext) {
		this.env = env;
		this.camelContext = camelContext;
		loadRoutes();
	}

	
    public void loadRoutes(){
    	try {
			this.routeDefs = initRoute();
		} catch (Exception e) {
			LOG.error("Exception ocurred loading routes {}", e );
		}
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
			routesBuilder = new WireTapRouteBuilder(camelContext, routeOptions);
		} else {
			routesBuilder = new DefaultRouteBuilder(camelContext, routeOptions);
		}
		return routesBuilder;
	}

	public List<RouteDef> initRoute() throws Exception {
		List<RouteDef> routes = new ArrayList<RouteDef>();
		String resourcePath = env.getProperty(CAMELSIMPLE_ROUTE_PATH);
		List<String> fileNames = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream(resourcePath),
				Charsets.UTF_8);
		for (String fileName : fileNames) {
			String resourceFileName = resourcePath + fileName;
			LOG.info("Loading route {}", resourceFileName);
			Path path = Paths.get(getClass().getClassLoader().getResource(resourceFileName).toURI());
			String content = new String(Files.readAllBytes(path));
			RouteDef routeDef = mapper.readValue(content, RouteDef.class);
			camelContext.addRoutes(getRouteBuilder(routeDef));
			routes.add(routeDef);
		}
		return routes;
	}
}