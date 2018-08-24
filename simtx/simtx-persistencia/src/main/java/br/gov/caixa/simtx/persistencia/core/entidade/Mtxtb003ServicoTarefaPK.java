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
public class Mtxtb003ServicoTarefaPK implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Column(name = "NU_SERVICO_011", insertable = false, updatable = false, unique = true, nullable = false, precision = 4)
    private long nuServico011;

    @Column(name = "NU_VERSAO_SERVICO_011", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuVersaoServico011;

    @Column(name = "NU_TAREFA_012", insertable = false, updatable = false, unique = true, nullable = false, precision = 5)
    private long nuTarefa012;

    @Column(name = "NU_VERSAO_TAREFA_012", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuVersaoTarefa012;

    public long getNuServico011() {
        return this.nuServico011;
    }

    public void setNuServico011(long nuServico011) {
        this.nuServico011 = nuServico011;
    }

    public long getNuVersaoServico011() {
        return this.nuVersaoServico011;
    }

    public void setNuVersaoServico011(long nuVersaoServico011) {
        this.nuVersaoServico011 = nuVersaoServico011;
    }

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

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Mtxtb003ServicoTarefaPK)) {
            return false;
        }
        Mtxtb003ServicoTarefaPK castOther = (Mtxtb003ServicoTarefaPK) other;
        return (this.nuServico011 == castOther.nuServico011)
            && (this.nuVersaoServico011 == castOther.nuVersaoServico011) && (this.nuTarefa012 == castOther.nuTarefa012)
            && (this.nuVersaoTarefa012 == castOther.nuVersaoTarefa012);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuServico011 ^ (this.nuServico011 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoServico011 ^ (this.nuVersaoServico011 >>> 32)));
        hash = hash * prime + ((int) (this.nuTarefa012 ^ (this.nuTarefa012 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoTarefa012 ^ (this.nuVersaoTarefa012 >>> 32)));
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb003ServicoTarefaPK [nuServico011=");
        builder.append(nuServico011);
        builder.append(", nuVersaoServico011=");
        builder.append(nuVersaoServico011);
        builder.append(", nuTarefa012=");
        builder.append(nuTarefa012);
        builder.append(", nuVersaoTarefa012=");
        builder.append(nuVersaoTarefa012);
        builder.append("]");
        return builder.toString();
    }
    
}
