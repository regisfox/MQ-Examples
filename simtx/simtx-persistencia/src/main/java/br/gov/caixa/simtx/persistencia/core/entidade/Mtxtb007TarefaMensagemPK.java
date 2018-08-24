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
public class Mtxtb007TarefaMensagemPK implements Serializable {

	private static final long serialVersionUID = 1L;

    @Column(name = "NU_TAREFA_012", insertable = false, updatable = false, unique = true, nullable = false, precision = 6)
    private long nuTarefa012;

    @Column(name = "NU_VERSAO_TAREFA_012", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuVersaoTarefa012;
    
    @Column(name = "NU_MENSAGEM_006", insertable = false, updatable = false, unique = true, nullable = false, precision = 5)
    private long nuMensagem006;


    public long getNuTarefa012() {
        return this.nuTarefa012;
    }

    public void setNuTarefa012(long nuTarefa012) {
        this.nuTarefa012 = nuTarefa012;
    }

    public long getNuVersaoTarefa012() {
        return this.nuVersaoTarefa012;
    }

    public void setNuVersaoTarefa012(long nuVersaoTarefa012) {
        this.nuVersaoTarefa012 = nuVersaoTarefa012;
    }

	public long getNuMensagem006() {
		return nuMensagem006;
	}

	public void setNuMensagem006(long nuMensagem006) {
		this.nuMensagem006 = nuMensagem006;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (nuMensagem006 ^ (nuMensagem006 >>> 32));
		result = prime * result + (int) (nuTarefa012 ^ (nuTarefa012 >>> 32));
		result = prime * result + (int) (nuVersaoTarefa012 ^ (nuVersaoTarefa012 >>> 32));
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
		Mtxtb007TarefaMensagemPK other = (Mtxtb007TarefaMensagemPK) obj;
		if (nuMensagem006 != other.nuMensagem006)
			return false;
		if (nuTarefa012 != other.nuTarefa012)
			return false;
		if (nuVersaoTarefa012 != other.nuVersaoTarefa012)
			return false;
		return true;
	}

    
}
