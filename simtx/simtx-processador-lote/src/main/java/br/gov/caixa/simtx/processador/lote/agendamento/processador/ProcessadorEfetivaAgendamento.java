/*******************************************************************************
 * Copyright (C)  2018 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.processador.lote.agendamento.processador;

import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import br.gov.caixa.simtx.agendamento.enuns.ServicoAgendamentoEnum;
import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;
import br.gov.caixa.simtx.processador.lote.agendamento.vo.AgendamentoServico;
import br.gov.caixa.simtx.util.ConstantesAgendamento;

@Stateless
public class ProcessadorEfetivaAgendamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int URL_CONTEXT_REST = 15;
	private static final int URL_PATH_CONTEXT_AGENDAMENTO_EFETIVA = 16;

	private static final Logger logger = Logger.getLogger(ProcessadorEfetivaAgendamento.class);

	@Inject
	private FornecedorDados fornecedorDados;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	@Asynchronous
	public void verificarServico(List<Mtxtb034TransacaoAgendamento> listaAgendamentos, boolean isUltimaExecucao) {
		for (Mtxtb034TransacaoAgendamento agendamento : listaAgendamentos) {
			ServicoAgendamentoEnum servicoAgendamentoEnum = ServicoAgendamentoEnum.obterServicoFinal(agendamento.getNuServico(),
					agendamento.getNuVersaoServico());
			if (servicoAgendamentoEnum != null && (servicoAgendamentoEnum.getServicoOrigem() == ConstantesAgendamento.CODIGO_SERVICO_PAGAMENTO_BOLETO
					|| servicoAgendamentoEnum.getServicoOrigem() == ConstantesAgendamento.CODIGO_SERVICO_PAGAMENTO_BOLETO_NPC)) {
				this.montarChamadaRest(agendamento.getNuNsuTransacaoAgendamento(), servicoAgendamentoEnum.getServicoFinal(),
						servicoAgendamentoEnum.getVersaoServicoFinal(), isUltimaExecucao);
			}
		}
	}

	private void montarChamadaRest(Long nsuTransacaoOrigem, Long numServicoFinal, Integer numServicoVersaoFinal, boolean isUltimaExecucao) {
		try {

			AgendamentoServico agendamentoServico = new AgendamentoServico(nsuTransacaoOrigem, numServicoFinal, numServicoVersaoFinal, isUltimaExecucao);
			String dadosJson = montarDadosJson(agendamentoServico);
			realizarEnvio(dadosJson);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void realizarEnvio(String dadosJson) {
		
		try {
			Mtxtb023Parametro urlPath = fornecedorDados.buscarParametroPorPK(URL_CONTEXT_REST);
			Mtxtb023Parametro contexPath = fornecedorDados.buscarParametroPorPK(URL_PATH_CONTEXT_AGENDAMENTO_EFETIVA);

			URL url = new URL(urlPath.getDeConteudoParam()+contexPath.getDeConteudoParam());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(80000);
			
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.write(dadosJson);
			out.close();

			int status = connection.getResponseCode();
			if (status / 100 != 2) {
				logger.error("Ocorreu algum erro. Codigo de reposta: " + status);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private String montarDadosJson(Object agendamentoServico) {

		Gson gson = new Gson();
		String dados = gson.toJson(agendamentoServico);

		logger.info("Produto serializado (json):");
		logger.info(dados);

		return dados;
	}
	
	
}
