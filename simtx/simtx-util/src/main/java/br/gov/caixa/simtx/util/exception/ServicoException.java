/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.exception;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;

public class ServicoException extends Exception {
	private static final long serialVersionUID = 6215361465016735503L;

	private final String sistemaOrigem;
	private final Mtxtb006Mensagem mensagem;

	public ServicoException(Mtxtb006Mensagem mensagem, String sistemaOrigem) {
		super();
		this.sistemaOrigem = sistemaOrigem;
		this.mensagem = mensagem;
	}

	public String getSistemaOrigem() {
		return sistemaOrigem;
	}

	public Mtxtb006Mensagem getMensagem() {
		return mensagem;
	}
	
	@Override
	public String getMessage() {
		return this.mensagem.getDeMensagemTecnica();
	}
}
