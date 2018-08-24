/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Mtxtb026ServicoTarefaRegrasPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "NU_REGRA_025", insertable = false, updatable = false, unique = true, nullable = false, precision = 6)
    private long nuRegra025;

	@Column(name = "NU_SERVICO_003", insertable = false, updatable = false, unique = true, nullable = false, precision = 6)
    private long nuServico003;

	@Column(name = "NU_VERSAO_SERVICO_003", insertable = false, updatable = false, unique = true, nullable = false, precision = 6)
    private long nuVersaoServico003;

	@Column(name = "NU_TAREFA_003", insertable = false, updatable = false, unique = true, nullable = false, precision = 6)
    private long nuTarefa003;

	@Column(name = "NU_VERSAO_TAREFA_003", insertable = false, updatable = false, unique = true, nullable = false, precision = 6)
    private long nuVersaoTarefa003;




	public long getNuRegra025() {
		return nuRegra025;
	}

	public void setNuRegra025(long nuRegra025) {
		this.nuRegra025 = nuRegra025;
	}

	public long getNuServico003() {
		return nuServico003;
	}

	public void setNuServico003(long nuServico003) {
		this.nuServico003 = nuServico003;
	}

	public long getNuVersaoServico003() {
		return nuVersaoServico003;
	}

	public void setNuVersaoServico003(long nuVersaoServico003) {
		this.nuVersaoServico003 = nuVersaoServico003;
	}

	public long getNuTarefa003() {
		return nuTarefa003;
	}

	public void setNuTarefa003(long nuTarefa003) {
		this.nuTarefa003 = nuTarefa003;
	}

	public long getNuVersaoTarefa003() {
		return nuVersaoTarefa003;
	}

	public void setNuVersaoTarefa003(long nuVersaoTarefa003) {
		this.nuVersaoTarefa003 = nuVersaoTarefa003;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (nuRegra025 ^ (nuRegra025 >>> 32));
		result = prime * result + (int) (nuServico003 ^ (nuServico003 >>> 32));
		result = prime * result + (int) (nuTarefa003 ^ (nuTarefa003 >>> 32));
		result = prime * result + (int) (nuVersaoServico003 ^ (nuVersaoServico003 >>> 32));
		result = prime * result + (int) (nuVersaoTarefa003 ^ (nuVersaoTarefa003 >>> 32));
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
		Mtxtb026ServicoTarefaRegrasPK other = (Mtxtb026ServicoTarefaRegrasPK) obj;
		if (nuRegra025 != other.nuRegra025)
			return false;
		if (nuServico003 != other.nuServico003)
			return false;
		if (nuTarefa003 != other.nuTarefa003)
			return false;
		if (nuVersaoServico003 != other.nuVersaoServico003)
			return false;
		if (nuVersaoTarefa003 != other.nuVersaoTarefa003)
			return false;
		return true;
	}

}
