package br.gov.caixa.simtx.processador.lote.expurgo.processador;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;

@Stateless
public class ProcessadorExpurgo implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ProcessadorExpurgo.class);

	@Inject
	private FornecedorDados fornecedorDados;

	@Inject
	private ProcessadorEnvioSicco processadorEnvioSicco;
	
	private static final String TABELAS = "MTXTB035_TRANSACAO_CONTA,MTXTB016_ITERACAO_CANAL,MTXTB015_SRVCO_TRNSO_TARFA,MTXTB017_VERSAO_SRVCO_TRNSO,MTXTB014_TRANSACAO";

	
	
	/**
	 * Inicia o processo de Expurgo das particoes via temporizador. Processo normal.
	 * 
	 */
	public void processarExpurgoTemporizador() {
		logger.info(" ==== Processo Expurgo Iniciado ==== ");
		try {
			Date diaAnterior = DataUtil.getDataAnterior(new Date());

			enviarTransacoesParaSicco(diaAnterior);

			realizarLimpeza();
		} 
		catch (Exception e) {
			logger.error(e);
		} 
		finally {
			logger.info(" ==== Processo Expurgo Finalizado ==== ");
		}
	}

	/**
	 * Realiza o processo do Expurgo das particoes manualmente pela chamada Rest.
	 * 
	 * @return {@link String}
	 */
	public String processarExpurgoManual() {
		logger.info(" ==== Processo Expurgo Manual Iniciado ==== ");
		String resposta = "";
		try {
			Date diaAnterior = DataUtil.getDataAnterior(new Date());

			enviarTransacoesParaSicco(diaAnterior);

			int qtdeParticoes = realizarLimpeza();
			resposta = "Total de tabelas excluidas: " + qtdeParticoes;
		} 
		catch (Exception e) {
			logger.error(e);
			resposta = e.getMessage();
		} 
		finally {
			logger.info(" ==== Processo Expurgo Manual Finalizado ==== ");
		}
		return resposta;
	}

	/**
	 * Verifica se ha transacoes para enviar ao Sicco.
	 * 
	 * @param diaAnterior
	 * @return
	 */
	private void enviarTransacoesParaSicco(Date diaAnterior) {
		try {
			logger.info("Verificando se ha transacoes pendentes para envio ao Sicco");
			if (this.fornecedorDados.possuiInformacoesParaEnvioCCO(diaAnterior)) {
				this.processadorEnvioSicco.processarEnvioPendentes();
			} 
			else {
				logger.info("Nao ha transacoes pendentes para envio");
			}
		} 
		catch (Exception e) {
			logger.error("Erro ao verificar se possui transacoes para envio ao Sicco", e);
		}
	}

	/**
	 * Executa a procedure q expurga as particoes.
	 * 
	 * @throws Exception
	 */
	private int realizarLimpeza() {
		logger.info("Expurgando particoes");
		int qtdeParticoes = this.fornecedorDados.limparParticoes(TABELAS);
		logger.info("Total de Particoes excluidas: " + qtdeParticoes);
		return qtdeParticoes;
	}


}