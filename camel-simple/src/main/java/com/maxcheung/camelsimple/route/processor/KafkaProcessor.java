package com.maxcheung.camelsimple.route.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;

public class KafkaProcessor implements Processor {

	private static final String APPLICATIONID = "applicationId";

	public void process(Exchange exchange) throws Exception {
//		exchange.getIn().getHeader(APPLICATIONID);
//		exchange.getIn().setHeader(KafkaConstants.TOPIC, exchange.getIn().getHeader(APPLICATIONID));
		exchange.getIn().setHeader(KafkaConstants.PARTITION_KEY, 0);
		exchange.getIn().setHeader(KafkaConstants.KEY, "1");
		
		
		String appId = (String) exchange.getIn().getHeaders().get(APPLICATIONID);
		Headers headers = new RecordHeaders();
		headers.add(new RecordHeader(APPLICATIONID, appId.getBytes() ));
		exchange.getIn().setHeader(KafkaConstants.HEADERS, headers);
		exchange.getOut().setBody(exchange.getIn().getBody());
		exchange.getOut().setHeaders(exchange.getIn().getHeaders());
		exchange.getOut().setHeader(KafkaConstants.HEADERS, headers);
	}

}