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
public class Mtxtb015SrvcoTrnsoTrfaPK implements Serializable {

	private static final long serialVersionUID = 1L;

    @Column(name = "NU_NSU_TRANSACAO_017", insertable = false, updatable = false, unique = true, nullable = false, precision = 15)
    private long nuNsuTransacao017;

    @Column(name = "NU_SERVICO_017", insertable = false, updatable = false, unique = true, nullable = false, precision = 4)
    private long nuServico017;

    @Column(name = "NU_VERSAO_SERVICO_017", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuVersaoServico017;

    @Column(name = "NU_TAREFA_012", insertable = false, updatable = false, unique = true, nullable = false, precision = 5)
    private long nuTarefa012;

    @Column(name = "NU_VERSAO_TAREFA_012", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuVersaoTarefa012;


    public long getNuNsuTransacao017() {
        return this.nuNsuTransacao017;
    }

    public void setNuNsuTransacao017(long nuNsuTransacao017) {
        this.nuNsuTransacao017 = nuNsuTransacao017;
    }

    public long getNuServico017() {
        return this.nuServico017;
    }

    public void setNuServico017(long nuServico017) {
        this.nuServico017 = nuServico017;
    }

    public long getNuVersaoServico017() {
        return this.nuVersaoServico017;
    }

    public void setNuVersaoServico017(long nuVersaoServico017) {
        this.nuVersaoServico017 = nuVersaoServico017;
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
        if (!(other instanceof Mtxtb015SrvcoTrnsoTrfaPK)) {
            return false;
        }
        Mtxtb015SrvcoTrnsoTrfaPK castOther = (Mtxtb015SrvcoTrnsoTrfaPK) other;
        return (this.nuNsuTransacao017 == castOther.nuNsuTransacao017) && (this.nuServico017 == castOther.nuServico017)
            && (this.nuVersaoServico017 == castOther.nuVersaoServico017) && (this.nuTarefa012 == castOther.nuTarefa012)
            && (this.nuVersaoTarefa012 == castOther.nuVersaoTarefa012);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuNsuTransacao017 ^ (this.nuNsuTransacao017 >>> 32)));
        hash = hash * prime + ((int) (this.nuServico017 ^ (this.nuServico017 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoServico017 ^ (this.nuVersaoServico017 >>> 32)));
        hash = hash * prime + ((int) (this.nuTarefa012 ^ (this.nuTarefa012 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoTarefa012 ^ (this.nuVersaoTarefa012 >>> 32)));

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb015SrvcoTrnsoTrfaPK [nuNsuTransacao017=");
        builder.append(nuNsuTransacao017);
        builder.append(", nuServico017=");
        builder.append(nuServico017);
        builder.append(", nuVersaoServico017=");
        builder.append(nuVersaoServico017);
        builder.append(", nuTarefa012=");
        builder.append(nuTarefa012);
        builder.append(", nuVersaoTarefa012=");
        builder.append(nuVersaoTarefa012);
        builder.append("]");
        return builder.toString();
    }


}
