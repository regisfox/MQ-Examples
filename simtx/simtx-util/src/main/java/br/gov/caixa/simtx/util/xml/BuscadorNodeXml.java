/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * The Class BuscadorNodeXml.
 */
public class BuscadorNodeXml extends BuscadorTextoXml {

	/**
	 * Instantiates a new buscador node xml.
	 *
	 * @param xml
	 *            the xml
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public BuscadorNodeXml(String xml) throws ParserConfigurationException, SAXException, IOException {
		super(xml);
	}

	/**
	 * Buscar texto.
	 *
	 * @param n
	 *            the n
	 * @param caminho
	 *            the caminho
	 * @return the string
	 */
	public String buscarTexto(Node n, String caminho) {
		String[] tags = caminho.split("/");
		Node text = buscarNode(n, tags, 0);

		if (text instanceof Text) {
			Text t = (Text) text;
			return t.getData();
		}
		return "";
	}

	/**
	 * Gets the ultima pos lista XML array.
	 *
	 * @param caminhoArray
	 *            the caminho array
	 * @param campo
	 *            the campo
	 * @return the ultima pos lista XML array
	 */
	public String getUltimaPosListaXMLArray(String caminhoArray, String campo) {
		NodeList nodeList = doc.getElementsByTagName(caminhoArray);
		int ultima = nodeList.getLength() - 1;
		Node node2 = nodeList.item(ultima);
		return buscarTexto(node2, campo);
	}

	/**
	 * Gets the primeira pos lista XML array.
	 *
	 * @param caminhoArray
	 *            the caminho array
	 * @param campo
	 *            the campo
	 * @return the primeira pos lista XML array
	 */
	public String getPrimeiraPosListaXMLArray(String caminhoArray, String campo) {
		NodeList nodeList = doc.getElementsByTagName(caminhoArray);
		Node node2 = nodeList.item(0);
		return buscarTexto(node2, campo);
	}

}
