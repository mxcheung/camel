package com.baeldung.camel.controller;

import java.util.List;

import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.camel.model.MyRoute;
import com.baeldung.camel.model.Sales;
import com.baeldung.camel.route.RouteBuilderService;

@Controller
@RestController
@RequestMapping("routebuilder")
public class RouteBuilderController {

	private static final Logger LOG = LoggerFactory.getLogger(RouteBuilderController.class);

	@Autowired
	private RouteBuilderService routeBuilderService;



	@RequestMapping(value = "/getRoutes", method = RequestMethod.GET)
	public @ResponseBody List<String> getRouteList() {
		LOG.info(" getRoutes");
		return routeBuilderService.getRoutes();
	}
	
	@RequestMapping(value = "/getRouteDefinition", method = RequestMethod.GET
			)
	public @ResponseBody RouteDefinition getRouteDefinition() {
		LOG.info(" getRouteDefinition");
		return routeBuilderService.getRouteDefinition();
	}

//	consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE },
//	produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE }

	@RequestMapping(value = "/addRoute", method = RequestMethod.POST)
	public @ResponseBody void addRoute( @RequestBody MyRoute myRoute) throws Exception {
		LOG.info(" addRoute");
		routeBuilderService.addRoute(myRoute);
	}
	
	@RequestMapping(value = "/initRoute", method = RequestMethod.GET)
	public @ResponseBody void initRoute() throws Exception {
		LOG.info(" initRoute");
		routeBuilderService.initRoute();
	}
	
}