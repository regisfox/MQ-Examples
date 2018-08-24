package br.gov.caixa.simtx.persistencia.core.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoSituacaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb030SituacaoCanal;

public class DaoSituacaoCanalImpl implements DaoSituacaoCanal {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    
	public void salvaSituacaoCanal(Mtxtb030SituacaoCanal situacao) {
		try {
			em.persist(situacao);
		} 
		catch (Exception e) {
			e.getStackTrace();
		}
	}
}
