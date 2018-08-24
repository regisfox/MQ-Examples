package br.gov.caixa.simtx.roteador.api.ws.matriz;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.roteador.api.util.ConstantesRoteadorWeb;
import br.gov.caixa.simtx.util.exception.ServicoException;

@Singleton
public class LeitorMatrizConsultaRegrasBoleto {

	private static final Logger logger = Logger.getLogger(LeitorMatrizConsultaRegrasBoleto.class);
	
	@Inject
	private FornecedorDados fornecedorDados;
	
	public static final int QUANTIDADE_PARAMETROS_CONSULTA_REGRAS_BOLETO = 8;
	
	private Map<String, String> linhasMatriz = new HashMap<>();
	
	private LeitorMatriz leitorMatriz = new LeitorMatriz(";");
	
	
	public String processaRegraBoletoEntrada(String parametrosEntrada) throws ServicoException {
		try {
	        String caminhoHomeSimtx = System.getProperty(Constantes.CAMINHO_HOME_SIMTX);
	
	        if (caminhoHomeSimtx == null) {
	        	logger.error("Variavel pasta.simtx.home nao definida");
	        	throw new NullPointerException("Variavel pasta.simtx.home nao definida");
	        }
	        
	        if(linhasMatriz.isEmpty()) {
				linhasMatriz = leitorMatriz.lerMatrizRegrasBoleto(QUANTIDADE_PARAMETROS_CONSULTA_REGRAS_BOLETO,
						caminhoHomeSimtx);
	        }
	        
			String retornoCanal = linhasMatriz.get(parametrosEntrada);
	
			if (retornoCanal == null) {
				logger.error("Nenhum resultado encontrado na matriz");
				retornoCanal = ConstantesRoteadorWeb.VALOR_CALCULADO;
			}
			return retornoCanal;
		} 
		catch (IOException e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.WS_NAO_RECUPEROU_DADOS_MATRIZ);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}


	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}
	
}
