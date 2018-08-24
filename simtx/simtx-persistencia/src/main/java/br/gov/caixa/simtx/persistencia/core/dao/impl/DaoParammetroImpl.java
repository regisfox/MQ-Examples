/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoParammetro;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;


@Stateless
public class DaoParammetroImpl implements DaoParammetro {
	
	@PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
	

	@Override
	public Mtxtb023Parametro loadParam(long code) {
		try {
			return em.find(Mtxtb023Parametro.class, code);
		}
		catch (NoResultException e) {
			return null;
		} 
	}

	public void alterar(Mtxtb023Parametro parametro) {
		this.em.merge(parametro);
		this.em.flush();
	}
	

}
