package br.gov.caixa.simtx.agendamento.entidade;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CancelamentoAgendamentoWebSaida {

	private Long nsuAgendamento;
	private Boolean situacao;
	
	public CancelamentoAgendamentoWebSaida() {
	}

	public CancelamentoAgendamentoWebSaida(Long nsuAgendamento, Boolean situacao) {
		super();
		this.nsuAgendamento = nsuAgendamento;
		this.situacao = situacao;
	}

	public Long getNsuAgendamento() {
		return nsuAgendamento;
	}
	
	@XmlElement
	public void setNsuAgendamento(Long nsuAgendamento) {
		this.nsuAgendamento = nsuAgendamento;
	}

	public Boolean getSituacao() {
		return situacao;
	}
	
	@XmlElement
	public void setSituacao(Boolean situacao) {
		this.situacao = situacao;
	}

}
