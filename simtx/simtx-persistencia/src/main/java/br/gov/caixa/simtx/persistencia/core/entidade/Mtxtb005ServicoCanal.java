/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Cacheable
@Table(name = "MTXTB005_SERVICO_CANAL")
@NamedQuery(name = "Mtxtb005ServicoCanal.findAll", query = "SELECT m FROM Mtxtb005ServicoCanal m")
public class Mtxtb005ServicoCanal implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb005ServicoCanalPK id;

    @Column(name = "IC_STCO_SRVCO_CANAL", nullable = false, precision = 1)
    private BigDecimal icStcoSrvcoCanal;

    @ManyToOne
    @JoinColumn(name = "NU_SERVICO_001", nullable = false, insertable = false, updatable = false)
    private Mtxtb001Servico mtxtb001Servico;

    @ManyToOne
    @JoinColumn(name = "NU_CANAL_004", nullable = false, insertable = false, updatable = false)
    private Mtxtb004Canal mtxtb004Canal;

    @OneToMany(mappedBy = "mtxtb005ServicoCanal")
    private List<Mtxtb013PrmtoSrvcoCanal> mtxtb013PrmtoSrvcoCanals;


    public Mtxtb005ServicoCanalPK getId() {
        return this.id;
    }

    public void setId(Mtxtb005ServicoCanalPK id) {
        this.id = id;
    }

    public BigDecimal getIcStcoSrvcoCanal() {
        return this.icStcoSrvcoCanal;
    }

    public void setIcStcoSrvcoCanal(BigDecimal icStcoSrvcoCanal) {
        this.icStcoSrvcoCanal = icStcoSrvcoCanal;
    }

    public Mtxtb001Servico getMtxtb001Servico() {
        return this.mtxtb001Servico;
    }

    public void setMtxtb001Servico(Mtxtb001Servico mtxtb001Servico) {
        this.mtxtb001Servico = mtxtb001Servico;
    }

    public Mtxtb004Canal getMtxtb004Canal() {
        return this.mtxtb004Canal;
    }

    public void setMtxtb004Canal(Mtxtb004Canal mtxtb004Canal) {
        this.mtxtb004Canal = mtxtb004Canal;
    }

    public List<Mtxtb013PrmtoSrvcoCanal> getMtxtb013PrmtoSrvcoCanals() {
        return this.mtxtb013PrmtoSrvcoCanals;
    }

    public void setMtxtb013PrmtoSrvcoCanals(List<Mtxtb013PrmtoSrvcoCanal> mtxtb013PrmtoSrvcoCanals) {
        this.mtxtb013PrmtoSrvcoCanals = mtxtb013PrmtoSrvcoCanals;
    }

    public Mtxtb013PrmtoSrvcoCanal addMtxtb013PrmtoSrvcoCanal(Mtxtb013PrmtoSrvcoCanal mtxtb013PrmtoSrvcoCanal) {
        getMtxtb013PrmtoSrvcoCanals().add(mtxtb013PrmtoSrvcoCanal);
        mtxtb013PrmtoSrvcoCanal.setMtxtb005ServicoCanal(this);

        return mtxtb013PrmtoSrvcoCanal;
    }

    public Mtxtb013PrmtoSrvcoCanal removeMtxtb013PrmtoSrvcoCanal(Mtxtb013PrmtoSrvcoCanal mtxtb013PrmtoSrvcoCanal) {
        getMtxtb013PrmtoSrvcoCanals().remove(mtxtb013PrmtoSrvcoCanal);
        mtxtb013PrmtoSrvcoCanal.setMtxtb005ServicoCanal(null);

        return mtxtb013PrmtoSrvcoCanal;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb005ServicoCanal [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (icStcoSrvcoCanal != null) {
            builder.append("icStcoSrvcoCanal=");
            builder.append(icStcoSrvcoCanal);
            builder.append(", ");
        }
        if (mtxtb001Servico != null) {
            builder.append("mtxtb001Servico=");
            builder.append(mtxtb001Servico);
            builder.append(", ");
        }
        if (mtxtb004Canal != null) {
            builder.append("mtxtb004Canal=");
            builder.append(mtxtb004Canal);
            builder.append(", ");
        }
        if (mtxtb013PrmtoSrvcoCanals != null) {
            builder.append("mtxtb013PrmtoSrvcoCanals=");
            builder.append(mtxtb013PrmtoSrvcoCanals);
        }
        builder.append("]");
        return builder.toString();
    }


}
