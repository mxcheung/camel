package com.maxcheung.camelsimple.route;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.maxcheung.camelsimple.util.XmlToJsonHelper;

public class SGXMessageProcessor implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(SGXMessageProcessor.class);

    private XmlToJsonHelper xmlToJsonHelper = new XmlToJsonHelper();
    private Map<String, String> messageTypeIdMap = getMessageTypeIdMap();

    public void process(Exchange exchange) {
		String body = exchange.getIn().getBody(String.class);
		try {
			Document xmlDoc = xmlToJsonHelper.xmltoDoc(body);
			String messageType = xmlToJsonHelper.getRootName(xmlDoc);
			String id = findNodeValue(messageType, xmlDoc);
			exchange.getOut().setHeader("messageType", messageType);
			exchange.getOut().setHeader("id", id);
		} catch (Exception e) {
			LOG.error("Exception ocurred converting message to document body : {} , error : {}", body, e);
		}
	}


	private String findNodeValue(String messageType, Document xmlDoc) throws XPathExpressionException {
		String keyNode = messageTypeIdMap.get(messageType);
		String id = "";
		if (keyNode != null) {
			id = xmlToJsonHelper.findNodeValue(keyNode, xmlDoc);
		}
		return id;
	}

	private Map<String, String> getMessageTypeIdMap() {
		Map<String, String> messageTypeIdMap = new HashMap<String, String>();
		messageTypeIdMap.put("Company", "AccountReference");
		messageTypeIdMap.put("customer", "customer-id");
		return messageTypeIdMap;
	}


}