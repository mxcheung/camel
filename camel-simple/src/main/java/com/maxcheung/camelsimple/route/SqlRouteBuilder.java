package com.maxcheung.camelsimple.route;


import org.apache.camel.CamelContext;
import org.apache.camel.Processor;

import com.maxcheung.camelsimple.model.RouteDef;

public class SqlRouteBuilder extends AbsRouteBuilder {

	public SqlRouteBuilder(CamelContext camelContext, Processor processor, RouteDef routeDef) {
		super(camelContext, processor, routeDef);
	}

	@Override
	public void configure() {
         from(from)
        .routeId(routeId)
 		.to(toUris) 
		.to("log:stream")
		 .split(body()).streaming()
	        .to("log:row")
			.log(">>> row  ${body}")
	   	    .process(processor)
			.log(log)
			.to("direct:sgx")
 		.end();
	}
}
