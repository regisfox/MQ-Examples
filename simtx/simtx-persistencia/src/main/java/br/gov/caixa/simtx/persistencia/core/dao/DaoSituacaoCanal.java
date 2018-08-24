package br.gov.caixa.simtx.persistencia.core.dao;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb030SituacaoCanal;

public interface DaoSituacaoCanal {
	
	/**
	  * Grava a auditoria da atualizacao da situacao do Canal.
	  * 
	  * @param Mtxtb030SituacaoCanal
	  * @return
	  */
	public void salvaSituacaoCanal(Mtxtb030SituacaoCanal situacao);
}
