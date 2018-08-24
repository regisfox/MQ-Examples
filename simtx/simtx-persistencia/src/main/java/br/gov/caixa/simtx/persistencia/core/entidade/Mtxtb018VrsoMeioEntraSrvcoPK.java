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
public class Mtxtb018VrsoMeioEntraSrvcoPK implements Serializable {

	private static final long serialVersionUID = 1L;

    @Column(name = "NU_MEIO_ENTRADA_008", insertable = false, updatable = false, unique = true, nullable = false, precision = 2)
    private long nuMeioEntrada008;

    @Column(name = "NU_SERVICO_011", insertable = false, updatable = false, unique = true, nullable = false, precision = 4)
    private long nuServico011;

    @Column(name = "NU_VERSAO_SERVICO_011", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuVersaoServico011;


    public long getNuMeioEntrada008() {
        return this.nuMeioEntrada008;
    }

    public void setNuMeioEntrada008(long nuMeioEntrada008) {
        this.nuMeioEntrada008 = nuMeioEntrada008;
    }

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

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Mtxtb018VrsoMeioEntraSrvcoPK)) {
            return false;
        }
        Mtxtb018VrsoMeioEntraSrvcoPK castOther = (Mtxtb018VrsoMeioEntraSrvcoPK) other;
        return (this.nuMeioEntrada008 == castOther.nuMeioEntrada008)
            && (this.nuServico011 == castOther.nuServico011)
            && (this.nuVersaoServico011 == castOther.nuVersaoServico011);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuMeioEntrada008 ^ (this.nuMeioEntrada008 >>> 32)));
        hash = hash * prime + ((int) (this.nuServico011 ^ (this.nuServico011 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoServico011 ^ (this.nuVersaoServico011 >>> 32)));

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb018VrsoMeioEntraSrvcoPK [nuMeioEntrada009=");
        builder.append(nuMeioEntrada008);
        builder.append(", nuServico011=");
        builder.append(nuServico011);
        builder.append(", nuVersaoServico011=");
        builder.append(nuVersaoServico011);
        builder.append("]");
        return builder.toString();
    }
}
