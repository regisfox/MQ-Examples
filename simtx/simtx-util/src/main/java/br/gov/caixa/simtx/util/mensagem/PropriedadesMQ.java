package br.gov.caixa.simtx.util.mensagem;

/**
 * Define propriedades de fila para Servico, Tarefa e Canais.
 * 
 * @author rctoscano
 *
 */
public class PropriedadesMQ {
	
	private Integer timeout;
	
	private String connectionFactory;
	
	private String filaRequisicao;
	
	private String filaResposta;
	
	
	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(String connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public String getFilaRequisicao() {
		return filaRequisicao;
	}

	public void setFilaRequisicao(String filaRequisicao) {
		this.filaRequisicao = filaRequisicao;
	}

	public String getFilaResposta() {
		return filaResposta;
	}

	public void setFilaResposta(String filaResposta) {
		this.filaResposta = filaResposta;
	}

}
