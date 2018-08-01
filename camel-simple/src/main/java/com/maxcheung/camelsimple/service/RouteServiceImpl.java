package com.maxcheung.camelsimple.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxcheung.camelsimple.model.RouteDef;
import com.maxcheung.camelsimple.repo.RouteDefRepository;
import com.maxcheung.camelsimple.route.DefaultRouteBuilder;
import com.maxcheung.camelsimple.route.KibanaRouteBuilder;
import com.maxcheung.camelsimple.route.WireTapRouteBuilder;

@Service
public class RouteServiceImpl implements RouteService {

	private static final String CAMELSIMPLE_ROUTE_PATH = "camelsimple.route.path";

	private static final Logger LOG = LoggerFactory.getLogger(RouteServiceImpl.class);

	private final CamelContext camelContext;
	private final Environment env;
	private final ResourceLoader resourceLoader;
	private List<RouteDef> routeDefs;
	private ObjectMapper mapper = new ObjectMapper();

	private RouteDefRepository routeDefRepository;
	
	@Autowired
	public RouteServiceImpl(Environment env, CamelContext camelContext, RouteDefRepository routeDefRepository, ResourceLoader resourceLoader) {
		this.env = env;
		this.camelContext = camelContext;
		this.routeDefRepository = routeDefRepository;
		this.resourceLoader = resourceLoader;
		loadRoutes();
	}

	public void loadRoutes() {
		try {
			this.routeDefs = initRoute();
			routeDefRepository.save(routeDefs);
		} catch (Exception e) {
			LOG.error("Exception ocurred loading routes {}", e);
		}
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
	public Resource[] getFiles(String locationPattern) throws IOException {
		try (Stream<Path> paths = Files.walk(Paths.get(locationPattern))) {
			paths.filter(Files::isRegularFile).forEach(System.out::println);
		}
		Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader())
				.getResources(locationPattern);
		return resources;
	}

	private RoutesBuilder getRouteBuilder(RouteDef routeOptions) {
		RoutesBuilder routesBuilder;
		if ("WIRETAP".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new WireTapRouteBuilder(camelContext, routeOptions);
		} else if ("KIBANA".equalsIgnoreCase(routeOptions.getRouteType())) {
			routesBuilder = new KibanaRouteBuilder(camelContext, routeOptions);
		} else {
			routesBuilder = new DefaultRouteBuilder(camelContext, routeOptions);
		}
		return routesBuilder;
	}

	Resource[] loadResources(String pattern) throws IOException {
		return ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader()).getResources(pattern);
	}

	public List<RouteDef> initRoute() throws Exception {
		List<RouteDef> routes = new ArrayList<RouteDef>();
		String resourcePath = env.getProperty(CAMELSIMPLE_ROUTE_PATH);

		String contents2 = resourceToString(resourcePath);
		List<String> fileNames = new ArrayList<String>(Arrays.asList(contents2.split("\n")));
		// List<String> fileNames =
		// IOUtils.readLines(getClass().getResourceAsStream(resourcePath),Charsets.UTF_8);

		LOG.info("loading routes fileNames {}", fileNames);
		LOG.info("Loading route fileNames.size {}", fileNames.size());
		for (String fileName : fileNames) {
			String resourceFileName = resourcePath + fileName;
			LOG.info("Loading route {}", resourceFileName);
			// Path path =
			// Paths.get(getClass().getClassLoader().getResource(resourceFileName).toURI());
			// String content =
			// IOUtils.toString(getClass().getResourceAsStream(resourceFileName),Charsets.UTF_8);
			String content = resourceToString(resourceFileName);
			// Path path = Paths.get(getClass().getResource(resourceFileName).toURI());
			// String content = new String(Files.readAllBytes(path));
			RouteDef routeDef = mapper.readValue(content, RouteDef.class);
			camelContext.addRoutes(getRouteBuilder(routeDef));
			routes.add(routeDef);
		}
		/*
		 * LOG.info("Loading route resourcePath {}", resourcePath); String resourcePath2
		 * = "./src/main/resources"; Stream<Path> pathsStream =
		 * Files.walk(Paths.get("./src/main/resources"));
		 * 
		 * 
		 * pathsStream .filter(Files::isRegularFile) .forEach( name -> {
		 * System.out.println(name);
		 * 
		 * try { String content = new String(Files.readAllBytes(name)); RouteDef
		 * routeDef = mapper.readValue(content, RouteDef.class);
		 * camelContext.addRoutes(getRouteBuilder(routeDef)); routes.add(routeDef); }
		 * catch ( Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } } );
		 */
		Resource[] resources = loadResources(resourcePath);

		// Resource[] resources1 = loadResources("classpath*:" + "../route/dev/*.*" );
		// Resource[] resources2 = loadResources("classpath*:" + "../../route/dev/*.*"
		// );
		// Resource[] resources3 = loadResources("file:target/classes/route/dev/**" );
		// Resource[] resources4 = loadResources("classpath:" + "../resource/dev/*.*" );

		/*
		 * 
		 * LOG.info("Loading route resources.length {}", resources.length); for
		 * (Resource resource : resources) { Path path = Paths.get(resource.getURI());
		 * LOG.info("Loading route {}", resource.getURI()); String content = new
		 * String(Files.readAllBytes(path)); RouteDef routeDef =
		 * mapper.readValue(content, RouteDef.class);
		 * camelContext.addRoutes(getRouteBuilder(routeDef)); routes.add(routeDef); }
		 * List<String> fileNames =
		 * IOUtils.readLines(getClass().getClassLoader().getResourceAsStream(
		 * resourcePath), Charsets.UTF_8);
		 * 
		 */
		return routes;
	}

	private String resourceToString(String resourcePath) {
		String data = "";
		ClassPathResource cpr = new ClassPathResource(resourcePath);
		try {
			byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
			data = new String(bdata, StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOG.warn("IOException", e);
		}
		return data;
	}
}