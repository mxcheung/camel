package com.maxcheung.camelsimple.route.processor;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class NoopProcessorTest extends CamelTestSupport {

	// START SNIPPET: e2
	private static final String FIXED_DATA = "Joe,Smith,Developer,75000,10012009" + LS
			+ "Jane,Doe,Architect,80000,01152008" + LS + "Jon,Anderson,Manager,85000,03182007" + LS;
	// END SNIPPET: e2

	@Test
	public void testNoopProcessor() throws Exception {
		MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedBodiesReceived(FIXED_DATA);
		template.sendBody("direct:noop", FIXED_DATA);
		mock.assertIsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:noop")
				.process(new NoopProcessor())
				.to("mock:result");

			}
		};
	}

}