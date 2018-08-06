package com.maxcheung.camelsimple.route.kibana;

import org.apache.camel.CamelContext;

import com.maxcheung.camelsimple.model.RouteDef;
import com.maxcheung.camelsimple.route.AbsRouteBuilder;

public class KibanaRouteBuilder extends AbsRouteBuilder {

	public KibanaRouteBuilder(CamelContext camelContext, RouteDef routeDef) {
		super(camelContext, routeDef);
	}


	@Override
	public void configure() throws Exception {
  	  from(from)
          .process(new KibanaProcessor())
          .log(log)
          .tracing(tracing)
  		  .to(toUris);
  	}
}