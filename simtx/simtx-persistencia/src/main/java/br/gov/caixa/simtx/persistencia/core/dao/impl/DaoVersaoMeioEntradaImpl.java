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

import br.gov.caixa.simtx.persistencia.core.dao.DaoVersaoMeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb009VrsoMeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb009VrsoMeioEntradaPK;


@Stateless
public class DaoVersaoMeioEntradaImpl implements DaoVersaoMeioEntrada {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    

    @Override
    public Mtxtb009VrsoMeioEntrada buscarPorPK(Mtxtb009VrsoMeioEntradaPK versaoMeioEntradaPK) {
        try {
            return em.find(Mtxtb009VrsoMeioEntrada.class, versaoMeioEntradaPK);
        }
        catch (NoResultException nre) {
            return null;
        }
    }
}
