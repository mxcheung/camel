package com.baeldung.camel.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;

import com.baeldung.camel.model.RouteDef;

public class WireTapRouteBuilder extends RouteBuilder {

	private static final String DEFAULT_TRACING = "true";
	private static final String DEFAULT_BODY = ">>> ${body}";
	
	private final String routeId;
	private final String from;
	private final String destination;
	private final String log;
    private final String tracing;

	public WireTapRouteBuilder(CamelContext camelContext, RouteDef routeDef) {
		super(camelContext);
		this.routeId = routeDef.getRouteId();
		this.from = routeDef.getFrom();
		this.destination = routeDef.getDestination();
		this.log = StringUtils.defaultIfEmpty(routeDef.getLog(), DEFAULT_BODY);
		this.tracing = StringUtils.defaultIfEmpty(routeDef.getTracing(),DEFAULT_TRACING);
	}

	@Override
	public void configure() throws Exception {
		from(from)
			.routeId(routeId)
			.tracing(tracing)
			.wireTap(destination)
			.log(log)
			.to(destination);
	}

}