package com.maxcheung.camelsimple.route.processor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class KibanaProcessor implements Processor {

	private static final String CONTENT = "content";

	public void process(Exchange exchange) throws Exception {
		String body = exchange.getIn().getBody(String.class);
		Map<String, String> map = new HashMap<String, String>();
		ZonedDateTime now = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		String formatDateTime = now.format(formatter);
		map.put("post_date", formatDateTime);
		map.put("routeId", exchange.getFromRouteId());
		map.put("exchangeId", exchange.getExchangeId());
		map.put(CONTENT, body);
		exchange.getOut().setBody(map);
	    exchange.getOut().setHeaders(exchange.getIn().getHeaders());
	}


}