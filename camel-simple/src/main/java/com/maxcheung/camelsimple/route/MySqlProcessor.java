package com.maxcheung.camelsimple.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MySqlProcessor implements Processor {


	public void process(Exchange exchange) throws Exception {
		String body = exchange.getIn().getBody(String.class);
        exchange.getOut().setHeader("reference", body);
	}


}