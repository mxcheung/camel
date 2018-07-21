package com.maxcheung.camelsimple.route;

import org.apache.camel.CamelContext;

import com.maxcheung.camelsimple.model.RouteDef;

public class DefaultRouteBuilder extends AbsRouteBuilder {

	public DefaultRouteBuilder(CamelContext camelContext, RouteDef routeDef) {
		super(camelContext, routeDef);
	}

	@Override
	public void configure() throws Exception {
		from(from)
			.routeId(routeId)
			.to(toUris)
			.tracing(tracing)
			.log(log);
		
		
	}

}