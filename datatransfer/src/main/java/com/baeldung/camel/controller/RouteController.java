package com.baeldung.camel.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.camel.model.MyRoute;
import com.baeldung.camel.service.RouteService;

@Controller
@RestController
@RequestMapping("routes")
public class RouteController {

	private static final Logger LOG = LoggerFactory.getLogger(RouteController.class);

	@Autowired
	private RouteService routeService;

	@RequestMapping(value = "/getRoutes", method = RequestMethod.GET)
	public @ResponseBody List<MyRoute> getRoutes() {
		LOG.info(" getRoutes");
		return routeService.getRoutes();
	}
	
	@RequestMapping(value = "/loadRoutes", method = RequestMethod.GET)
	public @ResponseBody void loadRoutes()  {
		LOG.info(" loadRoutes");
		routeService.loadRoutes();
	}

}