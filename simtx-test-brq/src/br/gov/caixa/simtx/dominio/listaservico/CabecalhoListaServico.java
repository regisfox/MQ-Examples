package br.gov.caixa.simtx.dominio.listaservico;

public class CabecalhoListaServico {
	private String nsu;
	private String versao;
	private String canal;

	public String getNsu() {
		return nsu;
	}

	public CabecalhoListaServico setNsu(String nsu) {
		this.nsu = nsu;
		return this;
	}

	public String getVersao() {
		return versao;
	}

	public CabecalhoListaServico setVersao(String versao) {
		this.versao = versao;
		return this;
	}

	public String getCanal() {
		return canal;
	}

	public CabecalhoListaServico setCanal(String canal) {
		this.canal = canal;
		return this;
	}
}
