package br.gov.caixa.simtx.cancelamento.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MtxTransacaoContaEntrada {

	private Long nuNsuTransacaoRefMtx016;
	private Long nsuCanal;
	private String linhaDigitavel;
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
	private int indicadorConta;
	private String codigoUsuario;
	private String codigoMaquina;
	
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
	
	public int getIndicadorConta() {
		return indicadorConta;
	}

	@XmlElement
	public void setIndicadorConta(int siglaCanal) {
		this.indicadorConta = siglaCanal;
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

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	@XmlElement
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getCodigoMaquina() {
		return codigoMaquina;
	}

	@XmlElement
	public void setCodigoMaquina(String codigoMaquina) {
		this.codigoMaquina = codigoMaquina;
	}

	public Long getNsuCanal() {
		return nsuCanal;
	}

	public void setNsuCanal(Long nsuCanal) {
		this.nsuCanal = nsuCanal;
	}

	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}

	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}
}
