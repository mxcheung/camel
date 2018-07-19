package com.baeldung.camel.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RouteDefTest {

    private final static String JSON_STRING = "{\"routeId\":\"routeId\"}";

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void shouldSerialize() throws JsonProcessingException {
    	RouteDef routeOptions = new RouteDef();
    	routeOptions.setRouteId("routeId");
        String json = this.mapper.writeValueAsString(routeOptions);
        assertEquals(JSON_STRING, json);
    }

    @Test
    public void shouldDeserialize() throws JSONException, JsonParseException, JsonMappingException, IOException {
    	RouteDef routeOptions = mapper.readValue(JSON_STRING, RouteDef.class);
    }

    @Test
    public void shouldSerializeFileToFile() throws IOException {
    	RouteDef routeOptions = new RouteDef();
    	routeOptions.setRouteType("DEFAULT");
    	routeOptions.setRouteId("in3-in2-route");
    	routeOptions.setFrom("file://C:/in3/?fileName=MyFile.txt&charset=utf-8");
    	routeOptions.setToUris(getArray("file://C:/in3/?fileName=MyFile.txt&charset=utf-8"));
    	routeOptions.setLog(">>> ${body}");
    	routeOptions.setTracing("true");
        String json = this.mapper.writeValueAsString(routeOptions);
        RouteDef routeOptionsB  =  mapper.readValue(json, RouteDef.class);
    }
	
    private String[] getArray(String uri) {
    	return Arrays.asList(uri).stream().toArray(String[]::new);
	}

	private String[] getArray(List<String> inList) {
		List<String> places = Arrays.asList("file://C:/in2/?fileName=MyFile.txt&charset=utf-8");
    	return inList.stream().toArray(String[]::new);
	}
  
    @Test
    public void shouldSerializeFileToSmtp() throws JsonProcessingException {
    	RouteDef routeOptions = new RouteDef();
    	routeOptions.setRouteId("in6-mail-route");
    	routeOptions.setFrom("file://C:/in6/?charset=utf-8");
    	routeOptions.setToUris(getArray("smtp://mycompany.mailserver:30?password=tiger&username=scott"));
    	routeOptions.setLog(">>> ${body}");
    	routeOptions.setTracing("true");
        String json = this.mapper.writeValueAsString(routeOptions);
    }

    @Test
    public void shouldSerializeFileToRabbit() throws JsonProcessingException {
    	RouteDef routeOptions = new RouteDef();
    	routeOptions.setRouteId("in4-rabbit-q2-route");
    	routeOptions.setFrom("file://C:/in4/?fileName=MyFile.txt&charset=utf-8");
    	routeOptions.setToUris(getArray("rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q2&autoDelete=false"));
    	routeOptions.setLog(">>> ${body}");
    	routeOptions.setTracing("true");
        String json = this.mapper.writeValueAsString(routeOptions);
    }
    
    @Test
    public void shouldSerializeFileToFtp() throws JsonProcessingException {
    	RouteDef routeOptions = new RouteDef();
    	routeOptions.setRouteId("in5-ftp-route");
    	routeOptions.setFrom("file://C:/in5/?charset=utf-8");
    	routeOptions.setToUris(getArray("ftp://dlpuser@dlptest.com@ftp.dlptest.com/upload?password=3D6XZV9MKdhM5fF&ftpClient.dataTimeout=30000"));
    	routeOptions.setLog(">>> ${body}");
    	routeOptions.setTracing("true");
        String json = this.mapper.writeValueAsString(routeOptions);
    }
    
	/*
	 * routes.add(new MyRoute("in5-ftp-route", "file://C:/in5/?charset=utf-8",
	 * "ftp://dlpuser@dlptest.com@ftp.dlptest.com/upload?password=3D6XZV9MKdhM5fF&ftpClient.dataTimeout=30000",
	 * "{\"routeId\":\"in5-ftp-route\",\"params\":{\"LogMessage\":\">> ${body}\"}}")
	 * );
	 * 
	 * 
	 * 
	 * 
	 */
    

}
