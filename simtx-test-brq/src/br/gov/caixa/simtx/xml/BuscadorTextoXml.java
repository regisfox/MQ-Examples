
package br.gov.caixa.simtx.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class BuscadorTextoXml {

	protected static Document doc;

	public BuscadorTextoXml(String xml) throws ParserConfigurationException, SAXException, IOException {
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(stream);
		doc.getDocumentElement().normalize();
	}

	public BuscadorTextoXml(byte[] xml) throws ParserConfigurationException, SAXException, IOException {
		this(new String(xml));
	}

	public static String buscarTexto(String caminho) {
		String[] tags = caminho.split("/");
		Node text = buscarNode(doc.getFirstChild(), tags, 0);

		if (text instanceof Text) {
			Text t = (Text) text;
			return t.getData();
		}
		return null;
	}

	/**
	 * Retorna o primeiro item do documento que corresponde ao tagName
	 * 
	 */
	public Node buscarNode(String tagName) {
		NodeList nl = doc.getElementsByTagName("*");
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeName().contains(tagName))
				return nl.item(i);
		}
		return null;
	}

	/**
	 * Retorna tags
	 * 
	 */
	public NodeList buscarNodes() {
		return doc.getElementsByTagName("*");
	}

	protected static Node buscarNode(Node node, String[] tags, int id) {
		NodeList nl = node.getChildNodes();
		String tag = tags[id];
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			String nodeName = n.getNodeName();
			if (nodeName.contains(tag)) {
				if (tags.length == id + 1) {
					return n.getFirstChild();
				} else {
					return buscarNode(n, tags, id + 1);
				}
			}
		}
		return null;
	}

	public static Node xpath(String caminho) {
		try {
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(caminho);

			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;

			return nodes.item(0);
		} catch (Exception e) {
			return null;
		}
	}

	public static NodeList xpathList(String caminho) {
		try {
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(caminho);

			return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (Exception e) {
			return null;
		}
	}

	public static String xpathTexto(String caminho) {
		try {
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(caminho);

			return (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (Exception e) {
			return null;
		}
	}

	public static String xpathTexto(String... caminhos) {
		for (String caminho : caminhos) {
			String retorno = xpathTexto(caminho);

			if (null != retorno && !retorno.equals(""))
				return retorno;
		}

		return "";
	}

	/**
	 * 
	 * Metodo para auxiliar a buscar uma tag numa arvore de nodes. TODO: Mover
	 * este metodo para uma classe auxiliadora
	 * 
	 * @param caminho
	 * @param node
	 * @return
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static String xpath(String caminho, Node node)
			throws ParserConfigurationException, XPathExpressionException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();
		doc.adoptNode(node);
		doc.appendChild(node);

		return (String) XPathFactory.newInstance().newXPath().evaluate(caminho, doc, XPathConstants.STRING);
	}

	/**
	 * Recebe um document de XML e grava no busdata. TODO: Mover este metodo
	 * para uma classe auxiliadora
	 * 
	 * @param doc
	 * @throws Exception
	 */
	public static String xmlTransform(Document doc) throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		System.out.println(output);
		return output;
	}

}
