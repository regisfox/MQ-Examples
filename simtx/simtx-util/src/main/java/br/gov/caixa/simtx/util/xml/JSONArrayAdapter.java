/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.xml;

import java.util.Iterator;

import org.json.JSONArray;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONArrayAdapter extends JSONArray {
	public JSONArrayAdapter(JsonNode jackson) {
		super();

		Iterator<JsonNode> elements = jackson.elements();
		while (elements.hasNext()) {
			try {
				this.put(new JSONAdapter(new ObjectMapper().readTree(elements.next().toString())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
