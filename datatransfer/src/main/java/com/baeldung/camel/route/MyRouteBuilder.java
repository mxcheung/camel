package com.baeldung.camel.route;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

import com.baeldung.camel.model.MyRoute;
import com.baeldung.camel.model.RouteOptions;

public class MyRouteBuilder extends RouteBuilder {

	private MyRoute myRoute;
	private RouteOptions routeOptions;

	public MyRouteBuilder(CamelContext camelContext, MyRoute myRoute, RouteOptions routeOptions) {
		super(camelContext);
		this.myRoute = myRoute;
		this.routeOptions = routeOptions;
	}

	@Override
	public void configure() throws Exception {
		RouteDefinition routeDefinition = from(myRoute.getSource())
		.to(myRoute.getDestination());
		routeDefinition.routeId(routeOptions.getRouteId());
		Map<String, String> params = routeOptions.getParams();
		if (params != null) {
			setLog(routeDefinition, params);
			setTracing(routeDefinition, params);
		}
	}

	private void setLog(RouteDefinition routeDefinition, Map<String, String> params) {
		String log = params.get("LogMessage");
		if (log != null) {
			routeDefinition.log(log);
		}
	}
	
	private void setTracing(RouteDefinition routeDefinition, Map<String, String> params) {
		String tracing = params.get("tracing");
		if (tracing != null) {
			routeDefinition.tracing(tracing);
		}
	}

}