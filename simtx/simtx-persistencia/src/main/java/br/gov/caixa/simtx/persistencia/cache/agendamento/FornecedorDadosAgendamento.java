package br.gov.caixa.simtx.persistencia.cache.agendamento;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;

@Local
public interface FornecedorDadosAgendamento {

	/**
	 * Salva a entidade MtxtbxxxTransacaoAgendamento
	 * 
	 * @param transacaoAgendamento
	 * @return
	 */
	public Mtxtb034TransacaoAgendamento salvarTransacaoAgendamento(Mtxtb034TransacaoAgendamento transacaoAgendamento);

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
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamento(
			Mtxtb034TransacaoAgendamento transacaoAgendamento);

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
	public void alterarTransacao(Mtxtb034TransacaoAgendamento transacaoAgendamento);
	
	/**
	 * Atualiza a entidade.
	 * 
	 * @param transacaoAgendamento
	 */
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoPeriodo(Mtxtb034TransacaoAgendamento transacaoAgendamento, Date periodoInicio, Date periodoFinal);

	/**
	 * Busca a transaçoes do Agendamento nao efetivados.
	 * 
	 * @param transacaoAgendamento
	 * @return Lis<Mtxtb034TransacaoAgendamento>
	 */
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoNaoEfetivados(Mtxtb034TransacaoAgendamento transacaoAgendamento);
	
	
	/**
	 * @param nsu
	 * @return
	 */
	public Mtxtb016IteracaoCanal buscaUltimoMtxtb016IteracaoCanal(long nsu);

	
}
