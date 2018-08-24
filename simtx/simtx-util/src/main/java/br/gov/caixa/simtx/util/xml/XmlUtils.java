/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.xml;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils {

	public static final String FORMATADOR_DATA_XML = "yyyy-MM-dd";

	private XmlUtils() {}
	
	public static boolean isXml(String xml) {
		return xml.trim().startsWith("<?xml") || xml.trim().startsWith("<");
	}

	public static Node procurarHeader(Document document) {
		Node node = document.getElementsByTagName("HEADER").item(0);
		
		if(node == null) {
			node = document.getElementsByTagName("header").item(0); 
		} else {
			return node;
		}
		
		if(node != null) return node;
		
		List<String> nsLista = getNameSpaces(document);
		
		for(String ns : nsLista) {
			node = document.getElementsByTagName(ns+":HEADER").item(0); 
			if(node == null) {
				node = document.getElementsByTagName(ns+":header").item(0);
			} else {
				return node;
			}
			
			if(node != null) return node;
		}
		
		return null;
	}
	
	public static List<String> getNameSpaces(Document document) {
		List<String> nsList = new ArrayList<>();

		NodeList todosNodes = document.getElementsByTagName("*");
		for (int j = 0; j < todosNodes.getLength(); j++) {
			NamedNodeMap attrs = todosNodes.item(j).getAttributes();
			for (int i = 0; i < attrs.getLength(); i++) {
				String attrName = attrs.item(i).getNodeName();
				if (attrName.matches("xmlns\\:.+")) {
					nsList.add(attrs.item(i).getNodeName().substring(6));
				}
			}
		}
		return nsList;
	}
}
