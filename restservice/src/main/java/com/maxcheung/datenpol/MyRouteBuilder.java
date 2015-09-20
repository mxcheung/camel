package com.maxcheung.datenpol;

import org.apache.camel.Expression;
import org.apache.camel.spring.SpringRouteBuilder;

public class MyRouteBuilder extends SpringRouteBuilder {

	@Override
	public void configure() throws Exception {
		from("restlet:http://localhost:8089/MyRestService?restletMethod=GET")
		.setBody(simple("Nice to meet you!")).to("stream:out");
	}

}