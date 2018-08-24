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

import br.gov.caixa.simtx.persistencia.core.dao.DaoServicoTarefaCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb020SrvcoTarfaCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb020SrvcoTarfaCanalPK;


@Stateless
public class DaoServicoTarefaCanalImp implements DaoServicoTarefaCanal {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;

    public List<Mtxtb020SrvcoTarfaCanal> buscarTarefasPorServicoCanal(Mtxtb020SrvcoTarfaCanalPK servicoTarefaCanalPK) {
		return em
				.createNamedQuery("Mtxtb020SrvcoTarfaCanal.buscarTarefasPorServicoCanal", Mtxtb020SrvcoTarfaCanal.class)
				.setParameter("nuServico", servicoTarefaCanalPK.getNuServico003())
				.setParameter("nuVersaoServico", servicoTarefaCanalPK.getNuVersaoServico003())
				.setParameter("nuCanal", servicoTarefaCanalPK.getNuCanal004()).getResultList();
    }

}
