package com.maxcheung.camelsimple.route.sql;


import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SqlRouteBuilder extends RouteBuilder {

	@Override
	public void configure() {
         from("file://C:/camel/sqltest/sgxmargin/input?charset=utf-8")
        .routeId("sql-route")
 		.to("sql:classpath:sql/sgxmarginall.sql?outputType=StreamList") 
		 .to("log:stream")
		 .split(body()).streaming()
	        .to("log:row")
			.log(">>> row  ${body}")
	   	    .process(new SqlProcessor())
			.log(">>> sql ${headers}  ${body}")
		//	.to("elasticsearch://elasticsearch?ip=localhost&port=9300&operation=INDEX&indexName=sgxmargin&indexType=tweet")
 		.end();

         from("direct:sgx")
 		.to("elasticsearch://elasticsearch?ip=localhost&port=9300&operation=INDEX&indexName=sgxmargin&indexType=tweet");

	}
}
