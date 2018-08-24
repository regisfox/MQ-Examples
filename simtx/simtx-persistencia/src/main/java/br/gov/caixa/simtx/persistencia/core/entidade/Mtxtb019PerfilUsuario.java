/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "MTXTB019_PERFIL_USUARIO")
@NamedQuery(name = "Mtxtb019PerfilUsuario.findAll", query = "SELECT m FROM Mtxtb019PerfilUsuario m")
public class Mtxtb019PerfilUsuario implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CO_PERFIL_USUARIO", unique = true, nullable = false, length = 8)
    private String coPerfilUsuario;

    @Column(name = "DE_PERFIL_USUARIO", nullable = false, length = 30)
    private String dePerfilUsuario;

    @Column(name = "IC_PERFIL_USUARIO", nullable = false, precision = 1)
    private BigDecimal icPerfilUsuario;

    @OneToMany(mappedBy = "mtxtb019PerfilUsuario")
    private List<Mtxtb021UsuarioCanal> mtxtb021UsuarioCanals;


    public String getCoPerfilUsuario() {
        return this.coPerfilUsuario;
    }

    public void setCoPerfilUsuario(String coPerfilUsuario) {
        this.coPerfilUsuario = coPerfilUsuario;
    }

    public String getDePerfilUsuario() {
        return this.dePerfilUsuario;
    }

    public void setDePerfilUsuario(String dePerfilUsuario) {
        this.dePerfilUsuario = dePerfilUsuario;
    }

    public BigDecimal getIcPerfilUsuario() {
        return this.icPerfilUsuario;
    }

    public void setIcPerfilUsuario(BigDecimal icPerfilUsuario) {
        this.icPerfilUsuario = icPerfilUsuario;
    }

    public List<Mtxtb021UsuarioCanal> getMtxtb021UsuarioCanals() {
        return this.mtxtb021UsuarioCanals;
    }

    public void setMtxtb021UsuarioCanals(List<Mtxtb021UsuarioCanal> mtxtb021UsuarioCanals) {
        this.mtxtb021UsuarioCanals = mtxtb021UsuarioCanals;
    }

    public Mtxtb021UsuarioCanal addMtxtb021UsuarioCanal(Mtxtb021UsuarioCanal mtxtb021UsuarioCanal) {
        getMtxtb021UsuarioCanals().add(mtxtb021UsuarioCanal);
        mtxtb021UsuarioCanal.setMtxtb019PerfilUsuario(this);

        return mtxtb021UsuarioCanal;
    }

    public Mtxtb021UsuarioCanal removeMtxtb021UsuarioCanal(Mtxtb021UsuarioCanal mtxtb021UsuarioCanal) {
        getMtxtb021UsuarioCanals().remove(mtxtb021UsuarioCanal);
        mtxtb021UsuarioCanal.setMtxtb019PerfilUsuario(null);

        return mtxtb021UsuarioCanal;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb019PerfilUsuario [");
        if (coPerfilUsuario != null) {
            builder.append("coPerfilUsuario=");
            builder.append(coPerfilUsuario);
            builder.append(", ");
        }
        if (dePerfilUsuario != null) {
            builder.append("dePerfilUsuario=");
            builder.append(dePerfilUsuario);
            builder.append(", ");
        }
        if (icPerfilUsuario != null) {
            builder.append("icPerfilUsuario=");
            builder.append(icPerfilUsuario);
            builder.append(", ");
        }
        if (mtxtb021UsuarioCanals != null) {
            builder.append("mtxtb021UsuarioCanals=");
            builder.append(mtxtb021UsuarioCanals);
        }
        builder.append("]");
        return builder.toString();
    }


}
