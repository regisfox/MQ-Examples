package br.gov.caixa.simtx.web.beans;

public class MtxVersaoServico {
	
	protected Integer icSituacaoServico;
	//protected int status;
	protected Integer icSituacaoServicoOrigem;
	protected int icVersaoServico;
	
	protected long nuServico;

	protected String noServico;
	
	protected String selecionado;
	
	protected String status;

	public Integer getIcSituacaoServico() {
		return icSituacaoServico;
	}
	public void setIcSituacaoServico(Integer icSituacaoServico) {
		this.icSituacaoServico = icSituacaoServico;
	}
	public Integer getIcSituacaoServicoOrigem() {
		return icSituacaoServicoOrigem;
	}
	public void setIcSituacaoServicoOrigem(Integer icSituacaoServicoOrigem) {
		this.icSituacaoServicoOrigem = icSituacaoServicoOrigem;
	}
	public int getIcVersaoServico() {
		return icVersaoServico;
	}
	public void setIcVersaoServico(int icVersaoServico) {
		this.icVersaoServico = icVersaoServico;
	}
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
	public String getSelecionado() {
		return selecionado;
	}
	public void setSelecionado(String selecionado) {
		this.selecionado = selecionado;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
