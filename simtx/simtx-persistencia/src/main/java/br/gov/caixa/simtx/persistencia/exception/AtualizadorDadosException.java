/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.exception;

public class AtualizadorDadosException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public AtualizadorDadosException() {
		super();
	}

	public AtualizadorDadosException(String message, Throwable cause) {
        super(message, cause);
    }

}
