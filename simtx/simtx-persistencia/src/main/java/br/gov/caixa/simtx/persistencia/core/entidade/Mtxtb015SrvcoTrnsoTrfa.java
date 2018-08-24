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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "MTXTB015_SRVCO_TRNSO_TARFA")
@NamedQueries({ @NamedQuery(name = "Mtxtb015SrvcoTrnsoTrfa.findAll", query = "SELECT m FROM Mtxtb015SrvcoTrnsoTrfa m"),
        @NamedQuery(name = "Mtxtb015SrvcoTrnsoTrfa.buscarPorFiltro",
                query = "SELECT m FROM Mtxtb015SrvcoTrnsoTrfa m where m.id.nuServico017 = :nuServico and m.id.nuVersaoServico017= :nuVersaoServico and m.id.nuNsuTransacao017  = :nuNsuTransacao and m.id.nuTarefa012 = :nuTarefa and m.id.nuVersaoTarefa012 = :nuVersaoTarefa"),
        @NamedQuery(name = "Mtxtb015SrvcoTrnsoTrfa.buscarPorFiltroCorp",
                query = "SELECT m FROM Mtxtb015SrvcoTrnsoTrfa m where m.id.nuNsuTransacao017 = :nuNsuTransacao and m.nsuCorp = :nsuCorp"),
        @NamedQuery(name = "Mtxtb015SrvcoTrnsoTrfa.buscarTarefasDoServico",
                query = "SELECT m FROM Mtxtb015SrvcoTrnsoTrfa m where m.id.nuNsuTransacao017  = :nuNsuTransacao"), })
public class Mtxtb015SrvcoTrnsoTrfa implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb015SrvcoTrnsoTrfaPK id;

    @Column(name = "DE_XML_REQUISICAO", nullable = false, length = 4000, columnDefinition = "clob")
    private String deXmlRequisicao;

    @Column(name = "NU_NSU_TRANSACAO_CRPRO", nullable = true, length = 20)
    private Long nsuCorp;

    @Column(name = "DE_XML_RESPOSTA", nullable = false, length = 4000, columnDefinition = "clob")
    private String deXmlResposta;

    @Column(name = "TS_EXECUCAO_TRANSACAO", nullable = false)
    private Timestamp tsExecucaoTransacao;

    @Column(name = "DT_REFERENCIA", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dtReferencia;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "NU_TAREFA_012", referencedColumnName = "NU_TAREFA_002", nullable = false,
                    insertable = false, updatable = false),
            @JoinColumn(name = "NU_VERSAO_TAREFA_012", referencedColumnName = "NU_VERSAO_TAREFA", nullable = false,
                    insertable = false, updatable = false) })
    private Mtxtb012VersaoTarefa mtxtb012VersaoTarefa;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "NU_NSU_TRANSACAO_017", referencedColumnName = "NU_NSU_TRANSACAO_014", nullable = false,
                    insertable = false, updatable = false),
            @JoinColumn(name = "NU_SERVICO_017", referencedColumnName = "NU_SERVICO_011", nullable = false,
                    insertable = false, updatable = false),
            @JoinColumn(name = "NU_VERSAO_SERVICO_017", referencedColumnName = "NU_VERSAO_SERVICO_011",
                    nullable = false, insertable = false, updatable = false) })
    private Mtxtb017VersaoSrvcoTrnso mtxtb017VersaoSrvcoTrnso;


    public Mtxtb015SrvcoTrnsoTrfaPK getId() {
        return this.id;
    }

    public void setNsuCorp(Long id) {
        this.nsuCorp = id;
    }

    public Long getNsuCorp() {
        return this.nsuCorp;
    }

    public void setId(Mtxtb015SrvcoTrnsoTrfaPK id) {
        this.id = id;
    }

    public String getDeXmlRequisicao() {
        return this.deXmlRequisicao;
    }

    public void setDeXmlRequisicao(String deXmlRequisicao) {
        this.deXmlRequisicao = deXmlRequisicao;
    }

    public String getDeXmlResposta() {
        return this.deXmlResposta;
    }

    public void setDeXmlResposta(String deXmlResposta) {
        this.deXmlResposta = deXmlResposta;
    }

    public Timestamp getTsExecucaoTransacao() {
        return this.tsExecucaoTransacao;
    }

    public void setTsExecucaoTransacao(Timestamp tsExecucaoTransacao) {
        this.tsExecucaoTransacao = tsExecucaoTransacao;
    }

    public Date getDtReferencia() {
        return dtReferencia;
    }

    public void setDtReferencia(Date dtReferencia) {
        this.dtReferencia = dtReferencia;
    }

    public Mtxtb012VersaoTarefa getMtxtb012VersaoTarefa() {
        return this.mtxtb012VersaoTarefa;
    }

    public void setMtxtb012VersaoTarefa(Mtxtb012VersaoTarefa mtxtb012VersaoTarefa) {
        this.mtxtb012VersaoTarefa = mtxtb012VersaoTarefa;
    }

    public Mtxtb017VersaoSrvcoTrnso getMtxtb017VersaoSrvcoTrnso() {
        return this.mtxtb017VersaoSrvcoTrnso;
    }

    public void setMtxtb017VersaoSrvcoTrnso(Mtxtb017VersaoSrvcoTrnso mtxtb017VersaoSrvcoTrnso) {
        this.mtxtb017VersaoSrvcoTrnso = mtxtb017VersaoSrvcoTrnso;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb015SrvcoTrnsoTrfa [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (deXmlRequisicao != null) {
            builder.append("deXmlRequisicao=");
            builder.append(deXmlRequisicao);
            builder.append(", ");
        }
        if (deXmlResposta != null) {
            builder.append("deXmlResposta=");
            builder.append(deXmlResposta);
            builder.append(", ");
        }
        if (tsExecucaoTransacao != null) {
            builder.append("tsExecucaoTransacao=");
            builder.append(tsExecucaoTransacao);
            builder.append(", ");
        }
        if (mtxtb012VersaoTarefa != null) {
            builder.append("mtxtb012VersaoTarefa=");
            builder.append(mtxtb012VersaoTarefa);
            builder.append(", ");
        }
        if (mtxtb017VersaoSrvcoTrnso != null) {
            builder.append("mtxtb017VersaoSrvcoTrnso=");
            builder.append(mtxtb017VersaoSrvcoTrnso);
        }
        builder.append("]");
        return builder.toString();
    }

}
