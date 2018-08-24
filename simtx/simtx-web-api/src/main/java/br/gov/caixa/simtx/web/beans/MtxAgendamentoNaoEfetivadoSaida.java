package br.gov.caixa.simtx.web.beans;

import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class MtxAgendamentoNaoEfetivadoSaida extends MtxCancelamentoAgendamentoWeb {
	
	private MtxMensagem mensagem;

	public MtxMensagem getMensagem() {
		return mensagem;
	}

	public void setMensagem(MtxMensagem mensagem) {
		this.mensagem = mensagem;
	}

}
