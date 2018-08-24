/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.xml;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.gov.caixa.simtx.persistencia.constante.Constantes;

public class TransformadorXsl implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private transient TransformerFactory transformadorFactory;

	public TransformadorXsl() {
		this.transformadorFactory = TransformerFactory.newInstance();
	}

	public String transformar(String xml, String xsl, ParametroXsl... parametrosXls) throws TransformerException {
		StringWriter saidaEscritor = new StringWriter();
		Result saida = new StreamResult(saidaEscritor);

		Transformer transformador = transformadorFactory.newTransformer(new StreamSource(new StringReader(xsl)));
		transformador.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformador.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		for (ParametroXsl parametroXls : parametrosXls) {
			transformador.setParameter(parametroXls.getChave(), parametroXls.getValor());
		}

		transformador.transform(new StreamSource(new StringReader(xml)), saida);
		StringBuffer sb = saidaEscritor.getBuffer();
		
		return retirarTagsVazias(sb.toString());
	}
	
	private String retirarTagsVazias(String xml) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes(Constantes.ENCODE_PADRAO))));
			
			retirarTagsFilhasVazias(doc.getDocumentElement());
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);
		    
		    StringWriter resultadoString = new StringWriter();
		    StreamResult resultado = new StreamResult(resultadoString);
		    transformer.transform(source, resultado);
		    
			return resultadoString.toString();
		}catch (Exception e) {
			return xml;
		}
	}
	
	private void retirarTagsFilhasVazias(Element parentElement) {
		List<Element> toRemove = new LinkedList<>();

		NodeList children = parentElement.getChildNodes();
		int childrenCount = children.getLength();
		for (int i = 0; i < childrenCount; ++i) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) child;
				retirarTagsFilhasVazias(childElement);
				if (elementoEhVazio(childElement)) {
					toRemove.add(childElement);
				}
			}
		}

		for (Element childElement : toRemove) {
			parentElement.removeChild(childElement);
		}
		
		parentElement.normalize();
	}
	
	private boolean elementoEhVazio(Element element) {
		if (element.hasAttributes())
			return false;
		if (element.hasChildNodes())
			return false;
		NodeList children = element.getChildNodes();
		int childrenCount = children.getLength();
		for (int i = 0; i < childrenCount; ++i) {
			Node child = children.item(i);
			String value = child.getNodeValue();
			if (value != null) {
				return false;
			}
			if(child.getFirstChild() != null && children.item(0).getFirstChild().getNodeValue() != null) {
				return false;
			}
		}
		return true;
	}
	
	public static String xmlRemoveTagCabecalho(String xml) {
		String novo = "";
		if (null!= xml) {
			novo =  xml.replaceAll("<\\?xml.*\\?>", "");
		}
		return novo;
	}
	
}
