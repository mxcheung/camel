package com.baeldung.camel.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baeldung.camel.model.MyRoute;
import com.baeldung.camel.repo.MyRouteRepository;

@Service
public class RouteServiceImpl implements RouteService {


	@Autowired
	private MyRouteRepository myRouteRepository;


	@Override
	public List<MyRoute> getRoutes() {
		return  (List<MyRoute>) myRouteRepository.findAll();
	}
	
	@Override
	public void loadRoutes() {
		List<MyRoute> routes = new ArrayList<MyRoute>();
		routes.add(new MyRoute("in-out-rabbit-q1-route",
				"file://C:/in/?fileName=MyFile.txt&charset=utf-8", 
				"rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q1&autoDelete=false",
				"{\"routeId\":\"in-out-rabbit-q1-route\"}"));

		routes.add(new MyRoute("in2-rabbit-q2-route",
				"file://C:/in2/?fileName=MyFile.txt&charset=utf-8", 
				"rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q2&autoDelete=false",
				"{\"routeId\":\"in2-rabbit-q2-route\"}"));
				
		routes.add(new MyRoute("in3-in2-route",
				"file://C:/in3/?fileName=MyFile.txt&charset=utf-8",
				"file://C:/in2/?fileName=MyFile.txt&charset=utf-8",
				"{\"routeId\":\"in3-in2-route\",\"params\":{\"tracing\":\"true\",\"LogMessage\":\"Logging body >>> ${body}\"}}"));

		
	  //  private final static String JSON_STRING = "{\"routeId\":\"routeId\",\"params\":{\"tracing\":\"true\",\"LogMessage\":\">>> ${body}\"}}";

		
//		routes.add(new MyRoute("in5-ftp-route",
//				"file://C:/in5/?charset=utf-8",
//				"ftp://anonymous@speedtest.tele2.net/upload?password=anonymous&ftpClient.dataTimeout=30000"));

		

		
		routes.add(new MyRoute("in5-ftp-route",
				"file://C:/in5/?charset=utf-8",
				"ftp://dlpuser@dlptest.com@ftp.dlptest.com/upload?password=3D6XZV9MKdhM5fF&ftpClient.dataTimeout=30000",
				"{\"routeId\":\"in5-ftp-route\",\"params\":{\"LogMessage\":\">> ${body}\"}}"));
		
		routes.add(new MyRoute("in4-rabbit-q2-route",
				"file://C:/in4/?fileName=MyFile.txt&charset=utf-8", 
				"rabbitmq://localhost:5672/exchange1?username=guest&password=guest&queue=q2&autoDelete=false",
				"{\"routeId\":\"in4-rabbit-q2-route\",\"params\":{\"LogMessage\":\">>> ${body}\"}}"));
		


		routes.add(new MyRoute("in6-mail-route",
				"file://C:/in6/?charset=utf-8",
				"smtp://mycompany.mailserver:30?password=tiger&username=scott",
				"{\"routeId\":\"in2-rabbit-q2-route\"}"));


				
		myRouteRepository.save(routes);

	}

	
	

}