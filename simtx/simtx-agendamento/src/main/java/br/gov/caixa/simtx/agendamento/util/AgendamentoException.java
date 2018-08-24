package br.gov.caixa.simtx.agendamento.util;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;

public class AgendamentoException extends Exception {
	
	private static final long serialVersionUID = 6215361465016735503L;

	private final String mensagem;
	
	private final Mtxtb006Mensagem mtxtb006Mensagem;
	

	public AgendamentoException(Mtxtb006Mensagem mtxtb006Mensagem, String mensagem) {
		super();
		this.mtxtb006Mensagem = mtxtb006Mensagem;
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public Mtxtb006Mensagem getMtxtb006Mensagem() {
		return mtxtb006Mensagem;
	}
	
}
