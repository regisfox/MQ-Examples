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
public class Mtxtb010VrsoTarfaMeioEntraPK implements Serializable {

	private static final long serialVersionUID = 1L;

    @Column(name = "NU_MEIO_ENTRADA_008", insertable = false, updatable = false, unique = true, nullable = false, precision = 2)
    private long nuMeioEntrada008;

    @Column(name = "NU_TAREFA_012", insertable = false, updatable = false, unique = true, nullable = false, precision = 5)
    private long nuTarefa012;

    @Column(name = "NU_VERSAO_TAREFA_012", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuVersaoTarefa012;


    public long getNuMeioEntrada008() {
        return this.nuMeioEntrada008;
    }

    public void setNuMeioEntrada008(long nuMeioEntrada008) {
        this.nuMeioEntrada008 = nuMeioEntrada008;
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
        if (!(other instanceof Mtxtb010VrsoTarfaMeioEntraPK)) {
            return false;
        }
        Mtxtb010VrsoTarfaMeioEntraPK castOther = (Mtxtb010VrsoTarfaMeioEntraPK) other;
        return (this.nuMeioEntrada008 == castOther.nuMeioEntrada008)
            && (this.nuTarefa012 == castOther.nuTarefa012) && (this.nuVersaoTarefa012 == castOther.nuVersaoTarefa012);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuMeioEntrada008 ^ (this.nuMeioEntrada008 >>> 32)));
        hash = hash * prime + ((int) (this.nuTarefa012 ^ (this.nuTarefa012 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoTarefa012 ^ (this.nuVersaoTarefa012 >>> 32)));

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb010VrsoTarfaMeioEntraPK [nuMeioEntrada009=");
        builder.append(nuMeioEntrada008);
        builder.append(", nuTarefa012=");
        builder.append(nuTarefa012);
        builder.append(", nuVersaoTarefa012=");
        builder.append(nuVersaoTarefa012);
        builder.append("]");
        return builder.toString();
    }


}
