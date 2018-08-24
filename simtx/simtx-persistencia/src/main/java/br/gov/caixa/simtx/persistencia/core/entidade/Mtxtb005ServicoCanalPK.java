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
public class Mtxtb005ServicoCanalPK implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Column(name = "NU_SERVICO_001", insertable = false, updatable = false, unique = true, nullable = false, precision = 4)
    private long nuServico001;

    @Column(name = "NU_CANAL_004", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuCanal004;

    public long getNuServico001() {
        return this.nuServico001;
    }

    public void setNuServico001(long nuServico001) {
        this.nuServico001 = nuServico001;
    }

    public long getNuCanal004() {
        return this.nuCanal004;
    }

    public void setNuCanal004(long nuCanal004) {
        this.nuCanal004 = nuCanal004;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Mtxtb005ServicoCanalPK)) {
            return false;
        }
        Mtxtb005ServicoCanalPK castOther = (Mtxtb005ServicoCanalPK) other;
        return (this.nuServico001 == castOther.nuServico001) && (this.nuCanal004 == castOther.nuCanal004);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuServico001 ^ (this.nuServico001 >>> 32)));
        hash = hash * prime + ((int) (this.nuCanal004 ^ (this.nuCanal004 >>> 32)));

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb005ServicoCanalPK [nuServico001=");
        builder.append(nuServico001);
        builder.append(", nuCanal004=");
        builder.append(nuCanal004);
        builder.append("]");
        return builder.toString();
    }


}
