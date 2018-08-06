package com.maxcheung.camelsimple.route.sgx;


import static org.apache.camel.builder.PredicateBuilder.or;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class SgxRouteBuilder extends RouteBuilder {

    static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "spring-boot";
    static final long delay= 60000;
    

	@Override
	public void configure() {
	
		from("quartz2://myGroup/sgxTimer?cron=0/10+5+*+*+*+?")
		.to("direct:dashboard")
		.routeId("sgx-quartz2-route");

        from("scheduler://sgxFileMover?delay=60s")
        .to("bean:sgxFileMover?method=checkHoldingBay")
		.routeId("sgx-scheduler-route");
        

		from("direct:dashboard")
		.transform()
		.simple("Message at ${date:now:yyyy-MM-dd HH:mm:ss}")
		.log(">>> dashboard results  ${body}")
		.to("direct:index");

		
         from("file://C:/camel/sgxtest/input?charset=utf-8")
        .to("file://C:/camel/sgxtest/holdingbay?fileName=${date:now:yyyyMMddHHmmss}_${file:name}")
    	.routeId("sgx-holdingbay-route");


                  
        from("file://C:/camel/sgxtest/process?charset=utf-8")
        	.process(new SgxProcessor())
    		.log(">>> send to contents based route ${headers} ${body}")
            .to("direct:a")
        	.routeId("sgx-contents-based-route");

        List<String> aaa = new ArrayList<>();
        Predicate admin = or(header("messageType").isEqualTo("Company"), 
        					 header("messageType").isEqualTo("Company2"));
        
        from("direct:a")
        .choice()
	        .when(admin)
	            .to("direct:b")
	        .when(header("messageType").isEqualTo("customer"))
	            .to("direct:c")
	        .otherwise()
	            .to("direct:d");

		

		from("direct:b")
		.log(">>> table b ${headers} ${body}");

		from("direct:c")
		.log(">>> Delay wait for bos to process " + delay + " milli seconds table A ${headers} ${body}")
		.log(">>> table A ${headers} ${body}")
		.to("sql:classpath:sql/myquery.sql") 
		.to("direct:index");
		
		from("direct:d")
		.log(">>> unknown sending to DLQ ${body}")
		.to("direct:kibana-deadletterqueue");



	}
}
