package br.gov.caixa.simtx.roteador.api.ws.matriz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LeitorMatriz {

	private String divisorCsv;
	
	public LeitorMatriz(String divisorCsv) {
		this.divisorCsv = divisorCsv;
	}
	
	public String serializarParametrosLinhaMatriz(int quantidadeParametros, String linha) {
		StringBuilder camposPosicao = new StringBuilder();
		String[] campos = linha.split(this.divisorCsv);
		
		for(int i=0; i < quantidadeParametros; i++) {
			camposPosicao.append(campos[i] + ";");
		}
		
		return camposPosicao.toString();
	}
	
	private String serializarValorLinhaMatriz(int quantidadeParametros, String linha) {
		String[] campos = linha.split(this.divisorCsv);
		return campos[quantidadeParametros];
	}
	
	public Map<String, String> lerMatrizRegrasBoleto(int quantidadeParametros, String caminhoHomeSimtx) throws IOException  {
		String linha = null;
		HashMap<String, String> linhasMatriz = new HashMap<>();
		
		try (BufferedReader bufferedReader = new BufferedReader(
				new FileReader(caminhoHomeSimtx + "/docs/Regras-Pagamentos/Matriz.csv"))) {
			
			while ((linha = bufferedReader.readLine()) != null) {
				String parametro = serializarParametrosLinhaMatriz(quantidadeParametros, linha);
				String valor = serializarValorLinhaMatriz(quantidadeParametros, linha);

				linhasMatriz.put(parametro, valor);
			}
		} 
		
		return linhasMatriz;
	}
}
