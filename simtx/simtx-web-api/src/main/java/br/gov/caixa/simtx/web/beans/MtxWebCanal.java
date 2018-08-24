package br.gov.caixa.simtx.web.beans;

import java.math.BigDecimal;

public class MtxWebCanal {
	
	protected long nuCanal;
	protected String noCanal;
	protected BigDecimal icSituacaoCanal;
	protected BigDecimal icSituacaoCanalOrigem;
	protected int status;
	
	public long getNuCanal() {
		return nuCanal;
	}
	public void setNuCanal(long nuCanal) {
		this.nuCanal = nuCanal;
	}
	public String getNoCanal() {
		return noCanal;
	}
	public void setNoCanal(String noCanal) {
		this.noCanal = noCanal;
	}
	public BigDecimal getIcSituacaoCanal() {
		return icSituacaoCanal;
	}
	public void setIcSituacaoCanal(BigDecimal icSituacaoCanal) {
		this.icSituacaoCanal = icSituacaoCanal;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public BigDecimal getIcSituacaoCanalOrigem() {
		return icSituacaoCanalOrigem;
	}
	public void setIcSituacaoCanalOrigem(BigDecimal icSituacaoCanalOrigem) {
		this.icSituacaoCanalOrigem = icSituacaoCanalOrigem;
	}
}
