/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the Mtxtb016IteracaoCanal database table.
 * 
 */
@Entity
@Table(name = "MTXTB016_ITERACAO_CANAL")
@NamedQueries({ @NamedQuery(name = "Mtxtb016IteracaoCanal.findAll", query = "SELECT m FROM Mtxtb016IteracaoCanal m"),
    @NamedQuery(name = "Mtxtb016IteracaoCanal.buscarMaxPK", query = "SELECT max(m.id.nuIteracaoCanal) FROM Mtxtb016IteracaoCanal m")})
public class Mtxtb016IteracaoCanal implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb016IteracaoCanalPK id;

    @Lob
    @Column(name = "DE_RECEBIMENTO", nullable = false)
    private String deRecebimento;

    @Lob
    @Column(name = "DE_RETORNO", nullable = false)
    private String deRetorno;

    @Column(name = "TS_RECEBIMENTO_SOLICITACAO", nullable = false)
    private Timestamp tsRecebimentoSolicitacao;

    @Column(name = "TS_RETORNO_SOLICITACAO", nullable = false)
    private Timestamp tsRetornoSolicitacao;
    
    @Column(name = "CO_TERMINAL", nullable = false, length = 15)
    private String codTerminal;

    @Column(name = "DT_REFERENCIA", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dtReferencia;

    // bi-directional many-to-one association to Mtxtb004Canal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NU_CANAL_004", nullable = false)
    private Mtxtb004Canal mtxtb004Canal;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NU_NSU_TRANSACAO_014", nullable = false, insertable = false, updatable = false)
    private Mtxtb035TransacaoConta mtxtb035TransacaoConta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "NU_NSU_TRANSACAO_014", nullable = false, insertable = false, updatable = false)
    private Mtxtb014Transacao mtxtb014Transacao;


    public Mtxtb016IteracaoCanalPK getId() {
        return this.id;
    }

    public void setId(Mtxtb016IteracaoCanalPK id) {
        this.id = id;
    }

    public String getDeRecebimento() {
        return this.deRecebimento;
    }

    public void setDeRecebimento(String deRecebimento) {
        this.deRecebimento = deRecebimento;
    }

    public String getDeRetorno() {
        return this.deRetorno;
    }

    public void setDeRetorno(String deRetorno) {
        this.deRetorno = deRetorno;
    }

    public Timestamp getTsRecebimentoSolicitacao() {
        return this.tsRecebimentoSolicitacao;
    }

    public String getCodTerminal() {
		return codTerminal;
	}

	public void setCodTerminal(String codTerminal) {
		this.codTerminal = codTerminal;
	}

	public void setTsRecebimentoSolicitacao(Timestamp tsRecebimentoSolicitacao) {
        this.tsRecebimentoSolicitacao = tsRecebimentoSolicitacao;
    }

    public Timestamp getTsRetornoSolicitacao() {
        return this.tsRetornoSolicitacao;
    }

    public void setTsRetornoSolicitacao(Timestamp tsRetornoSolicitacao) {
        this.tsRetornoSolicitacao = tsRetornoSolicitacao;
    }

	public Date getDtReferencia() {
		return dtReferencia;
	}

	public void setDtReferencia(Date dtReferencia) {
		this.dtReferencia = dtReferencia;
	}

	public Mtxtb004Canal getMtxtb004Canal() {
        return this.mtxtb004Canal;
    }

    public void setMtxtb004Canal(Mtxtb004Canal mtxtb004Canal) {
        this.mtxtb004Canal = mtxtb004Canal;
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
        builder.append("Mtxtb016IteracaoCanal [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (deRecebimento != null) {
            builder.append("deRecebimento=");
            builder.append(deRecebimento);
            builder.append(", ");
        }
        if (deRetorno != null) {
            builder.append("deRetorno=");
            builder.append(deRetorno);
            builder.append(", ");
        }
        if (tsRecebimentoSolicitacao != null) {
            builder.append("tsRecebimentoSolicitacao=");
            builder.append(tsRecebimentoSolicitacao);
            builder.append(", ");
        }
        if (tsRetornoSolicitacao != null) {
            builder.append("tsRetornoSolicitacao=");
            builder.append(tsRetornoSolicitacao);
            builder.append(", ");
        }
        if (mtxtb004Canal != null) {
            builder.append("mtxtb004Canal=");
            builder.append(mtxtb004Canal);
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
