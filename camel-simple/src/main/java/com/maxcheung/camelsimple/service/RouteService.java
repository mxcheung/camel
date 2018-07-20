package com.maxcheung.camelsimple.service;


import java.util.List;

import com.maxcheung.camelsimple.model.RouteDef;

public interface RouteService {

	List<RouteDef> getRouteDefs();

	List<String> getCamelRoutes();
	
}