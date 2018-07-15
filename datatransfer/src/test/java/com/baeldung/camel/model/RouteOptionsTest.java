package com.baeldung.camel.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RouteOptionsTest {

    private final static String JSON_STRING = "{\"routeId\":\"routeId\",\"params\":{\"tracing\":\"true\",\"LogMessage\":\">>> ${body}\"}}";

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void shouldSerialize() throws JsonProcessingException {
    	RouteOptions routeOptions = new RouteOptions();
    	routeOptions.setRouteId("routeId");
    	Map<String, String> params = new HashMap<String, String>() ;
    	params.put("LogMessage",">>> ${body}");
    	params.put("tracing","true");
		routeOptions.setParams(params);
        String json = this.mapper.writeValueAsString(routeOptions);
        assertEquals(JSON_STRING, json);
    }

    @Test
    public void shouldDeserialize() throws JSONException, JsonParseException, JsonMappingException, IOException {
    	RouteOptions routeOptions = mapper.readValue(JSON_STRING, RouteOptions.class);
//        assertEquals(7, reportDefinition.getColumnDefinitions().size());
  //      assertEquals(APIR_CODE, columnDefinition.getTargetName());
    }
    
    
    
}
