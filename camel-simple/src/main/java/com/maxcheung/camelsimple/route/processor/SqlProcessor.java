package com.maxcheung.camelsimple.route.processor;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.util.LinkedCaseInsensitiveMap;

public class SqlProcessor implements Processor {

	private static final String REQUIRED_AMOUNT = "REQUIRED_AMOUNT";
	private static final String MARGIN_AMOUNT = "MARGIN_AMOUNT";
	private static final String EXCESS_AMOUNT = "EXCESS_AMOUNT";
	private static final String TRADE_DATE = "TRADE_DATE";

	public void process(Exchange exchange) throws Exception {
		LinkedCaseInsensitiveMap<?> map = (LinkedCaseInsensitiveMap) exchange.getIn().getBody();
		Map<String, Object> msg = getMessageTypeIdMap(map);
		exchange.getOut().setBody(msg);
		exchange.getOut().setHeader("indexId", map.get("indexId"));
		ProducerTemplate template = exchange.getContext().createProducerTemplate();
		// String indexId = template.requestBodyAndHeader("direct:sgx", msg, "indexId",
		// map.get("indexId"),String.class);
		// template.requestBody("direct:sgx", map, String.class);
	}

	private Map<String, Object> getMessageTypeIdMap(LinkedCaseInsensitiveMap<?> map) {
		Map<String, Object> messageTypeIdMap = new HashMap<String, Object>();
		Date tradeDt = (Date) map.get(TRADE_DATE);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		ZonedDateTime zonedTradeDt = tradeDt.toLocalDate().atStartOfDay(ZoneOffset.systemDefault());
		String formatDateTime = zonedTradeDt.format(formatter);
		BigDecimal excessAmount = (BigDecimal) map.get(EXCESS_AMOUNT);
		BigDecimal marginAmount = (BigDecimal) map.get(MARGIN_AMOUNT);
		BigDecimal requiredAmount = (BigDecimal) map.get(REQUIRED_AMOUNT);

		// "2018-07-29T04:06:55.310+10:00
		messageTypeIdMap.put(TRADE_DATE, formatDateTime);
		messageTypeIdMap.put(EXCESS_AMOUNT, excessAmount.longValue());
		messageTypeIdMap.put(MARGIN_AMOUNT, marginAmount.longValue());
		messageTypeIdMap.put(REQUIRED_AMOUNT, requiredAmount.longValue());
		return messageTypeIdMap;
	}
}