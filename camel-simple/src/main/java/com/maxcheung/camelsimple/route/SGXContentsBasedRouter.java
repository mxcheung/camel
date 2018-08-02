package com.maxcheung.camelsimple.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SGXContentsBasedRouter extends RouteBuilder {

    static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "spring-boot";

	@Override
	public void configure() {
		
		

        from("file://C:/sqltest/?charset=utf-8")
        	.process(new SGXMessageProcessor())
    		.log(">>> send to contents based route ${headers} ${body}")
            .to("direct:a")
        	.routeId("sgx-contents-based-route");

		from("direct:a")
        .choice()
	        .when(header("messageType").isEqualTo("Company"))
	            .to("direct:b")
	        .when(header("messageType").isEqualTo("customer"))
	            .to("direct:c")
	        .otherwise()
	            .to("direct:d");

		

		from("direct:b")
		.log(">>> table b ${headers} ${body}");

		from("direct:c")
		.log(">>> table A ${headers} ${body}")
		.to("sql:select * from ROUTE_DEF WHERE ID = :#id") 
		.to("direct:index");
		
		from("direct:d")
		.log(">>> unknown sending to DLQ ${body}")
		.to("direct:kibana-deadletterqueue");

		/*        
		from("file://C:/sqltest/?charset=utf-8")
        .process(new MySqlProcessor())
		.log(">>> table A ${headers} ${body}")
		.to("direct:tableX", "direct:tableA")
		.routeId("in-out-file-route");
*/		

	}
}
