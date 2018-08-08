package com.maxcheung.camelsimple.route.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.dataformat.beanio.BeanIOHeader;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BeanIOProcessor implements Processor {

	private StreamFactory factory = StreamFactory.newInstance();
	
	public BeanIOProcessor() {
		super();
		factory.load("./src/main/resources/beanio/mappings.xml");
	}

	public void process(Exchange exchange) throws Exception {

		System.out.println("OrderInputFileProcessor fixedlenght File coverting into xml file");
		// create a StreamFactory
		// load the mapping file
		// use a StreamFactory to create a BeanReader
		// use a StreamFactory to create a BeanReader
		String filename = exchange.getIn().getBody(GenericFile.class).getAbsoluteFilePath();
		File inputFile = new File(filename);
		BeanReader in = factory.createReader("employeeFile", inputFile);
		List<Object> results = new ArrayList<Object>();
		try {
			Object readObject;
			while ((readObject = in.read()) != null) {
				if (readObject instanceof BeanIOHeader) {
					exchange.getOut().getHeaders().putAll(((BeanIOHeader) readObject).getHeaders());
				}
				results.add(readObject);
			}
		} finally {
			in.close();
		}
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonAsString = objectMapper.writeValueAsString(results);
		exchange.getOut().setBody(jsonAsString);
	}

	// http://useof.org/java-open-source/org.beanio.BeanReader
}