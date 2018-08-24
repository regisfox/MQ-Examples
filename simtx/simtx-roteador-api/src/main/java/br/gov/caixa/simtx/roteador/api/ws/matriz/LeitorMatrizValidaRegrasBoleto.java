package br.gov.caixa.simtx.roteador.api.ws.matriz;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.Singleton;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.util.exception.ServicoException;

@Singleton
public class LeitorMatrizValidaRegrasBoleto {

	private static final Logger logger = Logger.getLogger(LeitorMatrizValidaRegrasBoleto.class);
	
	@Inject
	private FornecedorDados fornecedorDados;
	
	private static final int QUANTIDADE_PARAMETROS_VALIDA_REGRAS_BOLETO = 10;
	
	private LeitorMatriz leitorMatriz = new LeitorMatriz(";");
	
	@Inject
	private LeitorMatrizConsultaRegrasBoleto consultaRegrasBoleto;
	
	private Map<String, String> linhasMatriz = new HashMap<>();

	
	public Entry<String, String> processaRegraBoletoEntrada(String linha, String parametroAdicional)
			throws ServicoException {
		try {
	        String caminhoHomeSimtx = System.getProperty(Constantes.CAMINHO_HOME_SIMTX);
	
	        if (caminhoHomeSimtx == null) {
	        	logger.error("Variavel pasta.simtx.home nao definida");
	        	throw new NullPointerException("Variavel pasta.simtx.home nao definida");
	        }
	        
			String retornoConsultaRegrasBoleto = this.consultaRegrasBoleto.processaRegraBoletoEntrada(linha);
	        
			String chave = leitorMatriz.serializarParametrosLinhaMatriz(
					LeitorMatrizConsultaRegrasBoleto.QUANTIDADE_PARAMETROS_CONSULTA_REGRAS_BOLETO, linha);
			chave += retornoConsultaRegrasBoleto + ";" + parametroAdicional + ";";
	        		
	        if(linhasMatriz.isEmpty()) {
				linhasMatriz = leitorMatriz.lerMatrizRegrasBoleto(QUANTIDADE_PARAMETROS_VALIDA_REGRAS_BOLETO,
						caminhoHomeSimtx);
	        }
	        
	        logger.info("Chamando WS2 com chave = " + chave);
			String retornoValidaRegrasBoleto = linhasMatriz.get(chave);
	
			if (retornoValidaRegrasBoleto == null) {
				logger.error("Nenhum resultado encontrado na matriz");
				Mtxtb006Mensagem mensagem = this.fornecedorDados
						.buscarMensagem(MensagemRetorno.WS_NAO_RECUPEROU_DADOS_MATRIZ);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
			
			return new AbstractMap.SimpleEntry<>(retornoConsultaRegrasBoleto, retornoValidaRegrasBoleto);
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (IOException e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.WS_NAO_RECUPEROU_DADOS_MATRIZ);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}

	public void setConsultaRegrasBoleto(LeitorMatrizConsultaRegrasBoleto consultaRegrasBoleto) {
		this.consultaRegrasBoleto = consultaRegrasBoleto;
	}
	
	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}
}