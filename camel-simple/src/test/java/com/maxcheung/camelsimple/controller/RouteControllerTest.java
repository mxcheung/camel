package com.maxcheung.camelsimple.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxcheung.camelsimple.service.RouteService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RouteController.class, secure = false)
public class RouteControllerTest {

	private static final String ROUTES_BASE = "/routes/";
	private static final String CAMELSIMPLE_ROUTE_PATH = "camelsimple.route.path";
	private static final long DURATION_MILIS = 10000;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RouteService routeService;

	@Mock
	private ObjectMapper mapper;

	@Mock
	private Environment env;

	@Before
	public void setup() throws Exception {
	}

	@Test
	public void shouldGetRoutesDefinitions() throws Exception {
		MockHttpServletResponse response = getEndpoint("getRouteDefs");
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	public void shouldGetCamelRoutes() throws Exception {
		MockHttpServletResponse response = getEndpoint("getCamelRoutes");
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	private MockHttpServletResponse getEndpoint(String endpoint) throws JsonProcessingException, Exception {
		String uri = ROUTES_BASE + endpoint;
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		return result.getResponse();
	}

}
