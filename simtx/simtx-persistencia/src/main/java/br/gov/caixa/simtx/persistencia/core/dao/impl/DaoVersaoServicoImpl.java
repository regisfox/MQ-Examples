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

import br.gov.caixa.simtx.persistencia.core.dao.DaoVersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServicoPK;


@Stateless
public class DaoVersaoServicoImpl implements DaoVersaoServico {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    

    @Override
    public Mtxtb011VersaoServico buscarPorPK(Mtxtb011VersaoServicoPK versaoServicoPK) {
    	return em.find(Mtxtb011VersaoServico.class, versaoServicoPK);
    }
    
    /**
     * {@inheritDoc} 
     * 
     */
    @Override
    public Mtxtb011VersaoServico buscarPorNomeOperacao(Mtxtb011VersaoServico versaoServico) {
    	Mtxtb011VersaoServico retorno = null;
    	try {
			retorno = this.em.createNamedQuery("Mtxtb011VersaoServico.buscarPorNomeOperacao", Mtxtb011VersaoServico.class)
					.setParameter("noServicoBarramento", versaoServico.getMtxtb001Servico().getNoServicoBarramento())
					.setParameter("noOperacaoBarramento", versaoServico.getMtxtb001Servico().getNoOperacaoBarramento())
					.setParameter("nuVersaoServico", versaoServico.getId().getNuVersaoServico())
					.getSingleResult();
        }
    	catch (NoResultException nre) {
    		return null;
        }
    	return retorno;
    }
    
	/**
	 * Sera repensado.
	 */
	public void alteraSituacaoServico(List<Mtxtb011VersaoServico> listaVersaoServicos) {
		for (final Mtxtb011VersaoServico versaoServico : listaVersaoServicos) {
			em.merge(versaoServico);
		}
	}

	@Override
	public List<Mtxtb011VersaoServico> buscarVersaoServicoPorNome(String nomeServico) {
		return this.em.createNamedQuery("Mtxtb011VersaoServico.buscarVersaoServicoPorNome", Mtxtb011VersaoServico.class)
				.setParameter("nomeServico", nomeServico)
				.getResultList();
	}

	@Override
	public List<Mtxtb011VersaoServico> buscarVersaoServicoPorSituacao(int situacaoVersaoServico) {
		return this.em.createNamedQuery("Mtxtb011VersaoServico.buscarVersaoServicoPorSituacao", Mtxtb011VersaoServico.class)
				.setParameter("situacaoServico", situacaoVersaoServico)
				.getResultList();
	}

	@Override
	public List<Mtxtb011VersaoServico> buscarTodosVersaoServico() {
		return this.em.createNamedQuery("Mtxtb011VersaoServico.findAll", Mtxtb011VersaoServico.class).getResultList();
	}
}
