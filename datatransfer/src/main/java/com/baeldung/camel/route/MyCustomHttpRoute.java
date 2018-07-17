package com.baeldung.camel.route;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class MyCustomHttpRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
/*    	
		from("file://C:/in/?fileName=MyFile.txt&charset=utf-8")
		.to("rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q1&autoDelete=false")  
		.routeId("in-out-file-route");

		from("file://C:/in2/?fileName=MyFile.txt&charset=utf-8")
		.to("rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q1&autoDelete=false")  
		.routeId("in-out-file2-route");

		    	  .to("http4://httpbin.org/post")

    	  .to("http4://httpbin.org/post")

*/
    	getContext().setTracing(true);

    	  // general error handler
        errorHandler(defaultErrorHandler()
            .maximumRedeliveries(5)
            .backOffMultiplier(4)
            .retryAttemptedLogLevel(LoggingLevel.WARN));
    	
        // in case of a http exception then retry at most 3 times
        // and if exhausted then upload using ftp instead
        onException(IOException.class, HttpOperationFailedException.class)
            .maximumRedeliveries(3)
            .handled(true)
            .log(">>> move to error log ${body}")
        	.to("file://C:/http4/in2/error");

        
    	from("file://C:/http4/in2/")
    	  .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
    	  .to("http4://localhost:8081/sales/postSalesX")
    	  .routeId("http4-in-out-file-route")
		   .log(">>> ${body}");
    	
    }
}

