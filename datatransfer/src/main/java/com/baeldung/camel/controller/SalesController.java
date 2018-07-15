package com.baeldung.camel.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.camel.model.Sales;
import com.baeldung.camel.service.SalesService;

@Controller
@RestController
@RequestMapping("sales")
public class SalesController {

	private static final Logger LOG = LoggerFactory.getLogger(SalesController.class);

	@Autowired
	private SalesService salesService;

	@RequestMapping(value = "/getSalesX", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public @ResponseBody List<Sales> getSalesX() {
		LOG.info(" getSales");
		return salesService.getSales();
	}

	@RequestMapping(value = "/postSalesX", method = RequestMethod.POST, 
			consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public @ResponseBody List<Sales> postSalesX(@RequestBody List<Sales> sales) {
		LOG.info(" getSales");
		return sales;
	}

	@RequestMapping(value = "/getSales", method = RequestMethod.GET)
	public List<Sales> getSales() {
		LOG.info(" getSales");
		return salesService.getSales();
	}

	@RequestMapping(value = "/loadSales", method = RequestMethod.GET)
	public void loadSales() {
		LOG.info(" loadSales");
		salesService.loadSales();
	}

}