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

import br.gov.caixa.simtx.persistencia.core.dao.DaoMensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;

@Stateless
public class DaoMensagemImpl implements DaoMensagem {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    
    @Override
	public Mtxtb006Mensagem findbyCodigoMensagem(String codigoMensagem) {
		return this.em.createNamedQuery("Mtxtb006Mensagem.buscarPorTarefaCodMensagem", Mtxtb006Mensagem.class)
				.setParameter("coMensagem", codigoMensagem).getSingleResult();
	}
    
    @Override
	public Mtxtb006Mensagem findbyNsu(Long nsu) throws SemResultadoException {
		try {
			return em.find(Mtxtb006Mensagem.class, nsu);
		}catch (NoResultException e) {
			throw new SemResultadoException();
		}
	}
	
	@Override
	public void salvaMensagem(Mtxtb006Mensagem mensagem) {
		this.em.merge(mensagem);
	}
}
