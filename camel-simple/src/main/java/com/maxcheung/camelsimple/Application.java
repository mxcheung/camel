package com.maxcheung.camelsimple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.StandardEnvironment;

import com.github.ulisesbocchio.jar.resources.JarResourceLoader;

@SpringBootApplication
public class Application {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		LOG.info("Application starting ..." );
//		SpringApplication.run(Application.class, args);
        StandardEnvironment environment = new StandardEnvironment();
        new SpringApplicationBuilder()
            .sources(Application.class)
            .environment(environment)
            .resourceLoader(new JarResourceLoader(environment, "resources.extract.dir"))
            .build()
            .run(args);
	}

}