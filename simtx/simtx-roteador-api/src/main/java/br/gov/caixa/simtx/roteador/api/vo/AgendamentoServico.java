package br.gov.caixa.simtx.roteador.api.vo;

import java.io.Serializable;

public class AgendamentoServico implements Serializable {

	private static final long serialVersionUID = 1330649339043443477L;
	private Long nsuTransacaoOrigem;
	private Long numServicoFinal;
	private Integer numServicoVersaoFinal;
	private boolean ultimaExecucao;

	public AgendamentoServico() {
		super();
	}

	public Long getNsuTransacaoOrigem() {
		return nsuTransacaoOrigem;
	}

	public void setNsuTransacaoOrigem(Long nsuTransacaoOrigem) {
		this.nsuTransacaoOrigem = nsuTransacaoOrigem;
	}

	public Long getNumServicoFinal() {
		return numServicoFinal;
	}

	public void setNumServicoFinal(Long numServicoFinal) {
		this.numServicoFinal = numServicoFinal;
	}

	public Integer getNumServicoVersaoFinal() {
		return numServicoVersaoFinal;
	}

	public void setNumServicoVersaoFinal(Integer numServicoVersaoFinal) {
		this.numServicoVersaoFinal = numServicoVersaoFinal;
	}

	public boolean isUltimaExecucao() {
		return ultimaExecucao;
	}

	public void setUltimaExecucao(boolean ultimaExecucao) {
		this.ultimaExecucao = ultimaExecucao;
	}
}
