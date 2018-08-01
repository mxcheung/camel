package com.maxcheung.camelsimple.route;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxcheung.camelsimple.util.XmlToJsonHelper;

public class MyProcessor implements Processor {

	ObjectMapper objectMapper;
	XmlToJsonHelper xmlToJsonHelper = new XmlToJsonHelper();

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
/*		
		Document doc;
		try {
		 doc = xmlToJsonHelper.xmltoDoc(body);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/		
		map.put("post_date", formatDateTime);
		map.put("routeId", exchange.getFromRouteId());
		map.put("exchangeId", exchange.getExchangeId());
		map.put("content", body);
		return map;
	}

}