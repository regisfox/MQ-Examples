package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the MTXTB031_SITUACAO_SRVCO_CNL database table.
 * 
 */
@Entity
@Table(name = "MTXTB031_SITUACAO_SRVCO_CNL")
public class Mtxtb031SituacaoServicoCanal implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "NU_SERVICO_005")
	private Long nuServico;
	
	@Column(name = "NU_CANAL_005")
	private Long nuCanal;
	
	@Column(name = "TS_ALTERACAO")
	private Date dataTsAlteracao;
	
	@Column(name = "IC_STCO_SRVCO_CNL")
	private int icSituacaoServicoCanal;
	
	@Column(name = "CO_USUARIO_ALTERACAO")
	private String coUsuarioAlteracao;
	
	@Column(name = "CO_MAQUINA_ALTERACAO")
	private String coMAquinaAlteracao;

	public Long getNuServico() {
		return nuServico;
	}

	public void setNuServico(Long nuServico) {
		this.nuServico = nuServico;
	}

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

	public int getIcSituacaoServicoCanal() {
		return icSituacaoServicoCanal;
	}

	public void setIcSituacaoServicoCanal(int icSituacaoServicoCanal) {
		this.icSituacaoServicoCanal = icSituacaoServicoCanal;
	}

	public String getCoUsuarioAlteracao() {
		return coUsuarioAlteracao;
	}

	public void setCoUsuarioAlteracao(String coUsuarioAlteracao) {
		this.coUsuarioAlteracao = coUsuarioAlteracao;
	}

	public String getCoMAquinaAlteracao() {
		return coMAquinaAlteracao;
	}

	public void setCoMAquinaAlteracao(String coMAquinaAlteracao) {
		this.coMAquinaAlteracao = coMAquinaAlteracao;
	}
}
