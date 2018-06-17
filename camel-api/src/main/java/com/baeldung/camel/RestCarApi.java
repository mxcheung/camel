package com.baeldung.camel;

import javax.ws.rs.core.MediaType;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class RestCarApi extends RouteBuilder {

	@Override
	public void configure() {

		/**
		 * The Rest DSL supports automatic binding json/xml contents to/from POJOs using
		 * Camels Data Format. By default the binding mode is off, meaning there is no
		 * automatic binding happening for incoming and outgoing messages. You may want
		 * to use binding if you develop POJOs that maps to your REST services request
		 * and response types.
		 */

		rest("/api2/")
				.description("Test2 REST Service")
				.id("api-car-route")
				.post("/bean")
				.produces(MediaType.APPLICATION_JSON)
				.consumes(MediaType.APPLICATION_JSON)
				// .get("/hello/{place}")
				.bindingMode(RestBindingMode.auto)
				.type(MyCar.class)
				.enableCORS(true)
				// .outType(OutBean.class)

				.to("direct:remoteCarService");

		from("direct:remoteCarService")
		.routeId("direct-car-route")
		.tracing()
		.log(">>> ${body.id}")
		.log(">>> ${body.model}")
		// .transform().simple("blue ${in.body.name}")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				MyCar bodyIn = (MyCar) exchange.getIn().getBody();

				ExampleCarServices.example(bodyIn);

				exchange.getIn().setBody(bodyIn);
			}
		}).setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));
	}
}
