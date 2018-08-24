/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtx.persistencia.constante.Constantes;
    

@Entity
@SequenceGenerator(name = "MTXSQ002_TRANSACAO_sequence", sequenceName = "MTXSQ014_NU_NSU_TRANSACAO", allocationSize = 1, initialValue = 1)
@Table(name = "MTXTB014_TRANSACAO")
@NamedQueries({
	@NamedQuery(name = "Mtxtb014Transacao.findAll", query = "SELECT m FROM Mtxtb014Transacao m"),
	@NamedQuery(name = "Mtxtb014Transacao.buscarPorNSUPAI", query = "SELECT m FROM Mtxtb014Transacao m where m.nuNsuTransacaoPai = :nuNsuTransacaoPai"),
	@NamedQuery(name = "Mtxtb014Transacao.buscarTransacaoOrigem", query = "SELECT m FROM Mtxtb014Transacao m where m.nuNsuTransacao = :nuNsuTransacaoPai"),
	@NamedQuery(name = "Mtxtb014Transacao.possuiEnvioCCO", query = "SELECT m FROM Mtxtb014Transacao m where m.icRetorno = 0 and m.dtReferencia <= :dataReferencia"),
	@NamedQuery(name = "Mtxtb014Transacao.buscarParaEnvioCCO", query = "SELECT m FROM Mtxtb014Transacao m where m.icRetorno = 0 and m.dtReferencia <= :dataReferencia"),
	@NamedQuery(name = "Mtxtb014Transacao.buscarInformacoesParaLimpeza", query = "SELECT m FROM Mtxtb014Transacao m where m.icRetorno = 1 and m.icEnvio = 1 and m.dtReferencia <= :dataReferencia ORDER BY m.dtReferencia") })
