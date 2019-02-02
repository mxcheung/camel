package com.maxcheung.camelsimple.route.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class QuickFixProcessor implements Processor {

	public static final byte SOH_byte = 0x01;
    public static final String SOH = String.format("%c", (char) SOH_byte); //FUTURE: this is fragile

	public void process(Exchange exchange) throws Exception {
		String body = exchange.getIn().getBody(String.class);
		body = body.replace("|", SOH);
		exchange.getOut().setBody(body);

	}


}