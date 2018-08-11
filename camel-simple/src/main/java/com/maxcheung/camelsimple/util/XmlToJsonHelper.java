package com.maxcheung.camelsimple.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlToJsonHelper {

	private static final int PRETTY_PRINT_INDENT_FACTOR = 4;
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlToJsonHelper.class);

	private XPathFactory factory = XPathFactory.newInstance();
	private XPath xpath = factory.newXPath();

	public String xmltoJson(File fXmlFile)
			throws ParserConfigurationException, SAXException, IOException, JSONException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		dbFactory.setValidating(false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		dbFactory.setCoalescing(true); // This removes CDATA Tag and converts it "
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		EntityResolver entityResolver = getEntityResolver();
		dBuilder.setEntityResolver(entityResolver);
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		Element element = doc.getDocumentElement();
		String json = nodeToJson(element);
		return json;
	}

	public Document xmltoDoc(File fXmlFile)
			throws ParserConfigurationException, SAXException, IOException, JSONException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		dbFactory.setValidating(false);
		// dbFactory.setNamespaceAware(true);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		dbFactory.setCoalescing(true); // This removes CDATA Tag and converts it "
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		EntityResolver entityResolver = getEntityResolver();
		dBuilder.setEntityResolver(entityResolver);
		Document doc = dBuilder.parse(fXmlFile);
		return doc;
	}

	public Document xmltoDoc(String xmlString)
			throws ParserConfigurationException, SAXException, IOException, JSONException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		// dbFactory.setNamespaceAware(true);
		// dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
		// false);
		// dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
		// dbFactory.setFeature("http://xml.org/sax/features/validation", false);
		// dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",
		// false);
		// dbFactory.setCoalescing(true); // This removes CDATA Tag and converts it "
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		// EntityResolver entityResolver = getEntityResolver();
		// dBuilder.setEntityResolver(entityResolver);
		InputSource is = new InputSource(new StringReader(xmlString));
		Document doc = dBuilder.parse(is);
		return doc;
	}

	public String getRootName(Document xmlDoc) {
		return xmlDoc.getDocumentElement().getNodeName();
	}

	public String findNodeValue(String nodeName, Document doc) throws XPathExpressionException {
		String expression = "//" + nodeName;
		Node node = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);
		// String accountRef = node.getChildNodes().item(0).getNodeValue();
		String nodeValue = "";
		if (node != null) {
			nodeValue = node.getChildNodes().item(0).getNodeValue();

		}
		return nodeValue;
	}

	private EntityResolver getEntityResolver() {
		EntityResolver entityResolver = new EntityResolver() {
			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				List<String> dtdEntityKeyWords = new ArrayList<String>();
				dtdEntityKeyWords.add("period_end");
				dtdEntityKeyWords.add("year");
				dtdEntityKeyWords.add("prev_qtr");
				dtdEntityKeyWords.contains(systemId);
				for (String dtdEntityKeyWord : dtdEntityKeyWords) {
					if (systemId.contains(dtdEntityKeyWord)) {
						return new InputSource(new StringReader(""));
					}
				}
				return null;
			}
		};
		return entityResolver;

	}

	private String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			LOGGER.error("nodeToString Transformer Exception", te);
		}
		return sw.toString();
	}

	public String nodeToJson(Node node) throws JSONException {
		String nodeStr = nodeToString(node);
		JSONObject xmlJSONObj = XML.toJSONObject(nodeStr);
		return xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
	}

}
