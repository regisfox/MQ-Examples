package br.gov.caixa.simtx.dominio;


public class Operacao {

	private String COD_CANAL;
	private String COD_SERVICO;
	private String COD_TRANSACAO;
	private String VERSAO;
	
	public Operacao(String cOD_CANAL, String cOD_SERVICO, String cOD_TRANSACAO,
			String vERSAO) {
		super();
		COD_CANAL = cOD_CANAL;
		COD_SERVICO = cOD_SERVICO;
		COD_TRANSACAO = cOD_TRANSACAO;
		VERSAO = vERSAO;
	}
	public String getCOD_CANAL() {
		return COD_CANAL;
	}
	public void setCOD_CANAL(String cOD_CANAL) {
		COD_CANAL = cOD_CANAL;
	}
	public String getCOD_SERVICO() {
		return COD_SERVICO;
	}
	public void setCOD_SERVICO(String cOD_SERVICO) {
		COD_SERVICO = cOD_SERVICO;
	}
	public String getCOD_TRANSACAO() {
		return COD_TRANSACAO;
	}
	public void setCOD_TRANSACAO(String cOD_TRANSACAO) {
		COD_TRANSACAO = cOD_TRANSACAO;
	}
	public String getVERSAO() {
		return VERSAO;
	}
	public void setVERSAO(String vERSAO) {
		VERSAO = vERSAO;
	}
		
	
}
