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
public class Mtxtb020SrvcoTarfaCanalPK implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @Column(name = "NU_SERVICO_003", insertable = false, updatable = false, unique = true, nullable = false, precision = 4)
    private long nuServico003;

    @Column(name = "NU_VERSAO_SERVICO_003", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuVersaoServico003;

    @Column(name = "NU_TAREFA_003", insertable = false, updatable = false, unique = true, nullable = false, precision = 5)
    private long nuTarefa003;

    @Column(name = "NU_VERSAO_TAREFA_003", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuVersaoTarefa003;

    @Column(name = "NU_CANAL_004", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuCanal004;
    

    public long getNuServico003() {
        return this.nuServico003;
    }

    public void setNuServico003(long nuServico003) {
        this.nuServico003 = nuServico003;
    }

    public long getNuVersaoServico003() {
        return this.nuVersaoServico003;
    }

    public void setNuVersaoServico003(long nuVersaoServico003) {
        this.nuVersaoServico003 = nuVersaoServico003;
    }

    public long getNuTarefa003() {
        return this.nuTarefa003;
    }

    public void setNuTarefa003(long nuTarefa003) {
        this.nuTarefa003 = nuTarefa003;
    }

    public long getNuVersaoTarefa003() {
        return this.nuVersaoTarefa003;
    }

    public void setNuVersaoTarefa003(long nuVersaoTarefa003) {
        this.nuVersaoTarefa003 = nuVersaoTarefa003;
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
        if (!(other instanceof Mtxtb020SrvcoTarfaCanalPK)) {
            return false;
        }
        Mtxtb020SrvcoTarfaCanalPK castOther = (Mtxtb020SrvcoTarfaCanalPK) other;
        return (this.nuServico003 == castOther.nuServico003)
            && (this.nuVersaoServico003 == castOther.nuVersaoServico003) && (this.nuTarefa003 == castOther.nuTarefa003)
            && (this.nuVersaoTarefa003 == castOther.nuVersaoTarefa003) && (this.nuCanal004 == castOther.nuCanal004);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuServico003 ^ (this.nuServico003 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoServico003 ^ (this.nuVersaoServico003 >>> 32)));
        hash = hash * prime + ((int) (this.nuTarefa003 ^ (this.nuTarefa003 >>> 32)));
        hash = hash * prime + ((int) (this.nuVersaoTarefa003 ^ (this.nuVersaoTarefa003 >>> 32)));
        hash = hash * prime + ((int) (this.nuCanal004 ^ (this.nuCanal004 >>> 32)));

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb020SrvcoTarfaCanalPK [nuServico003=");
        builder.append(nuServico003);
        builder.append(", nuVersaoServico003=");
        builder.append(nuVersaoServico003);
        builder.append(", nuTarefa003=");
        builder.append(nuTarefa003);
        builder.append(", nuVersaoTarefa003=");
        builder.append(nuVersaoTarefa003);
        builder.append(", nuCanal004=");
        builder.append(nuCanal004);
        builder.append("]");
        return builder.toString();
    }


}
