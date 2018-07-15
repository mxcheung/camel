package com.baeldung.camel.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

import com.baeldung.camel.model.MyRoute;

public class MyRouteBuilder extends RouteBuilder {

    private MyRoute myRoute;


    public MyRouteBuilder(CamelContext camelContext, MyRoute myRoute) {
        super(camelContext);
        this.myRoute=myRoute;
	}

	@Override
    public void configure() throws Exception {
        RouteDefinition routeDefinition = from(myRoute.getSource())
        .to(myRoute.getDestination())
        .tracing()
		.routeId(myRoute.getRouteId());
        
        routeDefinition.log(">>> ${body}");
    }
}