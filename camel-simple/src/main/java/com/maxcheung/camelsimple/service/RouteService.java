package com.maxcheung.camelsimple.service;


import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;

import com.maxcheung.camelsimple.model.RouteDef;

public interface RouteService {

	List<RouteDef> getRouteDefs();

	List<String> getCamelRoutes();

	Resource[] getFiles(String locationPattern) throws IOException;
	
}