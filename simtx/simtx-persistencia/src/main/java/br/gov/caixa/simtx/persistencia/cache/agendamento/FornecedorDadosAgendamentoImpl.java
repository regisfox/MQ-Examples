package br.gov.caixa.simtx.persistencia.cache.agendamento;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.caixa.simtx.persistencia.agendamento.dao.DaoTransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.core.dao.DaoIteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class FornecedorDadosAgendamentoImpl implements FornecedorDadosAgendamento, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private DaoTransacaoAgendamento daoTransacaoAgendamento;
	
	@Inject
	private DaoIteracaoCanal daoIteracaoCanal;


	/**
	 * {@inheritDoc}
	 */
	public Mtxtb034TransacaoAgendamento salvarTransacaoAgendamento(Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		return this.daoTransacaoAgendamento.salvar(transacaoAgendamento);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamento(
			Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		return this.daoTransacaoAgendamento.buscaTransacoesAgendamento(transacaoAgendamento);
	}

	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoPorData(Date date){
		return daoTransacaoAgendamento.buscaTransacoesAgendamentoPorData(date);
	}

	/**
	 * {@inheritDoc}
	 */
	public Mtxtb034TransacaoAgendamento buscaTransacaoAgendamentoPorPK(
			Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		return this.daoTransacaoAgendamento.buscaTransacaoAgendamentoPorPK(transacaoAgendamento);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void alterarTransacao(Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		this.daoTransacaoAgendamento.alterar(transacaoAgendamento);
	}

	@Override
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoPeriodo(
			Mtxtb034TransacaoAgendamento transacaoAgendamento, Date periodoInicio, Date periodoFinal) {
		return this.daoTransacaoAgendamento.buscaTransacoesAgendamentoPeriodo(transacaoAgendamento, periodoInicio,
				periodoFinal);
	}

	@Override
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoNaoEfetivados(Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		return this.daoTransacaoAgendamento.buscaTransacoesAgendamentoNaoEfetivadas(transacaoAgendamento);
	}

	@Override
	public Mtxtb016IteracaoCanal buscaUltimoMtxtb016IteracaoCanal(long nsu) {
		return this.daoIteracaoCanal.buscaUltimoMtxtb016IteracaoCanal(nsu);
	}
}