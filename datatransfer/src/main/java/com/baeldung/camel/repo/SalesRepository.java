package com.baeldung.camel.repo;

import org.springframework.data.repository.CrudRepository;

import com.baeldung.camel.model.Sales;

public interface SalesRepository extends CrudRepository<Sales, Long> {


    
}