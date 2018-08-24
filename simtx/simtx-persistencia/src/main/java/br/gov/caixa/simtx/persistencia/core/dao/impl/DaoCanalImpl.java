/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;

@Stateless
public class DaoCanalImpl implements DaoCanal {  

	
	private static final String STIAUCAO = "situacao";

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private transient EntityManager entityManager;


    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public Mtxtb004Canal buscarPorPK(Mtxtb004Canal canal) {
        return entityManager.find(Mtxtb004Canal.class, canal.getNuCanal());
    }
    
    /**
     * {@inheritDoc}
     * 
     */
	@Override
	public Mtxtb004Canal buscarPorSigla(Mtxtb004Canal canal) {
		try {
			return this.entityManager.createNamedQuery("Mtxtb004Canal.buscarPorSigla", Mtxtb004Canal.class)
					.setParameter("sigla", canal.getSigla()).getSingleResult();
		} 
		catch (NoResultException nre) {
			return null;
		}
	}
    
    /**
     * {@inheritDoc}
     * 
     */
    public List<Mtxtb004Canal> buscarPorSituacao(BigDecimal situacao)  {
        return this.entityManager.createNamedQuery("Mtxtb004Canal.buscarPorSituacao", Mtxtb004Canal.class)
				.setParameter(STIAUCAO, situacao)
				.getResultList();
    }

    /**
     * {@inheritDoc}
     * 
     */

	public void alteraSituacaoCanal(List<Mtxtb004Canal> listaCanais) {
		for (final Mtxtb004Canal canal : listaCanais) {
			entityManager.merge(canal);
		}
	}
	
    /**
     * Busca todos os canais.
     * 
     * @param canal
     * @throws Exception
     */
	public List<Mtxtb004Canal> buscarTodosCanais() {
		return this.entityManager.createNamedQuery("Mtxtb004Canal.buscarTodos", Mtxtb004Canal.class).getResultList();
	}
}
