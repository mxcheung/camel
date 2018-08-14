package com.maxcheung.camelsimple.route;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;

import com.maxcheung.camelsimple.model.RouteDef;

public class KafkaRouteBuilder extends AbsRouteBuilder {

	public KafkaRouteBuilder(CamelContext camelContext, Processor processor, RouteDef routeDef) {
		super(camelContext, processor, routeDef);
	}

	@Override
	public void configure() throws Exception {
		from(from)
			.routeId(routeId)
            .process(processor)
			.to(toUris)
			.tracing(tracing)
			.log(log);
	}

}