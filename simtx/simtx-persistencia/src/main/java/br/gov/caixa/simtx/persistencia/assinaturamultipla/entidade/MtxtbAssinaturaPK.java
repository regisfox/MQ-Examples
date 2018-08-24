/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MtxtbAssinaturaPK implements Serializable {

	private static final long serialVersionUID = 194878317049478505L;

	@Column(name = "NU_NSU_TRNSO_ASNTA_MLTPA_027", insertable = false, updatable = false, unique = true, nullable = false, precision = 15)
	private long nsuTransacaoOrigem;

	@Column(name = "NU_NSU_TRNSO_CONTROLE", insertable = false, updatable = false, unique = true, nullable = false, precision = 15)
	private long nsuTransacao;

	public long getNsuTransacaoOrigem() {
		return nsuTransacaoOrigem;
	}

	public void setNsuTransacaoOrigem(long nsuTransacaoOrigem) {
		this.nsuTransacaoOrigem = nsuTransacaoOrigem;
	}

	public long getNsuTransacao() {
		return nsuTransacao;
	}

	public void setNsuTransacao(long nsuTransacao) {
		this.nsuTransacao = nsuTransacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (nsuTransacao ^ (nsuTransacao >>> 32));
		result = prime * result + (int) (nsuTransacaoOrigem ^ (nsuTransacaoOrigem >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MtxtbAssinaturaPK other = (MtxtbAssinaturaPK) obj;
		if (nsuTransacao != other.nsuTransacao)
			return false;
		if (nsuTransacaoOrigem != other.nsuTransacaoOrigem)
			return false;
		return true;
	}
	

}
