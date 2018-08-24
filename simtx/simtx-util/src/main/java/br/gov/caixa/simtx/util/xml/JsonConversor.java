/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.xml;

import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConversor {

	public static String toXML(String jsonSimtx) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jackson = mapper.readTree(jsonSimtx);

			String xml = XML.toString(new JSONAdapter(jackson));
			xml = xml.replaceAll("<NAMESPACES.*/NAMESPACES>", "");

			for (JsonNode node : jackson.findValues("NAMESPACES").get(0)) {
				xml = xml.replaceFirst("SERVICO_ENTRADA",
						"SERVICO_ENTRADA " + node.get("NOME").asText().replaceAll("\"", "") + "=" + node.get("ENDERECO"));
			}

			return xml;
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	public static String toJSON(String xmlSimtx) {
	     JSONObject xmlJSONObj = XML.toJSONObject(xmlSimtx);
         String jsonPrettyPrintString = xmlJSONObj.toString(4);
         return jsonPrettyPrintString;
	}
}
