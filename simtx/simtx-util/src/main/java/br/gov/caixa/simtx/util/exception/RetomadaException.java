/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.exception;

/**
 * The Class RetomadaException.
 */
public class RetomadaException extends Error {
	
	private static final long serialVersionUID = 6215361465016735503L;

	private final String mensagem;

	public RetomadaException(String mensagem) {
		super();
		this.mensagem = mensagem;
	}

	@Override
	public String getMessage() {
		return this.mensagem;
	}
}
