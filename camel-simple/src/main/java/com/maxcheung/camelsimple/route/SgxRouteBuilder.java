package com.maxcheung.camelsimple.route;


import static org.apache.camel.builder.PredicateBuilder.or;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;

import com.maxcheung.camelsimple.model.RouteDef;

public class SgxRouteBuilder extends AbsRouteBuilder  {

	static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "spring-boot";
    static final long delay= 60000;

    public SgxRouteBuilder(CamelContext camelContext, Processor processor, RouteDef routeDef) {
    	super(camelContext, processor, routeDef);
    }
    

	@Override
	public void configure() {
	
		from("quartz2://myGroup/sgxTimer?cron=0/10+5+*+*+*+?")
		.to("direct:dashboard")
		.routeId("sgx-quartz2-route");
/*
        from("scheduler://sgxFileMover?delay=60s")
        .to("bean:sgxFileMover?method=checkHoldingBay")
		.routeId("sgx-scheduler-route");
  */      

		from("direct:dashboard")
		.transform()
		.simple("Message at ${date:now:yyyy-MM-dd HH:mm:ss}")
		.log(">>> dashboard results  ${body}")
    	.routeId("dashboard-index-route")
		.to("direct:index");

		
         from("file://C:/camel/sgxtest/input?charset=utf-8")
        .to("file://C:/camel/sgxtest/holdingbay?fileName=${date:now:yyyyMMddHHmmss}_${file:name}")
    	.routeId("sgx-holdingbay-route");


                  
        from("file://C:/camel/sgxtest/process?charset=utf-8")
        	.process(processor)
    		.log(">>> send to contents based route ${headers} ${body}")
            .to("direct:a")
        	.routeId("sgx-contents-based-route");

        List<String> aaa = new ArrayList<>();
        Predicate admin = or(header("messageType").isEqualTo("Company"), 
        					 header("messageType").isEqualTo("Company2"));
        
        from("direct:a")
    	.routeId("direct-a-route")
        .choice()
	        .when(admin)
	            .to("direct:b")
	        .when(header("messageType").isEqualTo("customer"))
	            .to("direct:c")
	        .otherwise()
	            .to("direct:d");

		

		from("direct:b")
    	.routeId("direct-b-route")
		.log(">>> table b ${headers} ${body}");

		from("direct:c")
    	.routeId("direct-c-route")
		.log(">>> Delay wait for bos to process " + delay + " milli seconds table A ${headers} ${body}")
		.log(">>> table A ${headers} ${body}")
		.to("sql:classpath:sql/myquery.sql") 
		.to("direct:index");
		
		from("direct:d")
    	.routeId("direct-d-route")
		.log(">>> unknown sending to DLQ ${body}")
		.to("direct:kibana-deadletterqueue");

	}
}
