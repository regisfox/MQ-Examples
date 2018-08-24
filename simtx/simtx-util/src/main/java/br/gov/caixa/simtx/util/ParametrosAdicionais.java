package br.gov.caixa.simtx.util;

import java.io.Serializable;

import br.gov.caixa.simtx.util.xml.DadosBarramento;

/**
 * Classe responsavel por passagens de parametros essenciais para fila e mensagem.
 * @author rsfagundes
 *
 */
public class ParametrosAdicionais implements Serializable {

	private static final long serialVersionUID = 4329278503370454194L;

	private String idMessage;
	private String jndiQueueConnectionFactory;
	private String jndiResponseQueue;
	private boolean converterRespostaParaJson;
	private DadosBarramento dadosBarramento;
	private String xmlMensagem;
	
	public ParametrosAdicionais(String idMessage, String jndiQueueConnectionFactory, String jndiResponseQueue, boolean converterRespostaParaJson, DadosBarramento dadosBarramento, String xmlMensagem) {
		this.idMessage = idMessage;
		this.jndiQueueConnectionFactory = jndiQueueConnectionFactory;
		this.jndiResponseQueue = jndiResponseQueue;
		this.converterRespostaParaJson = converterRespostaParaJson;
		this.dadosBarramento = dadosBarramento;
		this.xmlMensagem = xmlMensagem;
	}
	
	public ParametrosAdicionais(String xmlMensagem, String idMessage, String jndiQueueConnectionFactory, String jndiResponseQueue, boolean converterRespostaParaJson) {
		this.xmlMensagem = xmlMensagem;
		this.idMessage = idMessage;
		this.jndiQueueConnectionFactory = jndiQueueConnectionFactory;
		this.jndiResponseQueue = jndiResponseQueue;
		this.converterRespostaParaJson = converterRespostaParaJson;
	}

	public ParametrosAdicionais(String idMessage, String jndiQueueConnectionFactory, String jndiResponseQueue, boolean converterRespostaParaJson) {
		this.idMessage = idMessage;
		this.jndiQueueConnectionFactory = jndiQueueConnectionFactory;
		this.jndiResponseQueue = jndiResponseQueue;
		this.converterRespostaParaJson = converterRespostaParaJson;
	}
	
	public ParametrosAdicionais(DadosBarramento dadosBarramento, String xmlMensagem) {
		this.dadosBarramento = dadosBarramento;
		this.xmlMensagem = xmlMensagem;
	}
	
	public ParametrosAdicionais() {
		super();
	}

	public String getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(String idMessage) {
		this.idMessage = idMessage;
	}

	public String getJndiQueueConnectionFactory() {
		return jndiQueueConnectionFactory;
	}

	public void setJndiQueueConnectionFactory(String jndiQueueConnectionFactory) {
		this.jndiQueueConnectionFactory = jndiQueueConnectionFactory;
	}

	public String getJndiResponseQueue() {
		return jndiResponseQueue;
	}

	public void setJndiResponseQueue(String jndiResponseQueue) {
		this.jndiResponseQueue = jndiResponseQueue;
	}

	public boolean isConverterRespostaParaJson() {
		return converterRespostaParaJson;
	}

	public void setConverterRespostaParaJson(boolean converterRespostaParaJson) {
		this.converterRespostaParaJson = converterRespostaParaJson;
	}


	public DadosBarramento getDadosBarramento() {
		return dadosBarramento;
	}


	public void setDadosBarramento(DadosBarramento dadosBarramento) {
		this.dadosBarramento = dadosBarramento;
	}


	public String getXmlMensagem() {
		return xmlMensagem;
	}


	public void setXmlMensagem(String xmlMensagem) {
		this.xmlMensagem = xmlMensagem;
	}

}
