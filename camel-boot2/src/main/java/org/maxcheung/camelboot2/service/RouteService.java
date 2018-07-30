package org.maxcheung.camelboot2.service;


import java.io.IOException;
import java.util.List;

import org.maxcheung.camelboot2.model.RouteDef;
import org.springframework.core.io.Resource;

public interface RouteService {

	List<RouteDef> getRouteDefs();

	List<String> getCamelRoutes();

	Resource[] getFiles(String locationPattern) throws IOException;
	
}