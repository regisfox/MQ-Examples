/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Cacheable
@Table(name = "MTXTB018_VRSO_MEIO_ENTRA_SRVCO")
@NamedQuery(name = "Mtxtb018VrsoMeioEntraSrvco.findAll", query = "SELECT m FROM Mtxtb018VrsoMeioEntraSrvco m")
public class Mtxtb018VrsoMeioEntraSrvco implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb018VrsoMeioEntraSrvcoPK id;

    @Column(name = "DH_ATUALIZACAO", nullable = false)
    private Date dhAtualizacao;

    @Column(name = "IC_SITUACAO", nullable = false, precision = 1)
    private BigDecimal icSituacao;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "NU_MEIO_ENTRADA_008", referencedColumnName = "NU_MEIO_ENTRADA", nullable = false, insertable = false, updatable = false) })
    private Mtxtb008MeioEntrada mtxtb008MeioEntrada;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "NU_SERVICO_011", referencedColumnName = "NU_SERVICO_001", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "NU_VERSAO_SERVICO_011", referencedColumnName = "NU_VERSAO_SERVICO", nullable = false, insertable = false, updatable = false) })
    private Mtxtb011VersaoServico mtxtb011VersaoServico;


    public Mtxtb018VrsoMeioEntraSrvcoPK getId() {
        return this.id;
    }

    public void setId(Mtxtb018VrsoMeioEntraSrvcoPK id) {
        this.id = id;
    }

    public Date getDhAtualizacao() {
        return this.dhAtualizacao;
    }

    public void setDhAtualizacao(Date dhAtualizacao) {
        this.dhAtualizacao = dhAtualizacao;
    }

    public BigDecimal getIcSituacao() {
        return this.icSituacao;
    }

    public void setIcSituacao(BigDecimal icSituacao) {
        this.icSituacao = icSituacao;
    }

    public Mtxtb008MeioEntrada getMtxtb008MeioEntrada() {
        return this.mtxtb008MeioEntrada;
    }

    public void setMtxtb008MeioEntrada(Mtxtb008MeioEntrada mtxtb008MeioEntrada) {
        this.mtxtb008MeioEntrada = mtxtb008MeioEntrada;
    }

    public Mtxtb011VersaoServico getMtxtb011VersaoServico() {
        return this.mtxtb011VersaoServico;
    }

    public void setMtxtb011VersaoServico(Mtxtb011VersaoServico mtxtb011VersaoServico) {
        this.mtxtb011VersaoServico = mtxtb011VersaoServico;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb018VrsoMeioEntraSrvco [");
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
        if (icSituacao != null) {
            builder.append("icSituacao=");
            builder.append(icSituacao);
            builder.append(", ");
        }
        if (mtxtb008MeioEntrada != null) {
            builder.append("mtxtb009VrsoMeioEntrada=");
            builder.append(mtxtb008MeioEntrada);
            builder.append(", ");
        }
        if (mtxtb011VersaoServico != null) {
            builder.append("mtxtb011VersaoServico=");
            builder.append(mtxtb011VersaoServico);
        }
        builder.append("]");
        return builder.toString();
    }


}
