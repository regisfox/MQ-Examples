package br.gov.caixa.simtx.util;

public class DadosAuditoriaUsuario {

	private String idUsuario;
	private String ipUsuarioRequisicao;

	public DadosAuditoriaUsuario(String idUsuario, String ipUsuarioRequisicao) {
		super();
		this.idUsuario = idUsuario;
		this.ipUsuarioRequisicao = ipUsuarioRequisicao;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getIpUsuarioRequisicao() {
		return ipUsuarioRequisicao;
	}

	public void setIpUsuarioRequisicao(String ipUsuarioRequisicao) {
		this.ipUsuarioRequisicao = ipUsuarioRequisicao;
	}

}
