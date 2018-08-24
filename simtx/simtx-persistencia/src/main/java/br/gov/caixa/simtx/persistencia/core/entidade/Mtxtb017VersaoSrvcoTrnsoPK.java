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
public class Mtxtb017VersaoSrvcoTrnsoPK implements Serializable {

	private static final long serialVersionUID = 1L;

    @Column(name = "NU_SERVICO_011", insertable = false, updatable = false, unique = true, nullable = false, precision = 4)
    private long nuServico011;

    @Column(name = "NU_VERSAO_SERVICO_011", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuVersaoServico011;

    @Column(name = "NU_NSU_TRANSACAO_014", insertable = false, updatable = false, unique = true, nullable = false, precision = 15)
    private long nuNsuTransacao014;


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
        if (!(other instanceof Mtxtb017VersaoSrvcoTrnsoPK)) {
            return false;
        }
        Mtxtb017VersaoSrvcoTrnsoPK castOther = (Mtxtb017VersaoSrvcoTrnsoPK) other;
        return (this.nuServico011 == castOther.nuServico011)
            && (this.nuVersaoServico011 == castOther.nuVersaoServico011)
            && (this.nuNsuTransacao014 == castOther.nuNsuTransacao014);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuServico011 ^ (this.nuServico011 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoServico011 ^ (this.nuVersaoServico011 >>> 32)));
        hash = hash * prime + ((int) (this.nuNsuTransacao014 ^ (this.nuNsuTransacao014 >>> 32)));

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb017VersaoSrvcoTrnsoPK [nuServico011=");
        builder.append(nuServico011);
        builder.append(", nuVersaoServico011=");
        builder.append(nuVersaoServico011);
        builder.append(", nuNsuTransacao014=");
        builder.append(nuNsuTransacao014);
        builder.append("]");
        return builder.toString();
    }


}
