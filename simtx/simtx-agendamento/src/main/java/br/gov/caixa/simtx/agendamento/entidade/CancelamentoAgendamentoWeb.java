package br.gov.caixa.simtx.agendamento.entidade;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CancelamentoAgendamentoWeb {

	private Integer agencia;
	private Integer operacao;
	private Long conta;
	private int dv;
	private String tipoConta;
	private String codigoUsuario;
	protected String noCanal;
	private Long codigoCanal;
	private String codigoMaquina;
	private Long codigoServico;
	private Integer versaoServico;
	protected String noServico;
	private String codigoBarras;
	private String dataAgendamento;
	private String dataEfetivacao;
	private String valorTransacao;
	private Long nuNsuTransacaoAgendamento;
	
	public Integer getAgencia() {
		return agencia;
	}
	
	@XmlElement
	public void setAgencia(Integer agencia) {
		this.agencia = agencia;
	}
	
	public Integer getOperacao() {
		return operacao;
	}
	
	@XmlElement
	public void setOperacao(Integer operacao) {
		this.operacao = operacao;
	}
	
	public Long getConta() {
		return conta;
	}
	
	@XmlElement
	public void setConta(Long conta) {
		this.conta = conta;
	}
	
	public int getDv() {
		return dv;
	}
	
	@XmlElement
	public void setDv(int dv) {
		this.dv = dv;
	}
	
	public String getTipoConta() {
		return tipoConta;
	}
	
	@XmlElement
	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}
	
	public String getCodigoUsuario() {
		return codigoUsuario;
	}
	
	@XmlElement
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	
	public String getNoCanal() {
		return noCanal;
	}
	
	@XmlElement
	public void setNoCanal(String noCanal) {
		this.noCanal = noCanal;
	}
	
	public Long getCodigoCanal() {
		return codigoCanal;
	}
	
	@XmlElement
	public void setCodigoCanal(Long codigoCanal) {
		this.codigoCanal = codigoCanal;
	}
	
	public String getCodigoMaquina() {
		return codigoMaquina;
	}
	
	@XmlElement
	public void setCodigoMaquina(String codigoMaquina) {
		this.codigoMaquina = codigoMaquina;
	}
	
	public Long getCodigoServico() {
		return codigoServico;
	}
	
	@XmlElement
	public void setCodigoServico(Long codigoServico) {
		this.codigoServico = codigoServico;
	}
	
	public Integer getVersaoServico() {
		return versaoServico;
	}
	
	@XmlElement
	public void setVersaoServico(Integer versaoServico) {
		this.versaoServico = versaoServico;
	}
	
	public String getNoServico() {
		return noServico;
	}
	
	@XmlElement
	public void setNoServico(String noServico) {
		this.noServico = noServico;
	}
	
	public String getCodigoBarras() {
		return codigoBarras;
	}
	
	@XmlElement
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
	public String getDataAgendamento() {
		return dataAgendamento;
	}
	
	@XmlElement
	public void setDataAgendamento(String dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}
	
	public String getDataEfetivacao() {
		return dataEfetivacao;
	}
	
	@XmlElement
	public void setDataEfetivacao(String dataEfetivacao) {
		this.dataEfetivacao = dataEfetivacao;
	}
	
	public String getValorTransacao() {
		return valorTransacao;
	}
	
	@XmlElement
	public void setValorTransacao(String valorTransacao) {
		this.valorTransacao = valorTransacao;
	}
	
	public Long getNuNsuTransacaoAgendamento() {
		return nuNsuTransacaoAgendamento;
	}
	
	@XmlElement
	public void setNuNsuTransacaoAgendamento(Long nuNsuTransacaoAgendamento) {
		this.nuNsuTransacaoAgendamento = nuNsuTransacaoAgendamento;
	}
}
