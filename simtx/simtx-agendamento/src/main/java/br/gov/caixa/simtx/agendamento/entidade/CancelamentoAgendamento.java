package br.gov.caixa.simtx.agendamento.entidade;

import java.util.List;

public class CancelamentoAgendamento {

	private List<CancelamentoAgendamentoWeb> agendamentosWeb;

	public List<CancelamentoAgendamentoWeb> getAgendamentosWeb() {
		return agendamentosWeb;
	}

	public void setAgendamentosWeb(List<CancelamentoAgendamentoWeb> agendamentosWeb) {
		this.agendamentosWeb = agendamentosWeb;
	}
}
