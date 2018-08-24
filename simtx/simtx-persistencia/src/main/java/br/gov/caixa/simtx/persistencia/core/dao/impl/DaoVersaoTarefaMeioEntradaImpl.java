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

import br.gov.caixa.simtx.persistencia.core.dao.DaoVersaoTarefaMeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntra;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntraPK;


@Stateless
public class DaoVersaoTarefaMeioEntradaImpl implements DaoVersaoTarefaMeioEntrada {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mtxtb010VrsoTarfaMeioEntra> buscarTarefasMeioEntradaPorPK(
        Mtxtb010VrsoTarfaMeioEntraPK vrsoTarfaMeioEntraPK) {
		return em.createNamedQuery("Mtxtb010VrsoTarfaMeioEntra.buscarPorMeioEntrada", Mtxtb010VrsoTarfaMeioEntra.class)
				.setParameter("nuMeioEntrada", vrsoTarfaMeioEntraPK.getNuMeioEntrada008()).getResultList();
    }


}
