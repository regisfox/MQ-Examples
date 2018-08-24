package br.gov.caixa.simtx.cancelamento.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MtxTransacaoContaSaida {
	private Long nuNsuTransacaoRefMtx016;
	private String codBarras;
	private String dataReferencia;
	private String valor;
	private Long nuCanal;
	private String noCanal;
	private String siglaCanal;
	private Long nuServico;
	private String noServico;
	private String descricaoServico;
	private Long agencia;
	private int opProduto;
	private Long conta;
	private int dv;
	
	private boolean statusCancelamento;


	public Long getNuNsuTransacaoRefMtx016() {
		return nuNsuTransacaoRefMtx016;
	}
	
	@XmlElement
	public void setNuNsuTransacaoRefMtx016(Long nuNsuTransacaoRefMtx016) {
		this.nuNsuTransacaoRefMtx016 = nuNsuTransacaoRefMtx016;
	}
	public String getCodBarras() {
		return codBarras;
	}
	
	@XmlElement
	public void setCodBarras(String codBarras) {
		this.codBarras = codBarras;
	}
	public String getDataReferencia() {
		return dataReferencia;
	}
	
	@XmlElement
	public void setDataReferencia(String dataReferencia) {
		this.dataReferencia = dataReferencia;
	}
	public String getValor() {
		return valor;
	}
	
	@XmlElement
	public void setValor(String valor) {
		this.valor = valor;
	}
	public Long getNuCanal() {
		return nuCanal;
	}
	
	@XmlElement
	public void setNuCanal(Long nuCanal) {
		this.nuCanal = nuCanal;
	}
	public String getNoCanal() {
		return noCanal;
	}
	
	@XmlElement
	public void setNoCanal(String noCanal) {
		this.noCanal = noCanal;
	}
	public String getSiglaCanal() {
		return siglaCanal;
	}
	
	@XmlElement
	public void setSiglaCanal(String siglaCanal) {
		this.siglaCanal = siglaCanal;
	}
	public Long getNuServico() {
		return nuServico;
	}
	
	@XmlElement
	public void setNuServico(Long nuServico) {
		this.nuServico = nuServico;
	}
	public String getNoServico() {
		return noServico;
	}
	
	@XmlElement
	public void setNoServico(String noServico) {
		this.noServico = noServico;
	}
	public String getDescricaoServico() {
		return descricaoServico;
	}
	
	@XmlElement
	public void setDescricaoServico(String descricaoServico) {
		this.descricaoServico = descricaoServico;
	}
	public Long getAgencia() {
		return agencia;
	}
	
	@XmlElement
	public void setAgencia(Long agencia) {
		this.agencia = agencia;
	}
	public int getOpProduto() {
		return opProduto;
	}
	
	@XmlElement
	public void setOpProduto(int opProduto) {
		this.opProduto = opProduto;
	}
	public Long getConta() {
		return conta;
	}
	
	@XmlElement
	public void setConta(Long conta) {
		this.conta = conta;
	}
	public int getDv() {
		return dv;
	}
	
	@XmlElement
	public void setDv(int dv) {
		this.dv = dv;
	}

	public boolean isStatusCancelamento() {
		return statusCancelamento;
	}

	@XmlElement
	public void setStatusCancelamento(boolean statusCancelamento) {
		this.statusCancelamento = statusCancelamento;
	}
}
