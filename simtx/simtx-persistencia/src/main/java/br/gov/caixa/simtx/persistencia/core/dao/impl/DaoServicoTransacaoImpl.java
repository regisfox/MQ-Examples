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

import br.gov.caixa.simtx.persistencia.core.dao.DaoServicoTransacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnso;

@Stateless
public class DaoServicoTransacaoImpl implements DaoServicoTransacao {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    

    @Override
    public Mtxtb017VersaoSrvcoTrnso salvar(Mtxtb017VersaoSrvcoTrnso versaoServicoTransacao) {
        em.persist(versaoServicoTransacao);
        em.flush();
        em.refresh(versaoServicoTransacao);
        return versaoServicoTransacao;
    }
    
    @Override
    public Mtxtb017VersaoSrvcoTrnso buscarPorNSU(Mtxtb017VersaoSrvcoTrnso versaoServicoTransacao) {
    	Mtxtb017VersaoSrvcoTrnso retorno = null;
    	try {
        	retorno = em
                    .createNamedQuery("Mtxtb017VersaoSrvcoTrnso.buscarPorNSU", Mtxtb017VersaoSrvcoTrnso.class)
                    .setParameter("nuNsuTransacao", versaoServicoTransacao.getMtxtb014Transacao().getNuNsuTransacao())
                    .getSingleResult();
        }
    	catch (NoResultException nre) {
            retorno = null;
        }
    	return retorno;
    }
}
