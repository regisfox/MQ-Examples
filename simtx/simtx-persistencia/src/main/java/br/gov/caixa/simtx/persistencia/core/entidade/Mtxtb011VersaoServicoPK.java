/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the MTXTB011_VERSAO_SERVICO database table.
 * 
 */
@Embeddable
public class Mtxtb011VersaoServicoPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "NU_SERVICO_001", insertable = false, updatable = false, unique = true, nullable = false, precision = 4)
    private long nuServico001;

    @Column(name = "NU_VERSAO_SERVICO", unique = true, nullable = false, precision = 3)
    private int nuVersaoServico;

    public Mtxtb011VersaoServicoPK() {
    }
    
    public Mtxtb011VersaoServicoPK(long nuServico, int versaoServico) {
    	this.nuServico001 = nuServico;
    	this.nuVersaoServico = versaoServico;
    }

    public long getNuServico001() {
        return this.nuServico001;
    }

    public void setNuServico001(long nuServico001) {
        this.nuServico001 = nuServico001;
    }

    public int getNuVersaoServico() {
        return this.nuVersaoServico;
    }

    public void setNuVersaoServico(int nuVersaoServico) {
        this.nuVersaoServico = nuVersaoServico;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Mtxtb011VersaoServicoPK)) {
            return false;
        }
        Mtxtb011VersaoServicoPK castOther = (Mtxtb011VersaoServicoPK) other;
        return (this.nuServico001 == castOther.nuServico001) && (this.nuVersaoServico == castOther.nuVersaoServico);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuServico001 ^ (this.nuServico001 >>> 32)));
        hash = hash * prime + (this.nuVersaoServico ^ (this.nuVersaoServico >>> 32));

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb011VersaoServicoPK [nuServico001=");
        builder.append(nuServico001);
        builder.append(", nuVersaoServico=");
        builder.append(nuVersaoServico);
        builder.append("]");
        return builder.toString();
    }


}
