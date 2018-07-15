package com.baeldung.camel.repo;

import org.springframework.data.repository.CrudRepository;

import com.baeldung.camel.model.MyRoute;

public interface MyRouteRepository extends CrudRepository<MyRoute, Long> {
    
}