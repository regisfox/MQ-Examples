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
public class Mtxtb024TarefaFilaPK implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @Column(name = "NU_TAREFA_012", insertable = false, updatable = false, unique = true, nullable = false, precision = 5)
    private long nuTarefa012;

    @Column(name = "NU_VERSAO_TAREFA_012", unique = true, nullable = false, precision = 3)
    private long nuVersaoTarefa012;


    public long getNuTarefa012() {
        return this.nuTarefa012;
    }

    public void setNuTarefa012(long nuTarefa012) {
        this.nuTarefa012 = nuTarefa012;
    }

    public long getNuVersaoTarefa012() {
        return this.nuVersaoTarefa012;
    }

    public void setNuVersaoTarefa012(long nuVersaoTarefa) {
        this.nuVersaoTarefa012 = nuVersaoTarefa;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Mtxtb024TarefaFilaPK)) {
            return false;
        }
        Mtxtb024TarefaFilaPK castOther = (Mtxtb024TarefaFilaPK) other;
        return (this.nuTarefa012 == castOther.nuTarefa012) && (this.nuVersaoTarefa012 == castOther.nuVersaoTarefa012);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuTarefa012 ^ (this.nuTarefa012 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoTarefa012 ^ (this.nuVersaoTarefa012 >>> 32)));

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb012VersaoTarefaPK [nuTarefa012=");
        builder.append(nuTarefa012);
        builder.append(", nuVersaoTarefa12=");
        builder.append(nuVersaoTarefa012);
        builder.append("]");
        return builder.toString();
    }


}
