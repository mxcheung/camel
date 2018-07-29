package com.maxcheung.camelsimple.route;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MyProcessor implements Processor {

	ObjectMapper objectMapper;

	public void process(Exchange exchange) throws Exception {
		exchange.getOut().setBody(getMap(exchange));
	}

	//"2018-07-29T04:06:55.310+10:00
	private Map<String, String> getMap(Exchange exchange) {
		Map<String, String> map = new HashMap<String, String>();
		String body = exchange.getIn().getBody(String.class);
		ZonedDateTime now = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		String formatDateTime = now.format(formatter);
		map.put("post_date", formatDateTime);
		map.put("routeId", exchange.getFromRouteId());
		map.put("exchangeId", exchange.getExchangeId());
		map.put("content", body);
		return map;
	}

}