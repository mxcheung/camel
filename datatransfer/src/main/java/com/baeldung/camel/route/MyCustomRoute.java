package com.baeldung.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyCustomRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
/*    	
		from("file://C:/in/?fileName=MyFile.txt&charset=utf-8")
		.to("rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q1&autoDelete=false")  
		.routeId("in-out-file-route");

		from("file://C:/in2/?fileName=MyFile.txt&charset=utf-8")
		.to("rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q1&autoDelete=false")  
		.routeId("in-out-file2-route");
*/
    }
}