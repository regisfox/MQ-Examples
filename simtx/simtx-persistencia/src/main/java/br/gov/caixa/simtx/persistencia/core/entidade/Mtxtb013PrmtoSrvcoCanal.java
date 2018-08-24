/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the MTXTB013_PRMTO_SRVCO_CANAL database table.
 * 
 */
@Entity
@Cacheable
@Table(name = "MTXTB013_PRMTO_SRVCO_CANAL")
@NamedQueries({
        @NamedQuery(name = "Mtxtb013PrmtoSrvcoCanal.findAll", query = "SELECT m FROM Mtxtb013PrmtoSrvcoCanal m"),
        @NamedQuery(name = "Mtxtb013PrmtoSrvcoCanal.buscarPorServicoCanal",
                query = "SELECT m FROM Mtxtb013PrmtoSrvcoCanal m where m.mtxtb005ServicoCanal.mtxtb001Servico.nuServico = :nuServico and m.mtxtb005ServicoCanal.mtxtb004Canal.nuCanal = :nuCanal") })
public class Mtxtb013PrmtoSrvcoCanal implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NU_PARAMETRO", unique = true, nullable = false, precision = 9)
    private long nuParametro;

    @Column(name = "DH_ATUALIZACAO", nullable = false)
    private Date dhAtualizacao;

    @Column(name = "DT_FIM_SLCTO_SRVCO")
    private Date dtFimSlctoSrvco;

    @Column(name = "DT_INCO_SLCTO_SRVCO")
    private Date dtIncoSlctoSrvco;

    @Column(name = "HH_LIMITE_FIM")
    private Date hhLimiteFim;

    @Column(name = "HH_LIMITE_INICIO")
    private Date hhLimiteInicio;

    @Column(name = "VR_MNMO_SLCTO_SRVCO", precision = 10, scale = 2)
    private BigDecimal vrMnmoSlctoSrvco;

    @Column(name = "VR_MXMO_SLCTO_SRVCO", precision = 10, scale = 2)
    private BigDecimal vrMxmoSlctoSrvco;

    @ManyToOne
    @JoinColumns({ @JoinColumn(name = "NU_CANAL_005", referencedColumnName = "NU_CANAL_004", nullable = false),
            @JoinColumn(name = "NU_SERVICO_005", referencedColumnName = "NU_SERVICO_001", nullable = false) })
    private Mtxtb005ServicoCanal mtxtb005ServicoCanal;


    public long getNuParametro() {
        return this.nuParametro;
    }

    public void setNuParametro(long nuParametro) {
        this.nuParametro = nuParametro;
    }

    public Date getDhAtualizacao() {
        return this.dhAtualizacao;
    }

    public void setDhAtualizacao(Date dhAtualizacao) {
        this.dhAtualizacao = dhAtualizacao;
    }

    public Date getDtFimSlctoSrvco() {
        return this.dtFimSlctoSrvco;
    }

    public void setDtFimSlctoSrvco(Date dtFimSlctoSrvco) {
        this.dtFimSlctoSrvco = dtFimSlctoSrvco;
    }

    public Date getDtIncoSlctoSrvco() {
        return this.dtIncoSlctoSrvco;
    }

    public void setDtIncoSlctoSrvco(Date dtIncoSlctoSrvco) {
        this.dtIncoSlctoSrvco = dtIncoSlctoSrvco;
    }

    public Date getHhLimiteFim() {
        return this.hhLimiteFim;
    }

    public void setHhLimiteFim(Date hhLimiteFim) {
        this.hhLimiteFim = hhLimiteFim;
    }

    public Date getHhLimiteInicio() {
        return this.hhLimiteInicio;
    }

    public void setHhLimiteInicio(Date hhLimiteInicio) {
        this.hhLimiteInicio = hhLimiteInicio;
    }

    public BigDecimal getVrMnmoSlctoSrvco() {
        return this.vrMnmoSlctoSrvco;
    }

    public void setVrMnmoSlctoSrvco(BigDecimal vrMnmoSlctoSrvco) {
        this.vrMnmoSlctoSrvco = vrMnmoSlctoSrvco;
    }

    public BigDecimal getVrMxmoSlctoSrvco() {
        return this.vrMxmoSlctoSrvco;
    }

    public void setVrMxmoSlctoSrvco(BigDecimal vrMxmoSlctoSrvco) {
        this.vrMxmoSlctoSrvco = vrMxmoSlctoSrvco;
    }

    public Mtxtb005ServicoCanal getMtxtb005ServicoCanal() {
        return this.mtxtb005ServicoCanal;
    }

    public void setMtxtb005ServicoCanal(Mtxtb005ServicoCanal mtxtb005ServicoCanal) {
        this.mtxtb005ServicoCanal = mtxtb005ServicoCanal;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb013PrmtoSrvcoCanal [nuParametro=");
        builder.append(nuParametro);
        builder.append(", ");
        if (dhAtualizacao != null) {
            builder.append("dhAtualizacao=");
            builder.append(dhAtualizacao);
            builder.append(", ");
        }
        if (dtFimSlctoSrvco != null) {
            builder.append("dtFimSlctoSrvco=");
            builder.append(dtFimSlctoSrvco);
            builder.append(", ");
        }
        if (dtIncoSlctoSrvco != null) {
            builder.append("dtIncoSlctoSrvco=");
            builder.append(dtIncoSlctoSrvco);
            builder.append(", ");
        }
        if (hhLimiteFim != null) {
            builder.append("hhLimiteFim=");
            builder.append(hhLimiteFim);
            builder.append(", ");
        }
        if (hhLimiteInicio != null) {
            builder.append("hhLimiteInicio=");
            builder.append(hhLimiteInicio);
            builder.append(", ");
        }
        if (vrMnmoSlctoSrvco != null) {
            builder.append("vrMnmoSlctoSrvco=");
            builder.append(vrMnmoSlctoSrvco);
            builder.append(", ");
        }
        if (vrMxmoSlctoSrvco != null) {
            builder.append("vrMxmoSlctoSrvco=");
            builder.append(vrMxmoSlctoSrvco);
            builder.append(", ");
        }
        if (mtxtb005ServicoCanal != null) {
            builder.append("mtxtb005ServicoCanal=");
            builder.append(mtxtb005ServicoCanal);
        }
        builder.append("]");
        return builder.toString();
    }

}
