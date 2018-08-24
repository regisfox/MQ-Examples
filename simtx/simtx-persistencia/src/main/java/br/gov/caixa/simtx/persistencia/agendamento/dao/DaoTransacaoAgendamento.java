/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.agendamento.dao;

import java.util.Date;
import java.util.List;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;

public interface DaoTransacaoAgendamento {
	
	/**
	 * Grava a entidade.
	 * 
	 * @param transacaoAgendamento
	 * @return MtxtbxxxTransacaoAgendamento
	 */
	public Mtxtb034TransacaoAgendamento salvar(Mtxtb034TransacaoAgendamento transacaoAgendamento);
	
	/**
	 * Busca a entidade pelo Id.
	 * 
	 * @param transacaoAgendamento
	 * @return
	 */
	public Mtxtb034TransacaoAgendamento buscaTransacaoAgendamentoPorPK(Mtxtb034TransacaoAgendamento transacaoAgendamento);
	
	/**
	 * Busca a transaçoes do Agendamento.
	 * 
	 * @param transacaoAgendamento
	 * @return Lis<Mtxtb034TransacaoAgendamento>
	 */
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamento(Mtxtb034TransacaoAgendamento transacaoAgendamento);

	/**
	 * @param date
	 * @return
	 */
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoPorData(Date date);
	
	/**
	 * Atualiza a entidade.
	 * 
	 * @param transacaoAgendamento
	 */
	public void alterar(Mtxtb034TransacaoAgendamento transacaoAgendamento);

	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoPeriodo(Mtxtb034TransacaoAgendamento transacaoAgendamento, Date periodoInicio, Date periodoFinal);

	/**
	 * Busca a transaçoes do Agendamento.
	 * 
	 * @param transacaoAgendamento
	 * @return Lis<Mtxtb034TransacaoAgendamento>
	 */
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoNaoEfetivadas(Mtxtb034TransacaoAgendamento transacaoAgendamento);

}
