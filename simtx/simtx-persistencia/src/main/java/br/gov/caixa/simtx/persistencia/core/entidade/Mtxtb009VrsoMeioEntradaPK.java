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
public class Mtxtb009VrsoMeioEntradaPK implements Serializable {

	private static final long serialVersionUID = 1L;

    @Column(name = "NU_MEIO_ENTRADA_008", insertable = false, updatable = false, unique = true, nullable = false, precision = 2)
    private long nuMeioEntrada008;

    @Column(name = "NU_VERSAO_MEIO_ENTRADA", unique = true, nullable = false, precision = 3)
    private long nuVersaoMeioEntrada;


    public long getNuMeioEntrada008() {
        return this.nuMeioEntrada008;
    }

    public void setNuMeioEntrada008(long nuMeioEntrada008) {
        this.nuMeioEntrada008 = nuMeioEntrada008;
    }

    public long getNuVersaoMeioEntrada() {
        return this.nuVersaoMeioEntrada;
    }

    public void setNuVersaoMeioEntrada(long nuVersaoMeioEntrada) {
        this.nuVersaoMeioEntrada = nuVersaoMeioEntrada;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Mtxtb009VrsoMeioEntradaPK)) {
            return false;
        }
        Mtxtb009VrsoMeioEntradaPK castOther = (Mtxtb009VrsoMeioEntradaPK) other;
        return (this.nuMeioEntrada008 == castOther.nuMeioEntrada008)
            && (this.nuVersaoMeioEntrada == castOther.nuVersaoMeioEntrada);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuMeioEntrada008 ^ (this.nuMeioEntrada008 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoMeioEntrada ^ (this.nuVersaoMeioEntrada >>> 32)));

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb009VrsoMeioEntradaPK [nuMeioEntrada008=");
        builder.append(nuMeioEntrada008);
        builder.append(", nuVersaoMeioEntrada=");
        builder.append(nuVersaoMeioEntrada);
        builder.append("]");
        return builder.toString();
    }


}
