/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Mtxtb016IteracaoCanalPK implements Serializable {

	private static final long serialVersionUID = 1L;

    @Transient
    private static final long NU_ITERACAO_CANAL = 1;
    
    @Column(name = "NU_ITERACAO_CANAL", unique = true, nullable = false, precision = 15)
    private long nuIteracaoCanal = NU_ITERACAO_CANAL;

    @Column(name = "NU_NSU_TRANSACAO_014", insertable = false, updatable = false, unique = true, nullable = false, precision = 15)
    private long nuNsuTransacao014;


    public long getNuInteracaoCanal() {
    	this.nuIteracaoCanal = NU_ITERACAO_CANAL;
        return nuIteracaoCanal;
    }

    public long getNuNsuTransacao014() {
        return this.nuNsuTransacao014;
    }

    public void setNuNsuTransacao014(long nuNsuTransacao014) {
        this.nuNsuTransacao014 = nuNsuTransacao014;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Mtxtb016IteracaoCanalPK)) {
            return false;
        }
        Mtxtb016IteracaoCanalPK castOther = (Mtxtb016IteracaoCanalPK) other;
        return (this.nuIteracaoCanal == castOther.nuIteracaoCanal)
            && (this.nuNsuTransacao014 == castOther.nuNsuTransacao014);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuIteracaoCanal ^ (this.nuIteracaoCanal >>> 32)));
        hash = hash * prime + ((int) (this.nuNsuTransacao014 ^ (this.nuNsuTransacao014 >>> 32)));

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb016IteracaoCanalPK [nuInteracaoCanal=");
        builder.append(nuIteracaoCanal);
        builder.append(", nuNsuTransacao014=");
        builder.append(nuNsuTransacao014);
        builder.append("]");
        return builder.toString();
    }


}
