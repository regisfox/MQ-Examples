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

import br.gov.caixa.simtx.persistencia.core.dao.DaoMeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;


@Stateless
public class DaoMeioEntradaImpl implements DaoMeioEntrada {
    
    
    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private transient EntityManager em;

    
    
    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public Mtxtb008MeioEntrada buscarPorPK(Mtxtb008MeioEntrada meioEntrada) {
        try {
            return em.find(Mtxtb008MeioEntrada.class, meioEntrada.getNuMeioEntrada());
        }
        catch (NoResultException nre) {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public Mtxtb008MeioEntrada buscarPorNome(Mtxtb008MeioEntrada meioEntrada) {
        return this.em.createNamedQuery("Mtxtb008MeioEntrada.buscarPorNome", Mtxtb008MeioEntrada.class)
				.setParameter("noMeioEntrada", meioEntrada.getNoMeioEntrada())
				.getSingleResult();
    }
}
