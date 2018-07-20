package com.maxcheung.camelsimple.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.maxcheung.camelsimple.model.RouteDef;

@Service
public class RouteServiceImpl implements RouteService {

	private  List<RouteDef> routeDefs;

	@Override
	public List<RouteDef> getRoutes() {
		return routeDefs;
	}



}