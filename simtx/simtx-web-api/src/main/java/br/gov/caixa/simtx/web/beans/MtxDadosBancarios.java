package br.gov.caixa.simtx.web.beans;

public class MtxDadosBancarios {

	private int nuUnidade;

	private int nuProduto;

	private long nuConta;

	private int dvConta;

	private int tipoConta;

	public int getNuUnidade() {
		return nuUnidade;
	}

	public void setNuUnidade(int nuUnidade) {
		this.nuUnidade = nuUnidade;
	}

	public int getNuProduto() {
		return nuProduto;
	}

	public void setNuProduto(int nuProduto) {
		this.nuProduto = nuProduto;
	}

	public long getNuConta() {
		return nuConta;
	}

	public void setNuConta(long nuConta) {
		this.nuConta = nuConta;
	}

	public int getDvConta() {
		return dvConta;
	}

	public void setDvConta(int dvConta) {
		this.dvConta = dvConta;
	}

	public int getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(int tipoConta) {
		this.tipoConta = tipoConta;
	}

}
