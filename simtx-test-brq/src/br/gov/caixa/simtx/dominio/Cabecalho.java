package br.gov.caixa.simtx.dominio;


public class Cabecalho {
	public String canal;
	public String servico;
	public String versao;
	
	public String getCanal() {
		return canal;
	}
	public Cabecalho setCanal(String canal) {
		this.canal = canal;
		return this;
	}
	public String getServico() {
		return servico;
	}
	public Cabecalho setServico(String servico) {
		this.servico = servico;
		return this;
	}
	public String getVersao() {
		return versao;
	}
	public Cabecalho setVersao(String versao) {
		this.versao = versao;
		return this;
	}

	
}
