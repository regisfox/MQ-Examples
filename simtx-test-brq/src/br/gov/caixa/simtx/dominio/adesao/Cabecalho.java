package br.gov.caixa.simtx.dominio.adesao;

public class Cabecalho {
	private long nsu;
	private String data_hora;
	private String canal;
	private String versao;
	private String cpf;
	private String cnpj;

	public long getNsu() {
		return nsu;
	}

	public Cabecalho setNsu(long nsu) {
		this.nsu = nsu;
		return this;
	}

	public String getData_hora() {
		return data_hora;
	}

	public Cabecalho setData_hora(String data_hora) {
		this.data_hora = data_hora;
		return this;
	}

	public String getCanal() {
		return canal;
	}

	public Cabecalho setCanal(String canal) {
		this.canal = canal;
		return this;
	}

	public String getVersao() {
		return versao;
	}

	public Cabecalho setVersao(String versao) {
		this.versao = versao;
		return this;
	}

	public String getCpf() {
		return cpf;
	}

	public Cabecalho setCpf(String cpf) {
		this.cpf = cpf;
		return this;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	
}
