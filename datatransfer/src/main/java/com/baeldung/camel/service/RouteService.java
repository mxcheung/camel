package com.baeldung.camel.service;

import java.util.List;

import com.baeldung.camel.model.MyRoute;

public interface RouteService {

	List<MyRoute> getRoutes();


	void loadRoutes();

	
}
