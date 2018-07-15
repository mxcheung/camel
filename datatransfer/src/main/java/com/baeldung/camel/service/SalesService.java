package com.baeldung.camel.service;

import java.util.List;

import com.baeldung.camel.model.Sales;

public interface SalesService {

	void loadSales();

	List<Sales> getSales();
	
}
