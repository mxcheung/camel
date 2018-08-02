package com.maxcheung.camelsimple.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XmlToJsonHelperTest extends XmlToJsonHelperTst {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmlToJsonHelperTest.class);
	protected static final String SR_ARFI_FILEPATH = "src\\test\\resources\\arfi\\SR\\";

	@Before
	public void setup() {
		mapper = getObjectMapper();
		xmlHelper = getXmlToJsonHelper();
	}

	@Test
	public void shouldConvertXMltoJson()
			throws IOException, ParserConfigurationException, SAXException, JSONException, XPathExpressionException {
		String inputFile = SR_ARFI_FILEPATH + "Customer.xml";
		String json = getJsonfromXml(inputFile);
		LOGGER.info("Root element :" + json);
		assertTrue(json.contains("customer"));
		Document xmlDoc = xmltoDoc(inputFile);
		assertEquals("customer", getRootName(xmlDoc));
		assertEquals("12345", findNodeValue("customer-id", xmlDoc));
	}

	@Test
	public void shouldConvertActXMltoJson()
			throws IOException, ParserConfigurationException, SAXException, JSONException, XPathExpressionException {
		String inputFile = SR_ARFI_FILEPATH + "act.xml";
		String json = getJsonfromXml(inputFile);
		LOGGER.info("Root element :" + json);
		assertTrue(json.contains("Company"));
		Document xmlDoc = xmltoDoc(inputFile);
		Element root = xmlDoc.getDocumentElement();
		assertEquals("Company", root.getNodeName());
		assertEquals("INTE001", findNodeValue("AccountReference", xmlDoc));
		// NodeList nodelist = xmlDoc.getElementsByTagName("AccountReference");
		// String accountRef = nodelist.item(0).getChildNodes().item(0).getNodeValue();
		// assertEquals("INTE001",accountRef);
	}

	@Test
	public void shouldConvertKwsXMltoJson()
			throws IOException, ParserConfigurationException, SAXException, JSONException, XPathExpressionException {
		String inputFile = SR_ARFI_FILEPATH + "kws-arfi-20170327.xml";
		String json = getJsonfromXml(inputFile);
		LOGGER.info("Root element :" + json);
		assertTrue(json.contains("rfi"));
		Document xmlDoc = xmltoDoc(inputFile);
		Element root = xmlDoc.getDocumentElement();
		assertEquals("rfi", root.getNodeName());
		assertEquals("rfi", getRootName(xmlDoc));
		assertEquals("", findNodeValue("customer-id", xmlDoc));
	}


}