package br.gov.caixa.simtx.web.beans;

import java.util.Date;

public class MtxTransacaoConta {

	private long nuNsuTransacao;

	private Date dataReferencia;

	private long numeroUnidade;

	private long numeroConta;

	private int opProduto;

	private int nuDvConta;

	private long numeroServico;

	private int numeroVersaoServico;

	private MtxCanal canal;

	private MtxServico servico;

	public long getNuNsuTransacao() {
		return nuNsuTransacao;
	}

	public void setNuNsuTransacao(long nuNsuTransacao) {
		this.nuNsuTransacao = nuNsuTransacao;
	}

	public Date getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public long getNumeroUnidade() {
		return numeroUnidade;
	}

	public void setNumeroUnidade(long numeroUnidade) {
		this.numeroUnidade = numeroUnidade;
	}

	public long getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(long numeroConta) {
		this.numeroConta = numeroConta;
	}

	public int getOpProduto() {
		return opProduto;
	}

	public void setOpProduto(int opProduto) {
		this.opProduto = opProduto;
	}

	public int getNuDvConta() {
		return nuDvConta;
	}

	public void setNuDvConta(int nuDvConta) {
		this.nuDvConta = nuDvConta;
	}

	public long getNumeroServico() {
		return numeroServico;
	}

	public void setNumeroServico(long numeroServico) {
		this.numeroServico = numeroServico;
	}

	public int getNumeroVersaoServico() {
		return numeroVersaoServico;
	}

	public void setNumeroVersaoServico(int numeroVersaoServico) {
		this.numeroVersaoServico = numeroVersaoServico;
	}

	public MtxCanal getCanal() {
		return canal;
	}

	public void setCanal(MtxCanal canal) {
		this.canal = canal;
	}

	public MtxServico getServico() {
		return servico;
	}

	public void setServico(MtxServico servico) {
		this.servico = servico;
	}

}
