package com.baeldung.camel.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.model.FromDefinition;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baeldung.camel.model.MyRoute;
import com.baeldung.camel.model.RouteDef;
import com.baeldung.camel.service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RouteBuilderServiceImpl implements RouteBuilderService {

	private static final Logger LOG = LoggerFactory.getLogger(RouteBuilderServiceImpl.class);

	private final CamelContext camelContext;

	private final RouteService routeService;
	
    private ObjectMapper mapper;

	@Autowired
	public RouteBuilderServiceImpl(CamelContext camelContext, RouteService routeService) {
		super();
        this.mapper = new ObjectMapper();
		this.camelContext = camelContext;
		this.routeService = routeService;
		routeService.loadRoutes();
		initRoute();
	}


	@Override
	public List<String> getRoutes() {
		List<String> routes = new ArrayList<String>();
		List<RouteDefinition> camelDefs = getCamelContext().getRouteDefinitions();
		for (RouteDefinition routeDefinition : camelDefs) {
			routes.add(routeDefinition.toString());
		}
		return routes;
	}
	
	@Override
	public RouteDefinition getRouteDefinition() {
		List<RouteDefinition> camelDefs = getCamelContext().getRouteDefinitions();
		RouteDefinition routeDefinition = camelDefs.get(0);
		List<FromDefinition> inputs = routeDefinition.getInputs();
		routeDefinition.setInputs(inputs);
		return routeDefinition;
	}
	

	@Override
	public void addRoute(MyRoute myRoute) throws Exception {
		RouteDef routeOptions = mapper.readValue(myRoute.getOptions(), RouteDef.class);
		MyRouteBuilder myRouteBuilder = new MyRouteBuilder(camelContext,  routeOptions);
		getCamelContext().addRoutes(myRouteBuilder);

	}

	@Override
	public void initRoute() {
		List<MyRoute> routes = routeService.getRoutes();
		for (MyRoute route : routes) {
			try {
				addRoute(route);
			} catch (Exception e) {
				LOG.error(" Erroring adding route {}", e);
			}
		}

	}



	public CamelContext getCamelContext() {
		return camelContext;
	}

}