/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "MTXTB028_CNTLE_ASNTA_MLTPA")
public class Mtxtb028ControleAssinaturaMultipla implements Serializable {
	
	private static final long serialVersionUID = 8715040185676928616L;

	public static final int INDICADOR_VALIDACAO_ASSINATURA_NAO = 0;
	public static final int INDICADOR_VALIDACAO_ASSINATURA_SIM = 1;

	public Mtxtb028ControleAssinaturaMultipla() {
		super();
	}
	
	public Mtxtb028ControleAssinaturaMultipla(int indicadorValidacaoAssinatura) {
		super();
		this.indicadorValidacaoAssinatura = indicadorValidacaoAssinatura;
	}

	@EmbeddedId
	private MtxtbAssinaturaPK id;
	
	@Column(name = "NU_CPF", nullable = true, length = 11)
	private Long cpf;
	
	@Column(name = "DH_MULTICANAL", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
	private Date dataMulticanal;
	
	@Column(name = "NU_CANAL", length = 3, nullable = false)
	private long canal;
	
	@Column(name = "IC_VALIDACAO_ASSINATURA", length = 1, nullable = false)
	private int indicadorValidacaoAssinatura;

	public MtxtbAssinaturaPK getId() {
		return id;
	}

	public void setId(MtxtbAssinaturaPK id) {
		this.id = id;
	}

	public Long  getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public Date getDataMulticanal() {
		return dataMulticanal;
	}

	public void setDataMulticanal(Date dataMulticanal) {
		this.dataMulticanal = dataMulticanal;
	}

	public long getCanal() {
		return canal;
	}

	public void setCanal(long canal) {
		this.canal = canal;
	}

	public int getIndicadorValidacaoAssinatura() {
		return indicadorValidacaoAssinatura;
	}

	public void setIndicadorValidacaoAssinatura(int indicadorValidacaoAssinatura) {
		this.indicadorValidacaoAssinatura = indicadorValidacaoAssinatura;
	}
}
