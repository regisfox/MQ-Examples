package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the MTXTB029_SITUACAO_VRSO_SERVICO database table.
 * 
 */
@Entity
@Table(name = "MTXTB029_SITUACAO_VRSO_SERVICO")
public class Mtxtb029SituacaoVersaoServico implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "NU_SERVICO_011")
	private Long nuServico;
	
	@Column(name = "NU_VERSAO_SERVICO_011")
	private int nuVersaoServico;
	
	@Column(name = "TS_ALTERACAO")
	private Date dataTsAlteracao;
	
	@Column(name = "IC_STCO_VRSO_SRVCO")
	private int icSituacaoVersaoServico;
	
	@Column(name = "CO_USUARIO_ALTERACAO")
	private String coUsuarioAlteracao;
	
	@Column(name = "CO_MAQUINA_ALTERACAO")
	private String coMaquinaAlteracao;

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

	public Date getDataTsAlteracao() {
		return dataTsAlteracao;
	}

	public void setDataTsAlteracao(Date dataTsAlteracao) {
		this.dataTsAlteracao = dataTsAlteracao;
	}

	public int getIcSituacaoVersaoServico() {
		return icSituacaoVersaoServico;
	}

	public void setIcSituacaoVersaoServico(int icSituacaoVersaoServico) {
		this.icSituacaoVersaoServico = icSituacaoVersaoServico;
	}

	public String getCoUsuarioAlteracao() {
		return coUsuarioAlteracao;
	}

	public void setCoUsuarioAlteracao(String coUsuarioAlteracao) {
		this.coUsuarioAlteracao = coUsuarioAlteracao;
	}

	public String getCoMaquinaAlteracao() {
		return coMaquinaAlteracao;
	}

	public void setCoMaquinaAlteracao(String coMaquinaAlteracao) {
		this.coMaquinaAlteracao = coMaquinaAlteracao;
	}
}
