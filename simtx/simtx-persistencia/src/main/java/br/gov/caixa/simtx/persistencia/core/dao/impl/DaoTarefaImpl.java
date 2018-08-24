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

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.core.dao.DaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb002Tarefa;

@Stateless
public class DaoTarefaImpl implements DaoTarefa {
	
	@PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
	private EntityManager em;

	public static final Logger logger = Logger.getLogger(DaoTarefaImpl.class);
	
	@Override
	public Mtxtb002Tarefa buscarPorPK(Mtxtb002Tarefa tarefa) {
		return em.find(Mtxtb002Tarefa.class, tarefa.getNuTarefa());
	}

	@Override
	public List<Mtxtb002Tarefa> findAll() {
		 return this.em.createNamedQuery("Mtxtb002Tarefa.findAll", Mtxtb002Tarefa.class).getResultList();
	}
}
