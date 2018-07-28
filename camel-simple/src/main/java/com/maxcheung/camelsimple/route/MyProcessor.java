package com.maxcheung.camelsimple.route;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;

public class MyProcessor implements Processor {
	/*
	 * ProducerTemplate producer;
	 * 
	 * public void setProducer(ProducerTemplate producer) { this.producer =
	 * producer; }
	 */
	public void process(Exchange exchange) throws Exception {
		ProducerTemplate template = exchange.getContext().createProducerTemplate();

		Map<String, String> map = new HashMap<String, String>();
		// map.put("content", "test");
		String body = exchange.getIn().getBody(String.class);
		// String cleanString = body.replaceAll("\r", "").replaceAll("\n", "");
		map.put("content", body);
		exchange.getOut().setBody(map);

		// String indexId = template.requestBody("direct:index", map, String.class);
	}
}