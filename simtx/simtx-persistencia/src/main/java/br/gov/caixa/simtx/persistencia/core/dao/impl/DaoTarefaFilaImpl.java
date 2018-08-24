/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoTarefaFila;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFila;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFilaPK;

@Stateless
public class DaoTarefaFilaImpl implements DaoTarefaFila {
	
	@PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    

	@Override
	public List<Mtxtb024TarefaFila> buscarTarefasFilas(Mtxtb024TarefaFilaPK tarefaPK) {
		return em.createNamedQuery("Mtxtb024TarefaFila.buscarTarefasFilas", Mtxtb024TarefaFila.class)
	                .setParameter("nuTarefa", tarefaPK.getNuTarefa012())
	                .setParameter("nuVersaoTarefa", tarefaPK.getNuVersaoTarefa012()).getResultList();
	}

}
