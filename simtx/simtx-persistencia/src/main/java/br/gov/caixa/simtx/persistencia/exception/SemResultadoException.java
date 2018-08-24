/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.exception;

public class SemResultadoException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public SemResultadoException() {
		super();
	}

	public SemResultadoException(String message, Throwable cause) {
        super(message, cause);
    }

}
