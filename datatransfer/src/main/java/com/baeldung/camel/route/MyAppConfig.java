package com.baeldung.camel.route;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAppConfig {

  @Autowired
  CamelContext camelContext;

}