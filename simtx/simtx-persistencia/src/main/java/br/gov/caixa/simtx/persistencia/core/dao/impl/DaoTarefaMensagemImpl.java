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

import br.gov.caixa.simtx.persistencia.core.dao.DaoTarefaMensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagemPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;



@Stateless
public class DaoTarefaMensagemImpl implements DaoTarefaMensagem {  
    
    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager entityManager;

    
    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public List<Mtxtb007TarefaMensagem> buscarAutorizadorasPorTarefa(Mtxtb007TarefaMensagemPK tarefaMensagemPK) {
    	return this.entityManager.createNamedQuery("Mtxtb007TarefaMensagem.buscarPorTarefa", Mtxtb007TarefaMensagem.class)
					.setParameter("nuTarefa", tarefaMensagemPK.getNuTarefa012())
					.setParameter("nuVersaoTarefa", tarefaMensagemPK.getNuVersaoTarefa012())
					.getResultList();
    }
    
    /**
     * {@inheritDoc}
     */
    public Mtxtb007TarefaMensagem buscarPorTarefaCodRetorno(String codigoMensagem, Mtxtb012VersaoTarefa versaoTarefa) throws SemResultadoException {
        try {
        	return this.entityManager.createNamedQuery("Mtxtb007TarefaMensagem.buscarPorTarefaCodRetorno", Mtxtb007TarefaMensagem.class)
        			.setParameter("nuTarefa",  versaoTarefa.getId().getNuTarefa002())
        			.setParameter("nuVersaoTarefa", versaoTarefa.getId().getNuVersaoTarefa())
        			.setParameter("coMensagem", codigoMensagem).getSingleResult();
		} 
	    catch (NoResultException e) {
			throw new SemResultadoException();
		}
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public List<Mtxtb007TarefaMensagem> buscarTarefaMensagensPorNumeroTarefa(long nuTarefa) {
		return this.entityManager.createNamedQuery("Mtxtb007TarefaMensagem.buscarPorNumeroTarefa", Mtxtb007TarefaMensagem.class)
				.setParameter("nuTarefa", nuTarefa)
				.getResultList();
	}
}
