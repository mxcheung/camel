package com.maxcheung.camelsimple.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxcheung.camelsimple.model.RouteDef;
import com.maxcheung.camelsimple.model.SgxMargin;
import com.maxcheung.camelsimple.repo.RouteDefRepository;
import com.maxcheung.camelsimple.repo.SgxMarginRepository;
import com.maxcheung.camelsimple.route.DefaultRouteBuilder;
import com.maxcheung.camelsimple.route.SgxRouteBuilder;
import com.maxcheung.camelsimple.route.SlackRouteBuilder;
import com.maxcheung.camelsimple.route.SqlRouteBuilder;
import com.maxcheung.camelsimple.route.WireTapRouteBuilder;
import com.maxcheung.camelsimple.route.processor.BeanIOProcessor;
import com.maxcheung.camelsimple.route.processor.KafkaConsumerProcessor;
import com.maxcheung.camelsimple.route.processor.KafkaProcessor;
import com.maxcheung.camelsimple.route.processor.KibanaProcessor;
import com.maxcheung.camelsimple.route.processor.NoopProcessor;
import com.maxcheung.camelsimple.route.processor.QuickFixProcessor;
import com.maxcheung.camelsimple.route.processor.SgxFileMoverProcessor;
import com.maxcheung.camelsimple.route.processor.SgxProcessor;
import com.maxcheung.camelsimple.route.processor.SqlProcessor;

@Service
public class RouteServiceImpl implements RouteService {

	private static final String CAMELSIMPLE_ROUTE_PATH = "camelsimple.route.path";

	private static final Logger LOG = LoggerFactory.getLogger(RouteServiceImpl.class);

	private final CamelContext camelContext;
	private final Environment env;
	private List<RouteDef> routeDefs;
	private ObjectMapper mapper = new ObjectMapper();
	
	private RouteDefRepository routeDefRepository;
	private SgxMarginRepository sgxMarginRepository;
	
	@Autowired
	public RouteServiceImpl(Environment env, CamelContext camelContext, RouteDefRepository routeDefRepository, 
			SgxMarginRepository sgxMarginRepository) {
		this.env = env;
		this.camelContext = camelContext;
		this.routeDefRepository = routeDefRepository;
		this.sgxMarginRepository = sgxMarginRepository;
		loadRoutes();
	}

	public void loadRoutes() {
		try {
			this.routeDefs = initRoute();
			routeDefRepository.save(routeDefs);
			List<SgxMargin> sgxMargins = new ArrayList<SgxMargin>();
			sgxMargins.add(getSgxMargin(LocalDate.of(2018, 8, 05),  BigDecimal.valueOf(100000000),  BigDecimal.valueOf(75000000),  BigDecimal.valueOf(60000000)) );
			sgxMargins.add(getSgxMargin(LocalDate.of(2018, 8, 06),  BigDecimal.valueOf(100000000),  BigDecimal.valueOf(55000000),  BigDecimal.valueOf(60000000)) );
			sgxMargins.add(getSgxMargin(LocalDate.of(2018, 8, 07),  BigDecimal.valueOf(100000000),  BigDecimal.valueOf(74000000),  BigDecimal.valueOf(60000000)) );
			sgxMargins.add(getSgxMargin(LocalDate.of(2018, 8, 8),  BigDecimal.valueOf(100000000),  BigDecimal.valueOf(73000000),  BigDecimal.valueOf(60000000)) );
			sgxMargins.add(getSgxMargin(LocalDate.of(2018, 8, 9),  BigDecimal.valueOf(100000000),  BigDecimal.valueOf(33000000),  BigDecimal.valueOf(60000000)) );
			sgxMarginRepository.save(sgxMargins);
			
			Iterable<SgxMargin> margins = sgxMarginRepository.findAll();
			ObjectMapper mapper = new ObjectMapper();
			for (SgxMargin margin : margins) {
				mapper.writeValue(new File("c:\\temp\\"+ margin.getTradeDate() + "_file.json"), margin);

				
			}
		} catch (Exception e) {
			LOG.error("Exception ocurred loading routes {}", e);
		}
	}

	private SgxMargin getSgxMargin(LocalDate tradeDate,  BigDecimal marginAmount, BigDecimal excessAmount, BigDecimal requiredAmount) {
		SgxMargin sgxMargin = new SgxMargin();
		sgxMargin.setTradeDate(tradeDate);
		sgxMargin.setMarginAmount(marginAmount);
		sgxMargin.setExcessAmount(excessAmount);
		sgxMargin.setRequiredAmount(requiredAmount);
		return sgxMargin;
	}

	@Override
	public List<RouteDef> getRouteDefs() {
		return routeDefs;
	}

	@Override
	public List<String> getCamelRoutes() {
		List<String> routes = new ArrayList<String>();
		List<RouteDefinition> camelDefs = camelContext.getRouteDefinitions();
		for (RouteDefinition routeDefinition : camelDefs) {
			routes.add(routeDefinition.toString());
		}
		return routes;
	}

	@Override
	public List<File> getFiles(String locationPattern) throws IOException {
		List<File> filesInFolder = Files.walk(Paths.get(locationPattern))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
		return filesInFolder;
	}
	
	private RoutesBuilder getRouteBuilder(RouteDef routeOptions) {
		RoutesBuilder routesBuilder;
		if ("WIRETAP".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new WireTapRouteBuilder(camelContext, new NoopProcessor(), routeOptions);
		} else if ("SGXFILEMOVER".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new DefaultRouteBuilder(camelContext, new SgxFileMoverProcessor(), routeOptions);
		} else if ("SGX".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new SgxRouteBuilder(camelContext, new SgxProcessor(), routeOptions);
		} else if ("SQL".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new SqlRouteBuilder(camelContext, new SqlProcessor(), routeOptions);
		} else if ("KAFKA".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new DefaultRouteBuilder(camelContext, new KafkaProcessor(), routeOptions);
		} else if ("KAFKACONSUMER".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new DefaultRouteBuilder(camelContext, new KafkaConsumerProcessor(), routeOptions);
		} else if ("SLACK".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new SlackRouteBuilder(camelContext, new NoopProcessor(), routeOptions);
		} else if ("KIBANA".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new DefaultRouteBuilder(camelContext, new KibanaProcessor(), routeOptions);
		} else if ("BEANIO".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new DefaultRouteBuilder(camelContext, new BeanIOProcessor(), routeOptions);
		} else if ("QUICKFIX".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new DefaultRouteBuilder(camelContext, new QuickFixProcessor(), routeOptions);
		} else {
			routesBuilder = new DefaultRouteBuilder(camelContext, new NoopProcessor(), routeOptions);
		}
		return routesBuilder;
	}

	Resource[] loadResources(String pattern) throws IOException {
		return ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader()).getResources(pattern);
	}

	public List<RouteDef> initRoute() throws Exception {
		List<RouteDef> routes = new ArrayList<RouteDef>();
		String resourcePath = env.getProperty(CAMELSIMPLE_ROUTE_PATH);
		List<File> files = getFiles(resourcePath);
		LOG.info("loading routes files {}", files);
		LOG.info("Loading route files.size {}", files.size());
		for (File file : files) {
			LOG.info("Loading route {}", file.getAbsolutePath());
			 String content = new String(Files.readAllBytes(file.toPath()));
			RouteDef routeDef = mapper.readValue(content, RouteDef.class);
			camelContext.addRoutes(getRouteBuilder(routeDef));
			routes.add(routeDef);
		}

		return routes;
	}

}