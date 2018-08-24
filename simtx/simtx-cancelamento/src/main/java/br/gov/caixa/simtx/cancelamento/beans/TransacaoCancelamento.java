package br.gov.caixa.simtx.cancelamento.beans;

import java.util.Date;

/**
 * @author rsfagundes
 *
 */
public class TransacaoCancelamento {

	private Date dataReferencia;
	private Date dataHoraCanal;
	private Long nsuOrigem;
	private String siglaCanal;
	private String terminal;
	private String codBarras;
	private Long codCanal;
	private Long codServico;
	private String valor;
	private Long agencia;
	private int opProduto;
	private Long nuConta;
	private int nuDV;
	private Long meioEntrada;
	private String ipMaquina;
	private String usuario;

	public Date getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public Date getDataHoraCanal() {
		return dataHoraCanal;
	}

	public void setDataHoraCanal(Date dataHoraCanal) {
		this.dataHoraCanal = dataHoraCanal;
	}

	public Long getNsuOrigem() {
		return nsuOrigem;
	}

	public void setNsuOrigem(Long nsuOrigem) {
		this.nsuOrigem = nsuOrigem;
	}

	public String getSiglaCanal() {
		return siglaCanal;
	}

	public void setSiglaCanal(String siglaCanal) {
		this.siglaCanal = siglaCanal;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getCodBarras() {
		return codBarras;
	}

	public void setCodBarras(String codBarras) {
		this.codBarras = codBarras;
	}

	public Long getCodCanal() {
		return codCanal;
	}

	public void setCodCanal(Long codCanal) {
		this.codCanal = codCanal;
	}

	public Long getCodServico() {
		return codServico;
	}

	public void setCodServico(Long codServico) {
		this.codServico = codServico;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Long getAgencia() {
		return agencia;
	}

	public void setAgencia(Long agencia) {
		this.agencia = agencia;
	}

	public int getOpProduto() {
		return opProduto;
	}

	public void setOpProduto(int opProduto) {
		this.opProduto = opProduto;
	}

	public Long getNuConta() {
		return nuConta;
	}

	public void setNuConta(Long conta) {
		this.nuConta = conta;
	}

	public Long getMeioEntrada() {
		return meioEntrada;
	}

	public void setMeioEntrada(Long meioEntrada) {
		this.meioEntrada = meioEntrada;
	}

	public String getIpMaquina() {
		return ipMaquina;
	}

	public void setIpMaquina(String ipMaquina) {
		this.ipMaquina = ipMaquina;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getNuDV() {
		return nuDV;
	}

	public void setNuDV(int nuDV) {
		this.nuDV = nuDV;
	}

}
