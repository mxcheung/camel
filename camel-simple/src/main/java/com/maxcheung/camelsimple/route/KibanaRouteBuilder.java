package com.maxcheung.camelsimple.route;

import org.apache.camel.CamelContext;
import com.maxcheung.camelsimple.model.RouteDef;

public class KibanaRouteBuilder extends AbsRouteBuilder {

	public KibanaRouteBuilder(CamelContext camelContext, RouteDef routeDef) {
		super(camelContext, routeDef);
	}


	@Override
	public void configure() throws Exception {
  	  from(from)
          .process(new MyProcessor())
          .log(log)
          .tracing(tracing)
  		  .to(toUris);
  	}
}