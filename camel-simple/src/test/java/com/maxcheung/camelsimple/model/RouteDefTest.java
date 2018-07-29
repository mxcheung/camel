package com.maxcheung.camelsimple.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RouteDefTest {

	private final static String JSON_STRING = "{\"routeId\":\"routeId\",\"routeType\":\"DEFAULT\",\"from\":\"file://C:/in3/?fileName=MyFile.txt&charset=utf-8\",\"toUris\":[\"file://C:/in2A/\",\"file://C:/in2B/\"],\"log\":\">>> ${body}\",\"tracing\":\"true\",\"backOffMultiplier\":4.0,\"maximumRedeliveries\":5}";
	private final String[] toUris = { "file://C:/in2A/", "file://C:/in2B/" };

	private ObjectMapper mapper;

	@Before
	public void setup() {
		mapper = new ObjectMapper();
	}

	@Test
	public void shouldSerialize() throws JsonProcessingException {
		RouteDef routeDef = new RouteDef();
		routeDef.setRouteId("routeId");
		routeDef.setRouteType("DEFAULT");
		routeDef.setFrom("file://C:/in3/?fileName=MyFile.txt&charset=utf-8");
		routeDef.setToUris(toUris);
		routeDef.setLog(">>> ${body}");
		routeDef.setTracing("true");
		routeDef.setBackOffMultiplier(4);
		routeDef.setMaximumRedeliveries(5);
		String json = this.mapper.writeValueAsString(routeDef);
		assertEquals(JSON_STRING, json);
	}

	@Test
	public void shouldDeserialize() throws JsonParseException, JsonMappingException, IOException {
		RouteDef routeDef = mapper.readValue(JSON_STRING, RouteDef.class);
		assertEquals("routeId", routeDef.getRouteId());
		assertEquals("DEFAULT", routeDef.getRouteType());
		assertEquals("file://C:/in3/?fileName=MyFile.txt&charset=utf-8", routeDef.getFrom());
		assertEquals(2, routeDef.getToUris().length);
	}

	@Test
	public void shouldDeserializex() throws JsonParseException, JsonMappingException, IOException {
		ZonedDateTime now = ZonedDateTime.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	//	DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		String formatDateTime = now.format(formatter);
	}
	



}
