package com.maxcheung.camelsimple.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class InOutFileApi extends RouteBuilder {

    static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "spring-boot";

	@Override
	public void configure() {
		
		from("file://C:/sqltest/?charset=utf-8")
        .process(new MySqlProcessor())
		.log(">>> table A ${headers} ${body}")
		.to("direct:tableX", "direct:tableA")
		.routeId("in-out-file-route");
		

		from("direct:tableX")
		.log(">>> table X ${headers} ${body}");

		from("direct:tableA")
		.log(">>> table A ${headers} ${body}")
		.to("sql:select * from ROUTE_DEF WHERE ID = :#reference") 
		.to("direct:index");
		
		from("direct:tableB")
		.to("sql:select * from ROUTE_DEF  WHERE ID = :#reference") 
		.log(">>> table B ${body}")
		.to("direct:index");
		
	}
}
