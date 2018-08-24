/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "MTXTB017_VERSAO_SRVCO_TRNSO")
@NamedQueries({
	@NamedQuery(name = "Mtxtb017VersaoSrvcoTrnso.findAll", query = "SELECT m FROM Mtxtb017VersaoSrvcoTrnso m"),
	@NamedQuery(name = "Mtxtb017VersaoSrvcoTrnso.buscarPorNSU", query = "SELECT m FROM Mtxtb017VersaoSrvcoTrnso m join m.mtxtb011VersaoServico vs where m.id.nuNsuTransacao014 = :nuNsuTransacao") })
public class Mtxtb017VersaoSrvcoTrnso implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb017VersaoSrvcoTrnsoPK id;

    @Column(name = "TS_SOLICITACAO", nullable = false)
    private Timestamp tsSolicitacao;

    @Column(name = "DT_REFERENCIA", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dtReferencia;

    @OneToMany(mappedBy = "mtxtb017VersaoSrvcoTrnso",  cascade = CascadeType.ALL)
    private List<Mtxtb015SrvcoTrnsoTrfa> mtxtb015VrsoSrvcoTrnsoTrfas;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "NU_SERVICO_011", referencedColumnName = "NU_SERVICO_001", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "NU_VERSAO_SERVICO_011", referencedColumnName = "NU_VERSAO_SERVICO", nullable = false, insertable = false, updatable = false) })
    private Mtxtb011VersaoServico mtxtb011VersaoServico;

    @ManyToOne
    @JoinColumn(name = "NU_NSU_TRANSACAO_014", nullable = false, insertable = false, updatable = false)
    private Mtxtb014Transacao mtxtb014Transacao;


    public Mtxtb017VersaoSrvcoTrnsoPK getId() {
        return this.id;
    }

    public void setId(Mtxtb017VersaoSrvcoTrnsoPK id) {
        this.id = id;
    }

    public Timestamp getTsSolicitacao() {
        return this.tsSolicitacao;
    }

    public void setTsSolicitacao(Timestamp tsSolicitacao) {
        this.tsSolicitacao = tsSolicitacao;
    }

	public Date getDtReferencia() {
		return dtReferencia;
	}

	public void setDtReferencia(Date dtReferencia) {
		this.dtReferencia = dtReferencia;
	}

	public List<Mtxtb015SrvcoTrnsoTrfa> getMtxtb015VrsoSrvcoTrnsoTrfas() {
        return this.mtxtb015VrsoSrvcoTrnsoTrfas;
    }

    public void setMtxtb015VrsoSrvcoTrnsoTrfas(List<Mtxtb015SrvcoTrnsoTrfa> mtxtb015VrsoSrvcoTrnsoTrfas) {
        this.mtxtb015VrsoSrvcoTrnsoTrfas = mtxtb015VrsoSrvcoTrnsoTrfas;
    }

    public Mtxtb015SrvcoTrnsoTrfa addMtxtb015VrsoSrvcoTrnsoTrfa(
        Mtxtb015SrvcoTrnsoTrfa mtxtb015VrsoSrvcoTrnsoTrfa) {
        getMtxtb015VrsoSrvcoTrnsoTrfas().add(mtxtb015VrsoSrvcoTrnsoTrfa);
        mtxtb015VrsoSrvcoTrnsoTrfa.setMtxtb017VersaoSrvcoTrnso(this);

        return mtxtb015VrsoSrvcoTrnsoTrfa;
    }

    public Mtxtb015SrvcoTrnsoTrfa removeMtxtb015VrsoSrvcoTrnsoTrfa(
        Mtxtb015SrvcoTrnsoTrfa mtxtb015VrsoSrvcoTrnsoTrfa) {
        getMtxtb015VrsoSrvcoTrnsoTrfas().remove(mtxtb015VrsoSrvcoTrnsoTrfa);
        mtxtb015VrsoSrvcoTrnsoTrfa.setMtxtb017VersaoSrvcoTrnso(null);

        return mtxtb015VrsoSrvcoTrnsoTrfa;
    }

    public Mtxtb011VersaoServico getMtxtb011VersaoServico() {
        return this.mtxtb011VersaoServico;
    }

    public void setMtxtb011VersaoServico(Mtxtb011VersaoServico mtxtb011VersaoServico) {
        this.mtxtb011VersaoServico = mtxtb011VersaoServico;
    }

    public Mtxtb014Transacao getMtxtb014Transacao() {
        return this.mtxtb014Transacao;
    }

    public void setMtxtb014Transacao(Mtxtb014Transacao mtxtb014Transacao) {
        this.mtxtb014Transacao = mtxtb014Transacao;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb017VersaoSrvcoTrnso [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (tsSolicitacao != null) {
            builder.append("tsSolicitacao=");
            builder.append(tsSolicitacao);
            builder.append(", ");
        }
        if (mtxtb015VrsoSrvcoTrnsoTrfas != null) {
            builder.append("mtxtb015VrsoSrvcoTrnsoTrfas=");
            builder.append(mtxtb015VrsoSrvcoTrnsoTrfas);
            builder.append(", ");
        }
        if (mtxtb011VersaoServico != null) {
            builder.append("mtxtb011VersaoServico=");
            builder.append(mtxtb011VersaoServico);
            builder.append(", ");
        }
        if (mtxtb014Transacao != null) {
            builder.append("mtxtb014Transacao=");
            builder.append(mtxtb014Transacao);
        }
        builder.append("]");
        return builder.toString();
    }


}
