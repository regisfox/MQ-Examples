package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the MTXTB036_TRANSACAO_AUDITADA database table.
 * 
 */
@Entity
@Table(name = "MTXTB036_TRANSACAO_AUDITADA")
public class Mtxtb036TransacaoAuditada implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "NU_NSU_TRANSACAO_AUDITADA")
	private Long nuTransacaoAuditada;
	
	@Column(name = "NU_SERVICO_011")
	private Long nuServico;
	
	@Column(name = "NU_VERSAO_SERVICO_011")
	private int nuVersaoServico;
	
	@Column(name = "NU_NSU_TRANSACAO_ORIGEM")
	private Long nuTransacaoOrigem;
	
	@Column(name = "NU_CANAL_ORIGEM_005")
	private Long nuCanalOrigem;
	
	@Column(name = "NU_SERVICO_ORIGEM_011")
	private Long nuServicoOrigem;
	
	@Column(name = "NU_VERSAO_SERVICO_ORIGEM_011")
	private int nuVersaoServicoOrigem;
	
	@Column(name = "CO_USUARIO")
	private String coUsuario;
	
	@Column(name = "CO_MAQUINA_INCLUSAO")
	private String coMaquinaInclusao;
	
	@Column(name = "TS_INCLUSAO")
	private Timestamp tsInclusao ;

	public Long getNuTransacaoAuditada() {
		return nuTransacaoAuditada;
	}

	public void setNuTransacaoAuditada(Long nuTransacaoAuditada) {
		this.nuTransacaoAuditada = nuTransacaoAuditada;
	}

	public Long getNuServico() {
		return nuServico;
	}

	public void setNuServico(Long nuServico) {
		this.nuServico = nuServico;
	}

	public int getNuVersaoServico() {
		return nuVersaoServico;
	}

	public void setNuVersaoServico(int nuVersaoServico) {
		this.nuVersaoServico = nuVersaoServico;
	}

	public Long getNuTransacaoOrigem() {
		return nuTransacaoOrigem;
	}

	public void setNuTransacaoOrigem(Long nuTransacaoOrigem) {
		this.nuTransacaoOrigem = nuTransacaoOrigem;
	}

	public Long getNuCanalOrigem() {
		return nuCanalOrigem;
	}

	public void setNuCanalOrigem(Long nuCanalOrigem) {
		this.nuCanalOrigem = nuCanalOrigem;
	}

	public Long getNuServicoOrigem() {
		return nuServicoOrigem;
	}

	public void setNuServicoOrigem(Long nuServicoOrigem) {
		this.nuServicoOrigem = nuServicoOrigem;
	}

	public int getNuVersaoServicoOrigem() {
		return nuVersaoServicoOrigem;
	}

	public void setNuVersaoServicoOrigem(int nuVersaoServicoOrigem) {
		this.nuVersaoServicoOrigem = nuVersaoServicoOrigem;
	}

	public String getCoUsuario() {
		return coUsuario;
	}

	public void setCoUsuario(String coUsuario) {
		this.coUsuario = coUsuario;
	}

	public String getCoMaquinaInclusao() {
		return coMaquinaInclusao;
	}

	public void setCoMaquinaInclusao(String coMaquinaInclusao) {
		this.coMaquinaInclusao = coMaquinaInclusao;
	}

	public Timestamp getTsInclusao() {
		return tsInclusao;
	}

	public void setTsInclusao(Timestamp tsInclusao) {
		this.tsInclusao = tsInclusao;
	}

}