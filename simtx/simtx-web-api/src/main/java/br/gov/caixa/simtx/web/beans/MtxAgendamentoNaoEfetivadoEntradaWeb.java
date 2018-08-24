package br.gov.caixa.simtx.web.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MtxAgendamentoNaoEfetivadoEntradaWeb {

	private int agencia;

	private int tipoConta;

	private int operacao;

	private long conta;

	private int dv;

	private MtxCanal canal;

	private MtxServico servico;

	public int getAgencia() {
		return agencia;
	}

	@XmlElement
	public void setAgencia(int agencia) {
		this.agencia = agencia;
	}

	public int getTipoConta() {
		return tipoConta;
	}

	@XmlElement
	public void setTipoConta(int tipoConta) {
		this.tipoConta = tipoConta;
	}

	public int getOperacao() {
		return operacao;
	}

	@XmlElement
	public void setOperacao(int operacao) {
		this.operacao = operacao;
	}

	public long getConta() {
		return conta;
	}

	@XmlElement
	public void setConta(int conta) {
		this.conta = conta;
	}

	public int getDv() {
		return dv;
	}

	@XmlElement
	public void setDv(int dv) {
		this.dv = dv;
	}

	public MtxCanal getCanal() {
		return canal;
	}

	@XmlElement
	public void setCanal(MtxCanal canal) {
		this.canal = canal;
	}

	public MtxServico getServico() {
		return servico;
	}

	@XmlElement
	public void setServico(MtxServico servico) {
		this.servico = servico;
	}
}
