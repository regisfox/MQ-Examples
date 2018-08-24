package br.gov.caixa.simtx.persistencia.core.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoTransacaoAuditada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb036TransacaoAuditada;

public class DaoTransacaoAuditadaImpl implements DaoTransacaoAuditada {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    
	public Mtxtb036TransacaoAuditada salvarMtxtb036TransacaoAuditada(Mtxtb036TransacaoAuditada transacao) {
		em.persist(transacao);
		em.flush();
		return transacao;
	}

}
