/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DadosBarramento implements Serializable {
	
	private static final long serialVersionUID = -1386557562644970531L;
	
	private String dados;
    
	private transient Document documento;
	
	private TransformadorXsl transformador;
	
	public DadosBarramento() {
		this.dados = " ";
		this.transformador = new TransformadorXsl();
	}
	
	public void escrever(String msg) {
		StringBuilder msgFmt = new StringBuilder();
		msgFmt.append(" ");
		msgFmt.append(dados);
		msgFmt.append(msg.replaceAll("<\\?xml.*\\?>", ""));
		msgFmt.append(" ");
		this.dados = msgFmt.toString();
		this.documento = null;
	}
	
	public String ler(String xsl, ParametroXsl ... parametrosXsl) throws TransformerException {
		return transformador.transformar(getDadosLeitura(), xsl, parametrosXsl);
	}

	public String getDadosLeitura() {
		String busTimeTag = new BusTime().getBusTime();
		return "<BUSDATA>" + busTimeTag + " " + dados + "</BUSDATA>";
	}
	
	public String getDados() {
		return dados;
	}
	
	public Node xpath(String caminho) {
		try {
			if(this.documento == null) montarDocumento();
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(caminho);

			return (Node) expr.evaluate(this.documento, XPathConstants.NODE);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public NodeList xpathNodes(String caminho) {
		try {
			if(this.documento == null) montarDocumento();
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(caminho);

			return (NodeList) expr.evaluate(this.documento, XPathConstants.NODESET);
		} catch (Exception e) {
			return null;
		}
	}
	
	public String xpathTexto(String caminho) {
		try {
			if(this.documento == null) montarDocumento();
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(caminho);

			return (String) expr.evaluate(this.documento, XPathConstants.STRING);
		} catch (Exception e) {
			return null;
		}
	}
	
	private void montarDocumento() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		this.documento = dBuilder.parse(new InputSource(new ByteArrayInputStream(getDadosLeitura().getBytes("utf-8"))));
		this.documento.getDocumentElement().normalize();
	}

	
}
