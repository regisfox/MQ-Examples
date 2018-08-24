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

import br.gov.caixa.simtx.persistencia.core.dao.DaoTarefaMeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntra;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntraPK;


@Stateless
public class DaoTarefaMeioEntradaImpl implements DaoTarefaMeioEntrada {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    

    @Override
    public List<Mtxtb010VrsoTarfaMeioEntra> buscarPorMeioEntrada(Mtxtb010VrsoTarfaMeioEntraPK tarefaMeioEntradaPK) {
		return em.createNamedQuery("Mtxtb010VrsoTarfaMeioEntra.buscarPorMeioEntrada", Mtxtb010VrsoTarfaMeioEntra.class)
				.setParameter("nuMeioEntrada", tarefaMeioEntradaPK.getNuMeioEntrada008()).getResultList();
    }
}
