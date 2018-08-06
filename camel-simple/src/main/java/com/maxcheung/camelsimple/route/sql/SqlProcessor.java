package com.maxcheung.camelsimple.route.sql;

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
		Date TRADE_DATE = (Date) map.get("TRADE_DATE");
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		ZonedDateTime zonedTradeDt = TRADE_DATE.toLocalDate().atStartOfDay(ZoneOffset.systemDefault());
		String formatDateTime = zonedTradeDt.format(formatter);
		BigDecimal EXCESS_AMOUNT = (BigDecimal) map.get("EXCESS_AMOUNT");
		BigDecimal MARGIN_AMOUNT = (BigDecimal) map.get("MARGIN_AMOUNT");
		BigDecimal REQUIRED_AMOUNT = (BigDecimal) map.get("REQUIRED_AMOUNT");

		// "2018-07-29T04:06:55.310+10:00
		messageTypeIdMap.put("TRADE_DATE", formatDateTime);
		messageTypeIdMap.put("EXCESS_AMOUNT", EXCESS_AMOUNT.longValue());
		messageTypeIdMap.put("MARGIN_AMOUNT", MARGIN_AMOUNT.longValue());
		messageTypeIdMap.put("REQUIRED_AMOUNT", REQUIRED_AMOUNT.longValue());
		return messageTypeIdMap;
	}
}