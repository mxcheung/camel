package com.maxcheung.camelsimple.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;

import com.maxcheung.camelsimple.model.RouteDef;

public abstract class AbsRouteBuilder extends RouteBuilder {

	protected static final String DEFAULT_TRACING = "true";
	protected static final String DEFAULT_BODY = ">>> ${body}";
	protected final String routeId;
	protected final String from;
	protected final String[] toUris;
	protected final String log;
	protected final String tracing;
	protected final RouteDef routeDef;

	public AbsRouteBuilder(CamelContext camelContext, RouteDef routeDef) {
		super(camelContext);
		this.routeId = routeDef.getRouteId();
		this.from = routeDef.getFrom();
		this.toUris = routeDef.getToUris();
		this.log = StringUtils.defaultIfEmpty(routeDef.getLog(), DEFAULT_BODY);
		this.tracing = StringUtils.defaultIfEmpty(routeDef.getTracing(), DEFAULT_TRACING);
		this.routeDef = routeDef;

	}

}