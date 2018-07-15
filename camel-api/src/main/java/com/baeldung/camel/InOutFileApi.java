package com.baeldung.camel;

import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class InOutFileApi extends RouteBuilder {

    static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "spring-boot";

	@Override
	public void configure() {

//		from("file://C:/in/?fileName=MyFile.txt&charset=utf-8")
//	    .to("file://C:/in2/?fileName=MyFile.txt&charset=utf-8")

		
	//	from("rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q1&autoDelete=false");  
//		from("rabbitmq://localhost:5672/"+ topicExchangeName + "?username=guest&password=guest&queue=q1" + queueName)
		
//		from("rabbitmq://localhost:5672/" + topicExchangeName + "?queue=q1")
		
		
//		from("rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q1&autoDelete=false")  
//	    .to("file://C:/in2/?fileName=MyFile.txt&charset=utf-8")
	   
		
		
		from("file://C:/in/?fileName=MyFile.txt&charset=utf-8")
		.to("rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q1&autoDelete=false")  
		.routeId("in-out-file-route")
		.tracing()
		.log(">>> ${body}");
		
		
		rest("/api2x/")
		.description("Test2 REST Service")
		.id("api-carx-route")
		.post("/bean")
		.produces(MediaType.APPLICATION_JSON)
		.consumes(MediaType.APPLICATION_JSON)
		// .get("/hello/{place}")
		.to("rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q2&autoDelete=false");  


		
	}
}
