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
public class Mtxtb021UsuarioCanalPK implements Serializable {

	private static final long serialVersionUID = 1L;

    @Column(name = "NU_CANAL_004", insertable = false, updatable = false, unique = true, nullable = false, precision = 3)
    private long nuCanal004;

    @Column(name = "CO_PERFIL_USUARIO_019", insertable = false, updatable = false, unique = true, nullable = false, length = 8)
    private String coPerfilUsuario019;


    public long getNuCanal004() {
        return this.nuCanal004;
    }

    public void setNuCanal004(long nuCanal004) {
        this.nuCanal004 = nuCanal004;
    }

    public String getCoPerfilUsuario019() {
        return this.coPerfilUsuario019;
    }

    public void setCoPerfilUsuario019(String coPerfilUsuario019) {
        this.coPerfilUsuario019 = coPerfilUsuario019;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Mtxtb021UsuarioCanalPK)) {
            return false;
        }
        Mtxtb021UsuarioCanalPK castOther = (Mtxtb021UsuarioCanalPK) other;
        return (this.nuCanal004 == castOther.nuCanal004)
            && this.coPerfilUsuario019.equals(castOther.coPerfilUsuario019);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.nuCanal004 ^ (this.nuCanal004 >>> 32)));
        hash = hash * prime + this.coPerfilUsuario019.hashCode();

        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb021UsuarioCanalPK [nuCanal004=");
        builder.append(nuCanal004);
        builder.append(", ");
        if (coPerfilUsuario019 != null) {
            builder.append("coPerfilUsuario019=");
            builder.append(coPerfilUsuario019);
        }
        builder.append("]");
        return builder.toString();
    }


}
