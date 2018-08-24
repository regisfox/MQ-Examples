/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the MTXTB004_CANAL database table.
 * 
 */
@Entity
@Cacheable
@Table(name = "MTXTB004_CANAL")
@NamedQueries({
	@NamedQuery(name = "Mtxtb004Canal.findAll", query = "SELECT m FROM Mtxtb004Canal m"),
	@NamedQuery(name = "Mtxtb004Canal.buscarPorSigla", query =  "SELECT c FROM Mtxtb004Canal c where c.sigla = :sigla"),
	@NamedQuery(name = "Mtxtb004Canal.buscarPorSituacao", query =  "SELECT c FROM Mtxtb004Canal c where c.icSituacaoCanal = :situacao and c.nuCanal > 3"),
	@NamedQuery(name = "Mtxtb004Canal.buscarTodos", query =  "SELECT c FROM Mtxtb004Canal c where c.nuCanal > 3 and c.nuCanal <> 114 order by c.noCanal")
})
public class Mtxtb004Canal implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NU_CANAL", unique = true, nullable = false, precision = 3)
    private long nuCanal;

    @Column(name = "DH_ATUALIZACAO", nullable = false)
    private Date dhAtualizacao;

    @Column(name = "IC_SITUACAO_CANAL", nullable = false, precision = 1)
    private BigDecimal icSituacaoCanal;

    @Column(name = "NO_CANAL", nullable = false, length = 50)
    private String noCanal;

    // bi-directional many-to-many association to Mtxtb003ServicoTarefa
    @ManyToMany
    @JoinTable(name = "MTXTB020_SRVCO_TARFA_CANAL", joinColumns = {
        @JoinColumn(name = "NU_CANAL_004", nullable = false) }, inverseJoinColumns = {
            @JoinColumn(name = "NU_SERVICO_003", referencedColumnName = "NU_SERVICO_011", nullable = false),
            @JoinColumn(name = "NU_TAREFA_003", referencedColumnName = "NU_TAREFA_012", nullable = false),
            @JoinColumn(name = "NU_VERSAO_SERVICO_003", referencedColumnName = "NU_VERSAO_SERVICO_011", nullable = false),
            @JoinColumn(name = "NU_VERSAO_TAREFA_003", referencedColumnName = "NU_VERSAO_TAREFA_012", nullable = false) })
    private List<Mtxtb003ServicoTarefa> mtxtb003ServicoTarefas;

    // bi-directional many-to-one association to Mtxtb005ServicoCanal
    @OneToMany(mappedBy = "mtxtb004Canal")
    private List<Mtxtb005ServicoCanal> mtxtb005ServicoCanals;

    // bi-directional many-to-one association to Mtxtb016IteracaoCanal
    @OneToMany(mappedBy = "mtxtb004Canal")
    private List<Mtxtb016IteracaoCanal> mtxtb016IteracaoCanals;

    @Column(name = "NO_FILA_RQSCO_RSLCO_PNDNA")
    private String nomFilaReqResolvePendencia;

    @Column(name = "NO_FILA_RSPSA_RSLCO_PNDNA")
    private String nomFilaRspResolvePendencia;

    @Column(name = "QT_SGNDO_LMTE_RQSCO_RSLCO")
    private BigDecimal numTimeOutReqResolvePend;

    @Column(name = "QT_SGNDO_LMTE_RSPSA_RSLCO")
    private BigDecimal numTimeOutRespResolvePend;
    
    @Column(name = "NO_FILA_RESPOSTA_CANAL")
    private String noFilaRspCanal;

    @Column(name = "NO_CONEXAO_CANAL")
    private String noConexaoCanal;
    
    @Column(name = "SG_CANAL")
    private String sigla;
    
    @Column(name = "NU_REDE_TRANSMISSORA")
    private Integer nuRedeTransmissora;
    
    @Column(name = "NU_SEGMENTO")
    private Integer nuSegmento;
    

    public Mtxtb004Canal() {
    }

    public Mtxtb004Canal(Long nuCanal) {
    	this.nuCanal = nuCanal;
    }

    public long getNuCanal() {
        return this.nuCanal;
    }

    public void setNuCanal(long nuCanal) {
        this.nuCanal = nuCanal;
    }

    public Date getDhAtualizacao() {
        return this.dhAtualizacao;
    }

    public void setDhAtualizacao(Date dhAtualizacao) {
        this.dhAtualizacao = dhAtualizacao;
    }

    public BigDecimal getIcSituacaoCanal() {
        return this.icSituacaoCanal;
    }

    public void setIcSituacaoCanal(BigDecimal icSituacaoCanal) {
        this.icSituacaoCanal = icSituacaoCanal;
    }

    public String getNoCanal() {
        return this.noCanal;
    }

    public void setNoCanal(String noCanal) {
        this.noCanal = noCanal;
    }

    public List<Mtxtb003ServicoTarefa> getMtxtb003ServicoTarefas() {
        return this.mtxtb003ServicoTarefas;
    }

    public void setMtxtb003ServicoTarefas(List<Mtxtb003ServicoTarefa> mtxtb003ServicoTarefas) {
        this.mtxtb003ServicoTarefas = mtxtb003ServicoTarefas;
    }

    public List<Mtxtb005ServicoCanal> getMtxtb005ServicoCanals() {
        return this.mtxtb005ServicoCanals;
    }

    public void setMtxtb005ServicoCanals(List<Mtxtb005ServicoCanal> mtxtb005ServicoCanals) {
        this.mtxtb005ServicoCanals = mtxtb005ServicoCanals;
    }

    public Mtxtb005ServicoCanal addMtxtb005ServicoCanal(Mtxtb005ServicoCanal mtxtb005ServicoCanal) {
        getMtxtb005ServicoCanals().add(mtxtb005ServicoCanal);
        mtxtb005ServicoCanal.setMtxtb004Canal(this);

        return mtxtb005ServicoCanal;
    }

    public Mtxtb005ServicoCanal removeMtxtb005ServicoCanal(Mtxtb005ServicoCanal mtxtb005ServicoCanal) {
        getMtxtb005ServicoCanals().remove(mtxtb005ServicoCanal);
        mtxtb005ServicoCanal.setMtxtb004Canal(null);

        return mtxtb005ServicoCanal;
    }

    public List<Mtxtb016IteracaoCanal> getMtxtb016IteracaoCanals() {
        return this.mtxtb016IteracaoCanals;
    }

    public void setMtxtb016IteracaoCanals(List<Mtxtb016IteracaoCanal> mtxtb016IteracaoCanals) {
        this.mtxtb016IteracaoCanals = mtxtb016IteracaoCanals;
    }

    public Mtxtb016IteracaoCanal addMtxtb016IteracaoCanal(Mtxtb016IteracaoCanal mtxtb016IteracaoCanal) {
        getMtxtb016IteracaoCanals().add(mtxtb016IteracaoCanal);
        mtxtb016IteracaoCanal.setMtxtb004Canal(this);

        return mtxtb016IteracaoCanal;
    }

    public Mtxtb016IteracaoCanal removeMtxtb016IteracaoCanal(Mtxtb016IteracaoCanal mtxtb016IteracaoCanal) {
        getMtxtb016IteracaoCanals().remove(mtxtb016IteracaoCanal);
        mtxtb016IteracaoCanal.setMtxtb004Canal(null);

        return mtxtb016IteracaoCanal;
    }

    public String getNomFilaReqResolvePendencia() {
        return nomFilaReqResolvePendencia;
    }

    public void setNomFilaReqResolvePendencia(String nomFilaReqResolvePendencia) {
        this.nomFilaReqResolvePendencia = nomFilaReqResolvePendencia;
    }

    public String getNomFilaRspResolvePendencia() {
        return nomFilaRspResolvePendencia;
    }

    public void setNomFilaRspResolvePendencia(String nomFilaRspResolvePendencia) {
        this.nomFilaRspResolvePendencia = nomFilaRspResolvePendencia;
    }

    public BigDecimal getNumTimeOutReqResolvePend() {
        return numTimeOutReqResolvePend;
    }

    public void setNumTimeOutReqResolvePend(BigDecimal numTimeOutReqResolvePend) {
        this.numTimeOutReqResolvePend = numTimeOutReqResolvePend;
    }

    public BigDecimal getNumTimeOutRespResolvePend() {
        return numTimeOutRespResolvePend;
    }

    public void setNumTimeOutRespResolvePend(BigDecimal numTimeOutRespResolvePend) {
        this.numTimeOutRespResolvePend = numTimeOutRespResolvePend;
    }

    public String getNoFilaRspCanal() {
		return noFilaRspCanal;
	}

	public void setNoFilaRspCanal(String noFilaRspCanal) {
		this.noFilaRspCanal = noFilaRspCanal;
	}

	public String getNoConexaoCanal() {
		return noConexaoCanal;
	}

	public void setNoConexaoCanal(String noConexaoCanal) {
		this.noConexaoCanal = noConexaoCanal;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Integer getNuRedeTransmissora() {
		return nuRedeTransmissora;
	}

	public void setNuRedeTransmissora(Integer nuRedeTransmissora) {
		this.nuRedeTransmissora = nuRedeTransmissora;
	}

	public Integer getNuSegmento() {
		return nuSegmento;
	}

	public void setNuSegmento(Integer nuSegmento) {
		this.nuSegmento = nuSegmento;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb004Canal [nuCanal=");
        builder.append(nuCanal);
        builder.append(", ");
        if (dhAtualizacao != null) {
            builder.append("dhAtualizacao=");
            builder.append(dhAtualizacao);
            builder.append(", ");
        }
        if (icSituacaoCanal != null) {
            builder.append("icSituacaoCanal=");
            builder.append(icSituacaoCanal);
            builder.append(", ");
        }
        if (noCanal != null) {
            builder.append("noCanal=");
            builder.append(noCanal);
            builder.append(", ");
        }
        if (mtxtb003ServicoTarefas != null) {
            builder.append("mtxtb003ServicoTarefas=");
            builder.append(mtxtb003ServicoTarefas);
            builder.append(", ");
        }
        if (mtxtb005ServicoCanals != null) {
            builder.append("mtxtb005ServicoCanals=");
            builder.append(mtxtb005ServicoCanals);
            builder.append(", ");
        }
        if (mtxtb016IteracaoCanals != null) {
            builder.append("mtxtb016IteracaoCanals=");
            builder.append(mtxtb016IteracaoCanals);
            builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

}
