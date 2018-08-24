/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoPrmtoSrvcoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb005ServicoCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb013PrmtoSrvcoCanal;


@Stateless
public class DaoPrmtoSrvcoCanalImpl implements DaoPrmtoSrvcoCanal {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public Mtxtb013PrmtoSrvcoCanal buscarPorServicoCanal(Mtxtb005ServicoCanalPK servicoCanalPK) {
		return em.createNamedQuery("Mtxtb013PrmtoSrvcoCanal.buscarPorServicoCanal", Mtxtb013PrmtoSrvcoCanal.class)
				.setParameter("nuServico", servicoCanalPK.getNuServico001())
				.setParameter("nuCanal", servicoCanalPK.getNuCanal004()).getSingleResult();
    }
}
