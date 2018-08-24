package br.gov.caixa.simtx.web.beans.agendamentos.naoefetivados;

import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtx.web.beans.MtxCancelamentoAgendamentoSaida;
import br.gov.caixa.simtx.web.beans.MtxMensagem;

@XmlRootElement
public class MtxAgendamentoNaoEfetivadoSaida extends MtxCancelamentoAgendamentoSaida {
	
	private MtxMensagem mensagem;

	public MtxMensagem getMensagem() {
		return mensagem;
	}

	public void setMensagem(MtxMensagem mensagem) {
		this.mensagem = mensagem;
	}

}
