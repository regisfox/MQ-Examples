package br.gov.caixa.simtx.dominio.adesao;

public class Telefone {
	private String ddd;
	private String numero;
	
	public Telefone(String ddd, String numero) {
		super();
		this.ddd = ddd;
		this.numero = numero;
	}

	public String getDdd() {
		return ddd;
	}

	public Telefone setDdd(String ddd) {
		this.ddd = ddd;
		return this;
	}

	public String getNumero() {
		return numero;
	}

	public Telefone setNumero(String numero) {
		this.numero = numero;
		return this;
	}
}