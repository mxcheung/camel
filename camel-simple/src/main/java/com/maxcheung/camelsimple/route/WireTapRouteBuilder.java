package com.maxcheung.camelsimple.route;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;

import com.maxcheung.camelsimple.model.RouteDef;

public class WireTapRouteBuilder extends AbsRouteBuilder {

	public WireTapRouteBuilder(CamelContext camelContext, Processor processor, RouteDef routeDef) {
		super(camelContext, processor, routeDef);
	}

	@Override
	public void configure() throws Exception {
		from(from)
			.process(processor)
			.routeId(routeId)
			.tracing(tracing)
			.wireTap(toUris[0])
			.log(log);
	}
}