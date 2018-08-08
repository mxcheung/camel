package com.maxcheung.camelsimple.route.processor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.util.LinkedCaseInsensitiveMap;

public class SqlProcessorTest extends CamelTestSupport {

	private static final String DIRECT_INPUT = "direct:input";
	private static final String REQUIRED_AMOUNT = "REQUIRED_AMOUNT";
	private static final String MARGIN_AMOUNT = "MARGIN_AMOUNT";
	private static final String EXCESS_AMOUNT = "EXCESS_AMOUNT";
	private static final String TRADE_DATE = "TRADE_DATE";

	@Test
	public void testKibanaProcessor() throws Exception {
		MockEndpoint mock = getMockEndpoint("mock:result");
		Map<String, Object> sqlResultSet = new LinkedCaseInsensitiveMap<>();
		java.sql.Date tradeDt = java.sql.Date.valueOf("2017-11-15");
		sqlResultSet.put(TRADE_DATE, tradeDt);
		sqlResultSet.put(REQUIRED_AMOUNT, BigDecimal.ONE);
		sqlResultSet.put(EXCESS_AMOUNT, BigDecimal.ONE);
		sqlResultSet.put(MARGIN_AMOUNT, BigDecimal.ONE);
		template.sendBody(DIRECT_INPUT, sqlResultSet);
		assertEquals(1, mock.getExchanges().size());
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) mock.getExchanges().get(0).getIn().getBody();
		ZonedDateTime zonedTradeDt = tradeDt.toLocalDate().atStartOfDay(ZoneOffset.systemDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		String formatDateTime = zonedTradeDt.format(formatter);
		assertEquals(formatDateTime, map.get(TRADE_DATE));
		assertEquals(BigDecimal.ONE.longValue(), map.get(MARGIN_AMOUNT));
		assertEquals(BigDecimal.ONE.longValue(), map.get(REQUIRED_AMOUNT));
		assertEquals(BigDecimal.ONE.longValue(), map.get(MARGIN_AMOUNT));
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(DIRECT_INPUT)
				.process(new SqlProcessor())
				.to("mock:result");

			}
		};
	}

}