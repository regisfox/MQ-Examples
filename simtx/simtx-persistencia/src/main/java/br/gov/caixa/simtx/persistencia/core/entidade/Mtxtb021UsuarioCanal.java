/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "MTXTB021_USUARIO_CANAL")
@NamedQuery(name = "Mtxtb021UsuarioCanal.findAll", query = "SELECT m FROM Mtxtb021UsuarioCanal m")
public class Mtxtb021UsuarioCanal implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb021UsuarioCanalPK id;

    @Column(name = "DH_ATUALIZACAO", nullable = false)
    private Date dhAtualizacao;

    @Column(name = "IC_ALTERACAO", nullable = false, precision = 1)
    private BigDecimal icAlteracao;

    @Column(name = "IC_CONSULTA", nullable = false, precision = 1)
    private BigDecimal icConsulta;

    @Column(name = "IC_EXCLUSAO", nullable = false, precision = 1)
    private BigDecimal icExclusao;

    @Column(name = "IC_INCLUSAO", nullable = false, precision = 1)
    private BigDecimal icInclusao;

    @ManyToOne
    @JoinColumn(name = "NU_CANAL_004", nullable = false, insertable = false, updatable = false)
    private Mtxtb004Canal mtxtb004Canal;

    @ManyToOne
    @JoinColumn(name = "CO_PERFIL_USUARIO_019", nullable = false, insertable = false, updatable = false)
    private Mtxtb019PerfilUsuario mtxtb019PerfilUsuario;


    public Mtxtb021UsuarioCanalPK getId() {
        return this.id;
    }

    public void setId(Mtxtb021UsuarioCanalPK id) {
        this.id = id;
    }

    public Date getDhAtualizacao() {
        return this.dhAtualizacao;
    }

    public void setDhAtualizacao(Date dhAtualizacao) {
        this.dhAtualizacao = dhAtualizacao;
    }

    public BigDecimal getIcAlteracao() {
        return this.icAlteracao;
    }

    public void setIcAlteracao(BigDecimal icAlteracao) {
        this.icAlteracao = icAlteracao;
    }

    public BigDecimal getIcConsulta() {
        return this.icConsulta;
    }

    public void setIcConsulta(BigDecimal icConsulta) {
        this.icConsulta = icConsulta;
    }

    public BigDecimal getIcExclusao() {
        return this.icExclusao;
    }

    public void setIcExclusao(BigDecimal icExclusao) {
        this.icExclusao = icExclusao;
    }

    public BigDecimal getIcInclusao() {
        return this.icInclusao;
    }

    public void setIcInclusao(BigDecimal icInclusao) {
        this.icInclusao = icInclusao;
    }

    public Mtxtb004Canal getMtxtb004Canal() {
        return this.mtxtb004Canal;
    }

    public void setMtxtb004Canal(Mtxtb004Canal mtxtb004Canal) {
        this.mtxtb004Canal = mtxtb004Canal;
    }

    public Mtxtb019PerfilUsuario getMtxtb019PerfilUsuario() {
        return this.mtxtb019PerfilUsuario;
    }

    public void setMtxtb019PerfilUsuario(Mtxtb019PerfilUsuario mtxtb019PerfilUsuario) {
        this.mtxtb019PerfilUsuario = mtxtb019PerfilUsuario;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb021UsuarioCanal [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (dhAtualizacao != null) {
            builder.append("dhAtualizacao=");
            builder.append(dhAtualizacao);
            builder.append(", ");
        }
        if (icAlteracao != null) {
            builder.append("icAlteracao=");
            builder.append(icAlteracao);
            builder.append(", ");
        }
        if (icConsulta != null) {
            builder.append("icConsulta=");
            builder.append(icConsulta);
            builder.append(", ");
        }
        if (icExclusao != null) {
            builder.append("icExclusao=");
            builder.append(icExclusao);
            builder.append(", ");
        }
        if (icInclusao != null) {
            builder.append("icInclusao=");
            builder.append(icInclusao);
            builder.append(", ");
        }
        if (mtxtb004Canal != null) {
            builder.append("mtxtb004Canal=");
            builder.append(mtxtb004Canal);
            builder.append(", ");
        }
        if (mtxtb019PerfilUsuario != null) {
            builder.append("mtxtb019PerfilUsuario=");
            builder.append(mtxtb019PerfilUsuario);
        }
        builder.append("]");
        return builder.toString();
    }


}
