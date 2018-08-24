package br.gov.caixa.simtx.util.to;

import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.util.xml.Resposta;

public class TarefasServicoResposta {
	
	private boolean possuiTarefaImpeditiva;

	private Resposta resposta;

	private List<Mtxtb015SrvcoTrnsoTrfa> listaTransacaoTarefas;
	
	private String xmlSaidaSibar;

	public Resposta getResposta() {
		return resposta;
	}

	public void setResposta(Resposta resposta) {
		this.resposta = resposta;
	}

	public List<Mtxtb015SrvcoTrnsoTrfa> getListaTransacaoTarefas() {
		return listaTransacaoTarefas;
	}

	public void setListaTransacaoTarefas(List<Mtxtb015SrvcoTrnsoTrfa> listaTransacaoTarefas) {
		this.listaTransacaoTarefas = listaTransacaoTarefas;
	}

	public boolean isPossuiTarefaImpeditiva() {
		return possuiTarefaImpeditiva;
	}

	public void setPossuiTarefaImpeditiva(boolean possuiTarefaImpeditiva) {
		this.possuiTarefaImpeditiva = possuiTarefaImpeditiva;
	}

	public String getXmlSaidaSibar() {
		return xmlSaidaSibar;
	}

	public void setXmlSaidaSibar(String xmlSaidaSibar) {
		this.xmlSaidaSibar = xmlSaidaSibar;
	}

}
