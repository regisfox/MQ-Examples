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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "MTXTB009_VRSO_MEIO_ENTRADA")
@NamedQuery(name = "Mtxtb009VrsoMeioEntrada.findAll", query = "SELECT m FROM Mtxtb009VrsoMeioEntrada m")
public class Mtxtb009VrsoMeioEntrada implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb009VrsoMeioEntradaPK id;

    @Column(name = "DE_XSD", nullable = false, length = 20)
    private String deXsd;

    @Column(name = "DE_XSLT", nullable = false, length = 20)
    private String deXslt;

    @Column(name = "IC_SITUACAO", nullable = false, precision = 1)
    private BigDecimal icSituacao;

    @ManyToOne
    @JoinColumn(name = "NU_MEIO_ENTRADA_008", nullable = false, insertable = false, updatable = false)
    private Mtxtb008MeioEntrada mtxtb008MeioEntrada;

    @OneToMany(mappedBy = "mtxtb009VrsoMeioEntrada")
    private List<Mtxtb010VrsoTarfaMeioEntra> mtxtb010VrsoTarfaMeioEntras;

    @OneToMany(mappedBy = "mtxtb009VrsoMeioEntrada")
    private List<Mtxtb018VrsoMeioEntraSrvco> mtxtb018VrsoMeioEntraSrvcos;


    public Mtxtb009VrsoMeioEntradaPK getId() {
        return this.id;
    }

    public void setId(Mtxtb009VrsoMeioEntradaPK id) {
        this.id = id;
    }

    public String getDeXsd() {
        return this.deXsd;
    }

    public void setDeXsd(String deXsd) {
        this.deXsd = deXsd;
    }

    public String getDeXslt() {
        return this.deXslt;
    }

    public void setDeXslt(String deXslt) {
        this.deXslt = deXslt;
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

    public List<Mtxtb010VrsoTarfaMeioEntra> getMtxtb010VrsoTarfaMeioEntras() {
        return this.mtxtb010VrsoTarfaMeioEntras;
    }

    public void setMtxtb010VrsoTarfaMeioEntras(List<Mtxtb010VrsoTarfaMeioEntra> mtxtb010VrsoTarfaMeioEntras) {
        this.mtxtb010VrsoTarfaMeioEntras = mtxtb010VrsoTarfaMeioEntras;
    }

    public List<Mtxtb018VrsoMeioEntraSrvco> getMtxtb018VrsoMeioEntraSrvcos() {
        return this.mtxtb018VrsoMeioEntraSrvcos;
    }

    public void setMtxtb018VrsoMeioEntraSrvcos(List<Mtxtb018VrsoMeioEntraSrvco> mtxtb018VrsoMeioEntraSrvcos) {
        this.mtxtb018VrsoMeioEntraSrvcos = mtxtb018VrsoMeioEntraSrvcos;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb009VrsoMeioEntrada [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (deXsd != null) {
            builder.append("deXsd=");
            builder.append(deXsd);
            builder.append(", ");
        }
        if (deXslt != null) {
            builder.append("deXslt=");
            builder.append(deXslt);
            builder.append(", ");
        }
        if (icSituacao != null) {
            builder.append("icSituacao=");
            builder.append(icSituacao);
            builder.append(", ");
        }
        if (mtxtb008MeioEntrada != null) {
            builder.append("mtxtb008MeioEntrada=");
            builder.append(mtxtb008MeioEntrada);
            builder.append(", ");
        }
        if (mtxtb010VrsoTarfaMeioEntras != null) {
            builder.append("mtxtb010VrsoTarfaMeioEntras=");
            builder.append(mtxtb010VrsoTarfaMeioEntras);
            builder.append(", ");
        }
        if (mtxtb018VrsoMeioEntraSrvcos != null) {
            builder.append("mtxtb018VrsoMeioEntraSrvcos=");
            builder.append(mtxtb018VrsoMeioEntraSrvcos);
        }
        builder.append("]");
        return builder.toString();
    }

}
