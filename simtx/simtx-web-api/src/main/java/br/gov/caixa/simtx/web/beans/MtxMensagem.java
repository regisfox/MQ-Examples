package br.gov.caixa.simtx.web.beans;

public class MtxMensagem {

	private String codigoMensagem;
	private String mensagemNegocial;
	private String mensagemTecnica;

	public String getCodigoMensagem() {
		return codigoMensagem;
	}

	public void setCodigoMensagem(String codigoMensagem) {
		this.codigoMensagem = codigoMensagem;
	}

	public String getMensagemNegocial() {
		return mensagemNegocial;
	}

	public void setMensagemNegocial(String mensagemNegocial) {
		this.mensagemNegocial = mensagemNegocial;
	}

	public String getMensagemTecnica() {
		return mensagemTecnica;
	}

	public void setMensagemTecnica(String mensagemTecnica) {
		this.mensagemTecnica = mensagemTecnica;
	}

}
