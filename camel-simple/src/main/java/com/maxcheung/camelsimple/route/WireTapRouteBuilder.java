package com.maxcheung.camelsimple.route;

import org.apache.camel.CamelContext;
import com.maxcheung.camelsimple.model.RouteDef;

public class WireTapRouteBuilder extends AbsRouteBuilder {

	public WireTapRouteBuilder(CamelContext camelContext, RouteDef routeDef) {
		super(camelContext, routeDef);
	}

	@Override
	public void configure() throws Exception {
		from(from)
			.routeId(routeId)
			.tracing(tracing)
			.wireTap(toUris[0])
			.log(log);
	}
}