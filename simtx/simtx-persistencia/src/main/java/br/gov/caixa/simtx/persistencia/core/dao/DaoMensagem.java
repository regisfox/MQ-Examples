/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;


import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;

public interface DaoMensagem {
	
	/**
	 * Busca a mensagem pelo codigo.
	 * 
	 * @param codigoMensagem
	 * @param tarefa 
	 * @return
	 * @throws Exception
	 */
	public Mtxtb006Mensagem findbyNsu(Long nsu) throws SemResultadoException;
	
	/**
	 * Busca mensagem por codigo.
	 * 
	 *  @param codigoMensagem Codigo da mensagem.
	 *  
	 *  @return Mensagem.
	 */
	public Mtxtb006Mensagem findbyCodigoMensagem(String codigoMensagem);
	
	/**
	 * Salva mensagem.
	 * 
	 *  @param mensagem. A mensagem para ser salva.
	 *  
	 *  
	 */
	public void salvaMensagem(Mtxtb006Mensagem mensagem);
	
}

