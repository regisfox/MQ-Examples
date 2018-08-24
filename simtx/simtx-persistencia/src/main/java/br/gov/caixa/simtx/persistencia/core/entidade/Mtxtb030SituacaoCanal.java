package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the MTXTB030_SITUACAO_CANAL database table.
 * 
 */
@Entity
@Table(name = "MTXTB030_SITUACAO_CANAL")
public class Mtxtb030SituacaoCanal implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "NU_CANAL_004")
	private Long nuCanal;
	
	@Column(name = "TS_ALTERACAO")
	private Date dataTsAlteracao;
	
	@Column(name = "IC_SITUACAO_CANAL")
	private int icSituacaoCanal;
	
	@Column(name = "CO_USUARIO_ALTERACAO")
	private String coUsuarioAlteracao;
	
	@Column(name = "CO_MAQUINA_ALTERACAO")
	private String coMaquinaAlteracao;

	public Long getNuCanal() {
		return nuCanal;
	}

	public void setNuCanal(Long nuCanal) {
		this.nuCanal = nuCanal;
	}

	public Date getDataTsAlteracao() {
		return dataTsAlteracao;
	}

	public void setDataTsAlteracao(Date dataTsAlteracao) {
		this.dataTsAlteracao = dataTsAlteracao;
	}

	public int getIcSituacaoCanal() {
		return icSituacaoCanal;
	}

	public void setIcSituacaoCanal(int icSituacaoCanal) {
		this.icSituacaoCanal = icSituacaoCanal;
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
