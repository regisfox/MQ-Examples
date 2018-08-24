package br.gov.caixa.simtx.web.configuracao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class CancelamentoTransacional {

	private static final Logger logger = Logger.getLogger(CancelamentoTransacional.class);

	public String envioTransacional(String uri, List<?> recebimento) {

		String retornoTransacional = "";
		
		try {
			
			Gson gson = new Gson();
			String produtoJson = gson.toJson(recebimento);

			logger.info("Produto serializado (json):");
			logger.info(produtoJson);

			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(80000);
			
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.write(produtoJson);
			out.close();

			int status = connection.getResponseCode();
			if (status / 100 != 2) {
				logger.error("Ocorreu algum erro. Codigo de reposta: " + status);
			}

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				while ((retornoTransacional = reader.readLine()) != null) {
					logger.info("Retorno do transacional: " + retornoTransacional);
					return retornoTransacional;
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return retornoTransacional;
	}
}
