package br.gov.caixa.simtx.util.xml;

public class Resposta {
	private String codigo;
	private String acao;
	private String origem;
	private int icTipoMensagem;

	private String mensagemNegocial;
	private String mensagemTecnica;
	private String mensagemRetorno;
	private String mensagemInstitucional;
	private String mensagemTela;
	private String mensagemInformativa;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
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

	public String getMensagemRetorno() {
		return mensagemRetorno;
	}

	public void setMensagemRetorno(String mensagemRetorno) {
		this.mensagemRetorno = mensagemRetorno;
	}

	public String getMensagemInstitucional() {
		return mensagemInstitucional;
	}

	public void setMensagemInstitucional(String mensagemInstitucional) {
		this.mensagemInstitucional = mensagemInstitucional;
	}

	public String getMensagemTela() {
		return mensagemTela;
	}

	public void setMensagemTela(String mensagemTela) {
		this.mensagemTela = mensagemTela;
	}

	public String getMensagemInformativa() {
		return mensagemInformativa;
	}

	public void setMensagemInformativa(String mensagemInformativa) {
		this.mensagemInformativa = mensagemInformativa;
	}

	public int getIcTipoMensagem() {
		return icTipoMensagem;
	}

	public void setIcTipoMensagem(int icTipoMensagem) {
		this.icTipoMensagem = icTipoMensagem;
	}
	

}
