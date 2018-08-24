package br.gov.caixa.simtx.util.mock;

import javax.enterprise.inject.Alternative;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;

@Alternative
public class TratadorDeExcecaoMock extends TratadorDeExcecao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ProcessadorEnvioRetomadaTransacaoMock.class);

	@Override
	public void tratarExcecaoRetomada(DadosTransacaoComuns dadosTransacaoComuns, ServicoException se, ParametrosAdicionais parametrosAdicionais,
			boolean postaMensagemCanal) {
		logger.info("inicializando tratarExcecaoRetomada");
	}

	@Override
	public void tratarExcecaoRetomadaDesfazimentoTransacao(DadosTransacaoComuns dadosTransacaoComuns, ServicoException se,
			ParametrosAdicionais parametrosAdicionais, boolean postaMensagemCanal) {
		logger.info("inicializando tratarExcecaoRetomadaDesfazimentoTransacao");
	}

	@Override
	public void tratarExcecaoEfetivaAgendamentoPagamentoRetomadaDesfazimento(DadosTransacaoComuns dadosTransacaoComuns,
			Mtxtb034TransacaoAgendamento transacaoAgendamento, ServicoException se, ParametrosAdicionais parametrosAdicionais, boolean isUltimaExecucao,
			boolean postaMensagemCanal) {
		logger.info("inicializando tratarExcecaoEfetivaAgendamentoPagamentoRetomadaDesfazimento");
	}
}