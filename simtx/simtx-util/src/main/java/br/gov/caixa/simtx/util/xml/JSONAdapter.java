/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.xml;

import java.io.IOException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONAdapter extends JSONObject {

	private JsonNode jackson;

	public JSONAdapter(JsonNode jackson) {
		this.jackson = jackson;
	}

	@Override
	public Iterator<String> keys() {
		if (jackson.isArray())
			return super.keys();
		else
			return jackson.fieldNames();
	}

	@Override
	public Object opt(String key) {
		return get(key);
	}

	@Override
	public Object get(String key) throws JSONException {
		JsonNode nested = jackson.get(key);
		if (nested.isObject()) {
			return new JSONAdapter(nested);
		} else if (nested.isArray()) {
			try {
				return new JSONArrayAdapter(new ObjectMapper().readTree(nested.toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (nested.isTextual()) {
			return nested.asText();
		} else if (nested.isNumber()) {
			return nested.asDouble();
		} else if (nested.isBoolean()) {
			return nested.asBoolean();
		}

		return null;
	}
}
