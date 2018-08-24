/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.agendamento.entidade;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;

@Entity
@Table(name = "MTXTB034_TRANSACAO_AGENDAMENTO")
@NamedQueries({
	@NamedQuery(name = "Mtxtb034TransacaoAgendamento.findAll", query = "SELECT m FROM Mtxtb034TransacaoAgendamento m")
})
public class Mtxtb034TransacaoAgendamento {

	@Id
	@Column(name = "NU_NSU_TRANSACAO_AGENDAMENTO", unique = true, nullable = false, precision = 15)
	private long nuNsuTransacaoAgendamento;
	
	@Column(name = "DT_REFERENCIA", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dtReferencia;

	@Column(name = "DT_EFETIVACAO", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dtEfetivacao;
	
	@Column(name = "DE_IDENTIFICACAO_TRANSACAO", unique = true, precision = 25)
	private String deIdentificacaoTransacao;
	
	@Column(name = "IC_SITUACAO_AGENDAMENTO", unique = true, nullable = false, length = 1)
	private int icSituacao;
	
	@Column(name = "NU_SERVICO", unique = true, nullable = false, precision = 6)
	private long nuServico;
	
	@Column(name = "NU_VERSAO_SERVICO", unique = true, nullable = false, precision = 3)
	private int nuVersaoServico;
	
	@Column(name = "NU_CANAL", unique = true, nullable = false, precision = 3)
	private long nuCanal;
	
	@Column(name = "VR_TRANSACAO", nullable = false, length = 14, precision = 2)
	private BigDecimal valorTransacao;
	
	@Column(name = "NU_UNIDADE", nullable = false, length = 4)
    private int nuUnidade;

    @Column(name = "NU_PRODUTO", nullable = false, length = 4)
    private int nuProduto;

    @Column(name = "NU_CONTA", nullable = false, length = 12)
    private long nuConta;

    @Column(name = "NU_DV_CONTA", nullable = false, length = 1)
    private int dvConta;

    @Column(name = "IC_CONTA_SOLUCAO", nullable = false, length=1)
    private int icTipoConta;
    
    @Lob
    @Column(name = "DE_XML_AGENDAMENTO", nullable = false)
    private String deXmlAgendamento;
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "NU_SERVICO", referencedColumnName = "NU_SERVICO_001", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "NU_VERSAO_SERVICO", referencedColumnName = "NU_VERSAO_SERVICO", nullable = false, insertable = false, updatable = false) })
    private Mtxtb011VersaoServico mtxtb011VersaoServico;
	
    @ManyToOne
    @JoinColumn(name = "NU_CANAL", nullable = false, insertable = false, updatable = false)
	private Mtxtb004Canal mtxtb004Canal;
    
    @ManyToOne
    @JoinColumn(name = "NU_SERVICO", nullable = false, insertable = false, updatable = false)
	private Mtxtb001Servico mtxtb001Servico;

    public long getNuNsuTransacaoAgendamento() {
		return nuNsuTransacaoAgendamento;
	}

	public void setNuNsuTransacaoAgendamento(long nuNsuTransacaoAgendamento) {
		this.nuNsuTransacaoAgendamento = nuNsuTransacaoAgendamento;
	}

	public Date getDtReferencia() {
		return dtReferencia;
	}

	public void setDtReferencia(Date dtReferencia) {
		this.dtReferencia = dtReferencia;
	}

	public Date getDtEfetivacao() {
		return dtEfetivacao;
	}

	public void setDtEfetivacao(Date dtEfetivacao) {
		this.dtEfetivacao = dtEfetivacao;
	}

	public String getDeIdentificacaoTransacao() {
		return deIdentificacaoTransacao;
	}

	public void setDeIdentificacaoTransacao(String deIdentificacaoTransacao) {
		this.deIdentificacaoTransacao = deIdentificacaoTransacao;
	}

	public int getIcSituacao() {
		return icSituacao;
	}

	public void setIcSituacao(int icSituacao) {
		this.icSituacao = icSituacao;
	}

	public long getNuServico() {
		return nuServico;
	}

	public void setNuServico(long nuServico) {
		this.nuServico = nuServico;
	}

	public int getNuVersaoServico() {
		return nuVersaoServico;
	}

	public void setNuVersaoServico(int nuVersaoServico) {
		this.nuVersaoServico = nuVersaoServico;
	}

	public long getNuCanal() {
		return nuCanal;
	}

	public void setNuCanal(long nuCanal) {
		this.nuCanal = nuCanal;
	}

	public BigDecimal getValorTransacao() {
		return valorTransacao;
	}

	public void setValorTransacao(BigDecimal valorTransacao) {
		this.valorTransacao = valorTransacao;
	}

	public int getNuUnidade() {
		return nuUnidade;
	}

	public void setNuUnidade(int nuUnidade) {
		this.nuUnidade = nuUnidade;
	}

	public int getNuProduto() {
		return nuProduto;
	}

	public void setNuProduto(int nuProduto) {
		this.nuProduto = nuProduto;
	}

	public long getNuConta() {
		return nuConta;
	}

	public void setNuConta(long nuConta) {
		this.nuConta = nuConta;
	}

	public int getDvConta() {
		return dvConta;
	}

	public void setDvConta(int dvConta) {
		this.dvConta = dvConta;
	}

	public int getIcTipoConta() {
		return icTipoConta;
	}

	public void setIcTipoConta(int icTipoConta) {
		this.icTipoConta = icTipoConta;
	}

	public String getDeXmlAgendamento() {
		return deXmlAgendamento;
	}

	public void setDeXmlAgendamento(String deXmlAgendamento) {
		this.deXmlAgendamento = deXmlAgendamento;
	}

	public Mtxtb011VersaoServico getMtxtb011VersaoServico() {
		return mtxtb011VersaoServico;
	}

	public void setMtxtb011VersaoServico(Mtxtb011VersaoServico mtxtb011VersaoServico) {
		this.mtxtb011VersaoServico = mtxtb011VersaoServico;
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
