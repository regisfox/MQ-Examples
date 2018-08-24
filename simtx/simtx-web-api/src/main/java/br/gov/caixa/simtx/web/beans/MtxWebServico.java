package br.gov.caixa.simtx.web.beans;

import java.math.BigDecimal;

public class MtxWebServico {

	protected long nuServico;
	protected String noServico;
	protected int versao;
	protected BigDecimal icSituacaoServico;
	protected int status;
	protected BigDecimal icSituacaoServicoOrigem;
	protected int icVersaoServico;
	
	public long getNuServico() {
		return nuServico;
	}
	public void setNuServico(long nuServico) {
		this.nuServico = nuServico;
	}
	public String getNoServico() {
		return noServico;
	}
	public void setNoServico(String noServico) {
		this.noServico = noServico;
	}
	public int getVersao() {
		return versao;
	}
	public void setVersao(int versao) {
		this.versao = versao;
	}
	public BigDecimal getIcSituacaoServico() {
		return icSituacaoServico;
	}
	public void setIcSituacaoServico(BigDecimal icSituacaoServico) {
		this.icSituacaoServico = icSituacaoServico;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public BigDecimal getIcSituacaoServicoOrigem() {
		return icSituacaoServicoOrigem;
	}
	public void setIcSituacaoServicoOrigem(BigDecimal icSituacaoServicoOrigem) {
		this.icSituacaoServicoOrigem = icSituacaoServicoOrigem;
	}
	public int getIcVersaoServico() {
		return icVersaoServico;
	}
	public void setIcVersaoServico(int icVersaoServico) {
		this.icVersaoServico = icVersaoServico;
	}
}
