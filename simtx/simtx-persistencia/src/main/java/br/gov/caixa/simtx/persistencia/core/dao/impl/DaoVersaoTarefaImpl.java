/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoVersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefaPK;


@Stateless
public class DaoVersaoTarefaImpl implements DaoVersaoTarefa {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    

    /**
     * {@inheritDoc}
     */
    @Override
    public Mtxtb012VersaoTarefa buscarPorPK(Mtxtb012VersaoTarefaPK versaoTarefaPK) {
        return em.find(Mtxtb012VersaoTarefa.class, versaoTarefaPK);
    }
}
