package com.baeldung.camel.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baeldung.camel.model.MyRoute;
import com.baeldung.camel.model.RouteDef;
import com.baeldung.camel.repo.MyRouteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RouteServiceImpl implements RouteService {

	@Autowired
	private MyRouteRepository myRouteRepository;

	@Override
	public List<MyRoute> getRoutes() {
		return (List<MyRoute>) myRouteRepository.findAll();
	}

	@Override
	public void loadRoutes() {
		List<MyRoute> routes = new ArrayList<MyRoute>();

		/*
		 * routes.add(new MyRoute("in-out-rabbit-q1-route",
		 * "file://C:/in/?fileName=MyFile.txt&charset=utf-8",
		 * "rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q1&autoDelete=false",
		 * "{\"routeId\":\"in-out-rabbit-q1-route\"}"));
		 * 

		 * routes.add(new MyRoute("in2-rabbit-q2-route",
		 * "file://C:/in2/?fileName=MyFile.txt&charset=utf-8",
		 * "rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q2&autoDelete=false",
		 * "{\"routeId\":\"in2-rabbit-q2-route\"}"));
		 * 
		 * ));
		 */


		// routes.add(new MyRoute("in5-ftp-route",
		// "file://C:/in5/?charset=utf-8",
		// "ftp://anonymous@speedtest.tele2.net/upload?password=anonymous&ftpClient.dataTimeout=30000"));

		routes.add(getDef("in3-in2-route.json"));
		routes.add(getDef("in4-rabbit-q2-route.json"));
		routes.add(getDef("in5-ftp-route.json"));
		routes.add(getDef("in6-mail-route.json"));
		

		myRouteRepository.save(routes);

	}

	private MyRoute getDef(String fileName) {
		RouteDef routeDef = null;
		String content = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
			content = new String(Files.readAllBytes(path));
			routeDef = mapper.readValue(content, RouteDef.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MyRoute myRoute = new MyRoute(routeDef, content);
		return myRoute;
	}

}