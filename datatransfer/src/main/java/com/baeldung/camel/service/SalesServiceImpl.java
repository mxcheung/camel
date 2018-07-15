package com.baeldung.camel.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baeldung.camel.model.Sales;
import com.baeldung.camel.repo.SalesRepository;

@Service
public class SalesServiceImpl implements SalesService {

	@Autowired
	private SalesRepository salesRepository;

	@Override
	public List<Sales> getSales() {
		return  (List<Sales>) salesRepository.findAll();
	}


	@Override
	public void loadSales() {
		// TODO Auto-generated method stub
		List<Sales> sales = new ArrayList<Sales>();
		String account1 = "account1";
		String account2 = "account2";
		LocalDate tranDate1 = LocalDate.of(2018, 1, 1);
		LocalDate tranDate2 = LocalDate.of(2018, 1, 2);
		sales.add(new Sales(account1, tranDate1, "CASH", "AUD", BigDecimal.ONE));
		sales.add(new Sales(account1, tranDate1, "CASH", "USD", BigDecimal.TEN));
		sales.add(new Sales(account2, tranDate1, "CASH","USD", BigDecimal.TEN));
		sales.add(new Sales(account2, tranDate2, "EXCESS", null , BigDecimal.TEN));
		sales.add(new Sales(account2, tranDate2, "GST", null , BigDecimal.TEN));
		sales.add(new Sales(account2, tranDate2, "CASH", "AUD", BigDecimal.ONE));
		sales.add(new Sales(account2, tranDate2, "CASH", "USD", BigDecimal.TEN));
		salesRepository.save(sales);

	}

}