package com.maxcheung.camelsimple.route.processor;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class BeanIOProcessorTest extends CamelTestSupport {

	private static final String INPUT_FILE = "myBeanIOInputFile.txt";
	private static final String URI_START = "file://target/beanio/input/";
	private static final String URI_END = "mock:end";
	// START SNIPPET: e2
	private static final String FIXED_DATA = "Joe,Smith,Developer,75000,10012009" + LS
			                               + "Jane,Doe,Architect,80000,01152008" + LS 
			                               + "Jon,Anderson,Manager,85000,03182007" + LS;
	// END SNIPPET: e2
	private static final String FIXED_RESULT = "[{\"firstName\":\"Joe\",\"lastName\":\"Smith\",\"title\":\"Developer\",\"salary\":75000,\"hireDate\":1254319200000},{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"title\":\"Architect\",\"salary\":80000,\"hireDate\":1200315600000},{\"firstName\":\"Jon\",\"lastName\":\"Anderson\",\"title\":\"Manager\",\"salary\":85000,\"hireDate\":1174136400000}]";

	@Test
	public void testProcessor() throws Exception {
		MockEndpoint result = getMockEndpoint(URI_END);
		result.expectedMessageCount(1);
		result.expectedBodiesReceived(FIXED_RESULT);
		template.sendBodyAndHeader(URI_START, FIXED_DATA, Exchange.FILE_NAME, INPUT_FILE);
		result.assertIsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(URI_START)
				.process(new BeanIOProcessor())
				.to(URI_END);

			}
		};
	}

}