public class Mtxtb014Transacao implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "MTXSQ002_TRANSACAO_sequence", strategy = GenerationType.SEQUENCE)
    @Column(name = "NU_NSU_TRANSACAO", unique = true, nullable = false, precision = 15)
    private long nuNsuTransacao;

    @Column(name = "NU_NSU_TRANSACAO_ORIGEM", nullable = true, length = 15)
    private long nuNsuTransacaoPai;
    
	@Column(name = "CO_CANAL_ORIGEM", nullable = false, length = 20)
    private String coCanalOrigem;

    @Column(name = "IC_SITUACAO", nullable = false, precision = 1)
    private BigDecimal icSituacao;
    
    @Column(name = "IC_ENVIO", nullable = false, precision = 1)
    private BigDecimal icEnvio;
    
    @Column(name = "IC_RETORNO", nullable = false, precision = 1)
    private BigDecimal icRetorno;
    
    @Column(name = "TS_ATUALIZACAO", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date tsAtualizacao;

    @Column(name = "DT_REFERENCIA", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtReferencia;
    
    @Column(name = "DH_MULTICANAL", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dhMultiCanal;
    
    @Column(name = "DH_TRANSACAO_CANAL", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dhTransacaoCanal;
    
    @Column(name = "DT_CONTABIL", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date dtContabil;
    
    

    @OneToMany(mappedBy = "mtxtb014Transacao", fetch = FetchType.LAZY)
    private List<Mtxtb016IteracaoCanal> mtxtb016IteracaoCanals;

    @OneToMany(mappedBy = "mtxtb014Transacao", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mtxtb017VersaoSrvcoTrnso> mtxtb017VersaoSrvcoTrnsos;
    
    


    public long getNuNsuTransacaoPai() {
		return nuNsuTransacaoPai;
	}

	public void setNuNsuTransacaoPai(long nuNsuTransacaoPai) {
		this.nuNsuTransacaoPai = nuNsuTransacaoPai;
	}
	
    public long getNuNsuTransacao() {
        return this.nuNsuTransacao;
    }

    public void setNuNsuTransacao(long nuNsuTransacao) {
        this.nuNsuTransacao = nuNsuTransacao;
    }

    public String getCoCanalOrigem() {
        return this.coCanalOrigem;
    }

    public void setCoCanalOrigem(String coCanalOrigem) {
        this.coCanalOrigem = coCanalOrigem;
    }

    public BigDecimal getIcSituacao() {
        return this.icSituacao;
    }

    public void setIcSituacao(BigDecimal icSituacao) {
        this.icSituacao = icSituacao;
    }

	public Date getDtReferencia() {
		return dtReferencia;
	}

	public void setDtReferencia(Date dtReferencia) {
		this.dtReferencia = dtReferencia;
	}

	public Date getDhMultiCanal() {
		return dhMultiCanal;
	}

	public void setDhMultiCanal(Date dhMultiCanal) {
		this.dhMultiCanal = dhMultiCanal;
	}

	public Date getDhTransacaoCanal() {
		return dhTransacaoCanal;
	}

	public void setDhTransacaoCanal(Date dhTransacaoCanal) {
		this.dhTransacaoCanal = dhTransacaoCanal;
	}

	public Date getDtContabil() {
		return dtContabil;
	}

	public void setDtContabil(Date dtContabil) {
		this.dtContabil = dtContabil;
	}

	public List<Mtxtb016IteracaoCanal> getMtxtb016IteracaoCanals() {
        return this.mtxtb016IteracaoCanals;
    }

    public void setMtxtb016IteracaoCanals(List<Mtxtb016IteracaoCanal> mtxtb016IteracaoCanals) {
        this.mtxtb016IteracaoCanals = mtxtb016IteracaoCanals;
    }

    public Mtxtb016IteracaoCanal addMtxtb016IteracaoCanal(Mtxtb016IteracaoCanal mtxtb016IteracaoCanal) {
        getMtxtb016IteracaoCanals().add(mtxtb016IteracaoCanal);
        mtxtb016IteracaoCanal.setMtxtb014Transacao(this);

        return mtxtb016IteracaoCanal;
    }

    public Mtxtb016IteracaoCanal removeMtxtb016IteracaoCanal(Mtxtb016IteracaoCanal mtxtb016IteracaoCanal) {
        getMtxtb016IteracaoCanals().remove(mtxtb016IteracaoCanal);
        mtxtb016IteracaoCanal.setMtxtb014Transacao(null);

        return mtxtb016IteracaoCanal;
    }

    public List<Mtxtb017VersaoSrvcoTrnso> getMtxtb017VersaoSrvcoTrnsos() {
        return this.mtxtb017VersaoSrvcoTrnsos;
    }

    public void setMtxtb017VersaoSrvcoTrnsos(List<Mtxtb017VersaoSrvcoTrnso> mtxtb017VersaoSrvcoTrnsos) {
        this.mtxtb017VersaoSrvcoTrnsos = mtxtb017VersaoSrvcoTrnsos;
    }

    public Mtxtb017VersaoSrvcoTrnso addMtxtb017VersaoSrvcoTrnso(Mtxtb017VersaoSrvcoTrnso mtxtb017VersaoSrvcoTrnso) {
        getMtxtb017VersaoSrvcoTrnsos().add(mtxtb017VersaoSrvcoTrnso);
        mtxtb017VersaoSrvcoTrnso.setMtxtb014Transacao(this);

        return mtxtb017VersaoSrvcoTrnso;
    }

    public Mtxtb017VersaoSrvcoTrnso removeMtxtb017VersaoSrvcoTrnso(Mtxtb017VersaoSrvcoTrnso mtxtb017VersaoSrvcoTrnso) {
        getMtxtb017VersaoSrvcoTrnsos().remove(mtxtb017VersaoSrvcoTrnso);
        mtxtb017VersaoSrvcoTrnso.setMtxtb014Transacao(null);

        return mtxtb017VersaoSrvcoTrnso;
    }

    public BigDecimal getIcEnvio() {
		return icEnvio;
	}

	public void setIcEnvio(BigDecimal icEnvio) {
		this.icEnvio = icEnvio;
	}

	public BigDecimal getIcRetorno() {
		return icRetorno;
	}

	public void setIcRetorno(BigDecimal icRetorno) {
		this.icRetorno = icRetorno;
	}

	public Date getTsAtualizacao() {
		return tsAtualizacao;
	}

	public void setTsAtualizacao(Date tsAtualizacao) {
		this.tsAtualizacao = tsAtualizacao;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb014Transacao [nuNsuTransacao=");
        builder.append(nuNsuTransacao);
        builder.append(", ");
        if (coCanalOrigem != null) {
            builder.append("coCanalOrigem=");
            builder.append(coCanalOrigem);
            builder.append(", ");
        }
        if (icSituacao != null) {
            builder.append("icSituacao=");
            builder.append(icSituacao);
            builder.append(", ");
        }
        if (mtxtb016IteracaoCanals != null) {
            builder.append("mtxtb016IteracaoCanals=");
            builder.append(mtxtb016IteracaoCanals);
            builder.append(", ");
        }
        if (mtxtb017VersaoSrvcoTrnsos != null) {
            builder.append("mtxtb017VersaoSrvcoTrnsos=");
            builder.append(mtxtb017VersaoSrvcoTrnsos);
        }
        builder.append("]");
        return builder.toString();
    }

	public void finalizaSituacao() {
		this.icSituacao = Constantes.IC_SERVICO_FINALIZADO;
	}
	
	public void naoEfetivaSituacao() {
		this.icSituacao = Constantes.IC_SERVICO_NEGADO;
	}
	
	public void negarSituacao() {
		this.icSituacao = Constantes.IC_SERVICO_NEGADO;
	}

}
