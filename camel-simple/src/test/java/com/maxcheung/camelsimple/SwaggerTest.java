package com.maxcheung.camelsimple;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerTest {
    
	
	SwaggerConfig swaggerConfig;

	
	@Before
	public void setup() throws Exception {
		swaggerConfig =  new SwaggerConfig();
	}

	@Test
    public void shouldGetDocket() {
		Docket docket = swaggerConfig.api();
		assertEquals("CamelSimpleService",docket.getGroupName());
    }




}
