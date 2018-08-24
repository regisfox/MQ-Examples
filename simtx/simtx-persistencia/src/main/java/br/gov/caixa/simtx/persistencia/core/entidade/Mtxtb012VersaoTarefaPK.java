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
public class Mtxtb012VersaoTarefaPK implements Serializable {

	private static final long serialVersionUID = 1L;

    @Column(name = "NU_TAREFA_002", insertable = false, updatable = false, unique = true, nullable = false, precision = 5)
    private long nuTarefa002;

    @Column(name = "NU_VERSAO_TAREFA", unique = true, nullable = false, precision = 3)
    private long nuVersaoTarefa;


    public long getNuTarefa002() {
        return this.nuTarefa002;
    }

    public void setNuTarefa002(long nuTarefa002) {
        this.nuTarefa002 = nuTarefa002;
    }

    public long getNuVersaoTarefa() {
        return this.nuVersaoTarefa;
    }

    public void setNuVersaoTarefa(long nuVersaoTarefa) {
        this.nuVersaoTarefa = nuVersaoTarefa;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Mtxtb012VersaoTarefaPK)) {
            return false;
        }
        Mtxtb012VersaoTarefaPK castOther = (Mtxtb012VersaoTarefaPK) other;
        return (this.nuTarefa002 == castOther.nuTarefa002) && (this.nuVersaoTarefa == castOther.nuVersaoTarefa);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuTarefa002 ^ (this.nuTarefa002 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoTarefa ^ (this.nuVersaoTarefa >>> 32)));

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb012VersaoTarefaPK [nuTarefa002=");
        builder.append(nuTarefa002);
        builder.append(", nuVersaoTarefa=");
        builder.append(nuVersaoTarefa);
        builder.append("]");
        return builder.toString();
    }


}
