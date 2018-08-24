package br.gov.caixa.simtx.persistencia.core.dao;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb029SituacaoVersaoServico;

public interface DaoSituacaoServico {

	/**
	  * Grava a auditoria da atualizacao da situacao do servico.
	  * 
	  * @param Mtxtb029SituacaoVersaoServico
	  * @return
	  */
	public void salvaSituacaoServico(Mtxtb029SituacaoVersaoServico situacao);

}
