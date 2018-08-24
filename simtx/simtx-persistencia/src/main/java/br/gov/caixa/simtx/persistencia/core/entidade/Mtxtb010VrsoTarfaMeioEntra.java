/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Cacheable
@Table(name = "MTXTB010_VRSO_TARFA_MEIO_ENTRA")
@NamedQueries({
    @NamedQuery(name = "Mtxtb010VrsoTarfaMeioEntra.findAll", query = "SELECT m FROM Mtxtb010VrsoTarfaMeioEntra m"),
    @NamedQuery(name = "Mtxtb010VrsoTarfaMeioEntra.buscarPorMeioEntrada", query = "Select m FROM Mtxtb010VrsoTarfaMeioEntra m join m.mtxtb012VersaoTarefa vt where m.id.nuMeioEntrada008 = :nuMeioEntrada") })
public class Mtxtb010VrsoTarfaMeioEntra implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb010VrsoTarfaMeioEntraPK id;

    @Column(name = "IC_SITUACAO", nullable = false, precision = 1)
    private BigDecimal icSituacao;

    @ManyToOne
	@JoinColumns({
			@JoinColumn(name = "NU_MEIO_ENTRADA_008", referencedColumnName = "NU_MEIO_ENTRADA", nullable = false, insertable = false, updatable = false) })
    private Mtxtb008MeioEntrada mtxtb008MeioEntrada;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "NU_TAREFA_012", referencedColumnName = "NU_TAREFA_002", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "NU_VERSAO_TAREFA_012", referencedColumnName = "NU_VERSAO_TAREFA", nullable = false, insertable = false, updatable = false) })
    private Mtxtb012VersaoTarefa mtxtb012VersaoTarefa;


    public Mtxtb010VrsoTarfaMeioEntraPK getId() {
        return this.id;
    }

    public void setId(Mtxtb010VrsoTarfaMeioEntraPK id) {
        this.id = id;
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

    public Mtxtb012VersaoTarefa getMtxtb012VersaoTarefa() {
        return this.mtxtb012VersaoTarefa;
    }

    public void setMtxtb012VersaoTarefa(Mtxtb012VersaoTarefa mtxtb012VersaoTarefa) {
        this.mtxtb012VersaoTarefa = mtxtb012VersaoTarefa;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb010VrsoTarfaMeioEntra [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
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
        if (mtxtb012VersaoTarefa != null) {
            builder.append("mtxtb012VersaoTarefa=");
            builder.append(mtxtb012VersaoTarefa);
        }
        builder.append("]");
        return builder.toString();
    }


}
