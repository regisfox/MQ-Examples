/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoServicoTarefaRegras;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegras;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegrasPK;

@Stateless
public class DaoServicoTarefaRegrasImpl implements DaoServicoTarefaRegras {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    
    
    
    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public Mtxtb026ServicoTarefaRegras buscarPorPK(Mtxtb026ServicoTarefaRegrasPK regrasPK) {
        try {
        	return em.find(Mtxtb026ServicoTarefaRegras.class, regrasPK);
        }
        catch (NoResultException nre) {
        	return null;
        }
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public List<Mtxtb026ServicoTarefaRegras> buscarPorServicoTarefa(Mtxtb026ServicoTarefaRegrasPK regrasPK) {
		return this.em
				.createNamedQuery("Mtxtb026ServicoTarefaRegras.buscarPorServicoTarefa",
						Mtxtb026ServicoTarefaRegras.class)
				.setParameter("nuServico", regrasPK.getNuServico003())
				.setParameter("nuVersaoServico", regrasPK.getNuVersaoServico003())
				.setParameter("nuTarefa", regrasPK.getNuTarefa003())
				.setParameter("nuVersaoTarefa", regrasPK.getNuVersaoTarefa003()).getResultList();
    }
}
