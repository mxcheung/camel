package com.maxcheung.camelsimple.route.processor;

import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class KibanaProcessorTest extends CamelTestSupport {

	private static final String CONTENT = "content";

	// START SNIPPET: e2
	private static final String FIXED_DATA = "Joe,Smith,Developer,75000,10012009" + LS
			+ "Jane,Doe,Architect,80000,01152008" + LS + "Jon,Anderson,Manager,85000,03182007" + LS;
	// END SNIPPET: e2

	@Test
	public void testKibanaProcessor() throws Exception {
		MockEndpoint mock = getMockEndpoint("mock:result");
		template.sendBody("direct:noop", FIXED_DATA);
		assertEquals(1,mock.getExchanges().size());
		Map<String, String> map = (Map<String, String>) mock.getExchanges().get(0).getIn().getBody();
		assertEquals(FIXED_DATA,FIXED_DATA, map.get(CONTENT));
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:noop")
				.process(new KibanaProcessor())
				.to("mock:result");

			}
		};
	}

}