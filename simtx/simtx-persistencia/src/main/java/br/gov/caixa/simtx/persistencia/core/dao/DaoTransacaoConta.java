package br.gov.caixa.simtx.persistencia.core.dao;

import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;

public interface DaoTransacaoConta {

	public List<Mtxtb035TransacaoConta> buscaTransacao(Mtxtb035TransacaoConta transacaoConta);
	
	public Mtxtb035TransacaoConta salvar(Mtxtb035TransacaoConta transacaoConta);
	
	void atualizaStatusPagamento(Long nsuOrigem, Long icSituacao);

	public Mtxtb035TransacaoConta buscarPorNsu(Long nsu);

}
