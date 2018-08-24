package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the MTXTB032_TRANSACAO_CONTA database table.
 * 
 */
@Entity
@Table(name = "MTXTB035_TRANSACAO_CONTA")
public class Mtxtb035TransacaoConta implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name = "NU_NSU_TRANSACAO_016")
    private long nuNsuTransacao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_REFERENCIA")
	private Date dataReferencia;
	
	@Column(name = "IC_CONTA_SOLUCAO")
	private int indicadorConta;
	
	@Column(name = "NU_UNIDADE")
	private long numeroUnidade;

	@Column(name = "NU_CONTA")
	private long numeroConta;
	
	@Column(name = "NU_PRODUTO")
	private int opProduto;
	
	@Column(name = "NU_DV_CONTA")
	private int nuDvConta;
	
	@Column(name = "NU_CANAL_016")
	private long nuCanal;
	
	@Column(name = "NU_SERVICO_017")
	private long numeroServico;
	
	@Column(name = "NU_VERSAO_SERVICO_017")
	private int numeroVersaoServico;

	@Column(name = "IC_SITUACAO_TRNSO_CONTA")
	private long icSituacao;

	@OneToOne(mappedBy = "mtxtb035TransacaoConta")
	private Mtxtb016IteracaoCanal mtxtb016IteracaoCanal;

	@ManyToOne
	@JoinColumn(name = "NU_CANAL_016", referencedColumnName = "NU_CANAL", nullable = false, insertable = false, updatable = false)
	private Mtxtb004Canal mtxtb004Canal;

	@ManyToOne
	@JoinColumn(name = "NU_SERVICO_017", referencedColumnName = "NU_SERVICO", nullable = false, insertable = false, updatable = false)
	private Mtxtb001Servico mtxtb001Servico;

	public long getNuNsuTransacao() {
		return nuNsuTransacao;
	}

	public void setNuNsuTransacao(long nuNsuTransacao) {
		this.nuNsuTransacao = nuNsuTransacao;
	}

	public Date getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public int getIndicadorConta() {
		return indicadorConta;
	}

	public void setIndicadorConta(int indicadorConta) {
		this.indicadorConta = indicadorConta;
	}

	public long getNumeroUnidade() {
		return numeroUnidade;
	}

	public void setNumeroUnidade(long numeroUnidade) {
		this.numeroUnidade = numeroUnidade;
	}

	public long getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(long numeroConta) {
		this.numeroConta = numeroConta;
	}

	public int getOpProduto() {
		return opProduto;
	}

	public void setOpProduto(int opProduto) {
		this.opProduto = opProduto;
	}

	public int getNuDvConta() {
		return nuDvConta;
	}

	public void setNuDvConta(int nuDvConta) {
		this.nuDvConta = nuDvConta;
	}

	public long getNumeroCanal() {
		return nuCanal;
	}

	public void setNumeroCanal(long numeroCanal) {
		this.nuCanal = numeroCanal;
	}

	public long getNumeroServico() {
		return numeroServico;
	}

	public void setNumeroServico(long numeroServico) {
		this.numeroServico = numeroServico;
	}

	public int getNumeroVersaoServico() {
		return numeroVersaoServico;
	}

	public void setNumeroVersaoServico(int numeroVersaoServico) {
		this.numeroVersaoServico = numeroVersaoServico;
	}

	public long getIcSituacao() {
		return icSituacao;
	}

	public void setIcSituacao(long icSituacao) {
		this.icSituacao = icSituacao;
	}

	public Mtxtb016IteracaoCanal getMtxtb016IteracaoCanal() {
		return mtxtb016IteracaoCanal;
	}

	public void setMtxtb016IteracaoCanal(Mtxtb016IteracaoCanal mtxtb016IteracaoCanal) {
		this.mtxtb016IteracaoCanal = mtxtb016IteracaoCanal;
	}

	public Mtxtb004Canal getMtxtb004Canal() {
		return mtxtb004Canal;
	}

	public void setMtxtb004Canal(Mtxtb004Canal mtxtb004Canal) {
		this.mtxtb004Canal = mtxtb004Canal;
	}

	public Mtxtb001Servico getMtxtb001Servico() {
		return mtxtb001Servico;
	}

	public void setMtxtb001Servico(Mtxtb001Servico mtxtb001Servico) {
		this.mtxtb001Servico = mtxtb001Servico;
	}
	
}
