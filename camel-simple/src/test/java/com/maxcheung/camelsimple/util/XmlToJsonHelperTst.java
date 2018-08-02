package com.maxcheung.camelsimple.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class XmlToJsonHelperTst {

	protected static final String MEMBER_COMMUNICATIONS = "Member Communications";
	public static final int PRETTY_PRINT_INDENT_FACTOR = 4;

	protected static final String ARFI_UI_FILEPATH = "src\\test\\resources\\arfi\\arfi-20170619-ui.xml";

	protected ObjectMapper mapper;

	protected XmlToJsonHelper xmlHelper;

	protected ObjectMapper getObjectMapper() {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	protected XmlToJsonHelper getXmlToJsonHelper() {
		return new XmlToJsonHelper();
	}

	protected String getJsonfromXml(String filepath)
			throws ParserConfigurationException, SAXException, IOException, JSONException {
		return xmlHelper.xmltoJson(new File(filepath));
	}

	protected Document xmltoDoc(String filepath)
			throws ParserConfigurationException, SAXException, IOException, JSONException {
		return xmlHelper.xmltoDoc(new File(filepath));
	}

	protected String findNodeValue(String nodeName, Document doc)
			throws ParserConfigurationException, SAXException, IOException, JSONException, XPathExpressionException {
		return xmlHelper.findNodeValue(nodeName, doc);
	}

	protected String getRootName(Document xmlDoc) {
		return xmlHelper.getRootName(xmlDoc);
	}

}
