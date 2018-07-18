package com.baeldung.camel.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

import com.baeldung.camel.model.RouteDef;

public class MyRouteBuilder extends RouteBuilder {

	private final String routeId;
	private final String from;
	private final String destination;
	private final String log;
    private final String tracing;

	public MyRouteBuilder(CamelContext camelContext, RouteDef routeDef) {
		super(camelContext);
		this.routeId = routeDef.getRouteId();
		this.from = routeDef.getFrom();
		this.destination = routeDef.getDestination();
		this.log = routeDef.getLog();
		this.tracing = routeDef.getTracing();
	}

	@Override
	public void configure() throws Exception {
		from(from)
			.routeId(routeId)
			.to(destination)
			.tracing(tracing)
			.log(log);
	}

}