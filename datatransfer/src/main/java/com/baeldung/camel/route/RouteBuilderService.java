package com.baeldung.camel.route;

import java.util.List;

import org.apache.camel.model.RouteDefinition;

import com.baeldung.camel.model.MyRoute;

public interface RouteBuilderService {

	List<String> getRoutes();

	void addRoute(MyRoute myRoute) throws Exception;

	void initRoute();

	RouteDefinition getRouteDefinition();


	
}
