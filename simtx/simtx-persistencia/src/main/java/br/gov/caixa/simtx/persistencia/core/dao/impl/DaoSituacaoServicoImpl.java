package br.gov.caixa.simtx.persistencia.core.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoSituacaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb029SituacaoVersaoServico;

public class DaoSituacaoServicoImpl implements DaoSituacaoServico{

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
	
	public void salvaSituacaoServico(Mtxtb029SituacaoVersaoServico situacao) {
		try {
			em.persist(situacao);
		} 
		catch (Exception e) {
			e.getStackTrace();
		}
	}
}
