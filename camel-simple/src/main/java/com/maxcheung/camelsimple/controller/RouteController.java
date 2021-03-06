package com.maxcheung.camelsimple.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maxcheung.camelsimple.model.RouteDef;
import com.maxcheung.camelsimple.service.RouteService;

@Controller
@RestController
@RequestMapping("routes")
public class RouteController {

	private static final Logger LOG = LoggerFactory.getLogger(RouteController.class);

	@Autowired
	private RouteService routeService;

	@GetMapping(value = "/getRouteDefs")
	public @ResponseBody List<RouteDef> getRouteDefs() {
		LOG.info(" getRoutes");
		return routeService.getRouteDefs();
	}

	@RequestMapping(value = "/getCamelRoutes", method = RequestMethod.GET)
	public @ResponseBody List<String> getCamelRoutes() {
		LOG.info(" getRoutes");
		return routeService.getCamelRoutes();
	}


	@RequestMapping(value = "/getFiles", method = RequestMethod.GET)
	public @ResponseBody List<File> getCamelRoutes( @RequestParam("locationPattern") String locationPattern) throws IOException {
		LOG.info(" getFiles");
		return routeService.getFiles(locationPattern);
	}

}