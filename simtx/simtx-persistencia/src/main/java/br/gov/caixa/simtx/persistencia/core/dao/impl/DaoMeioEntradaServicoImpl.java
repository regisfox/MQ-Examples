/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoMeioEntradaServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvco;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvcoPK;


@Stateless
public class DaoMeioEntradaServicoImpl implements DaoMeioEntradaServico {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public Mtxtb018VrsoMeioEntraSrvco buscarPorPK(Mtxtb018VrsoMeioEntraSrvcoPK meioEntradaServicoPK) {
        return em.find(Mtxtb018VrsoMeioEntraSrvco.class, meioEntradaServicoPK);
    }

}
