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

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the MTXTB012_VERSAO_TAREFA database table.
 * 
 */
@Entity
//@Cacheable
@Table(name = "MTXTB012_VERSAO_TAREFA")
@NamedQuery(name = "Mtxtb012VersaoTarefa.findAll", query = "SELECT m FROM Mtxtb012VersaoTarefa m")
public class Mtxtb012VersaoTarefa implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb012VersaoTarefaPK id;

    @Column(name = "DE_XSD_REQUISICAO", nullable = false, length = 100)
    private String deXsdRequisicao;

    @Column(name = "DE_XSD_RESPOSTA", nullable = false, length = 100)
    private String deXsdResposta;

    @Column(name = "DE_XSLT_REQUISICAO", nullable = false, length = 100)
    private String deXsltRequisicao;

    @Column(name = "DE_XSLT_RESPOSTA", nullable = false, length = 100)
    private String deXsltResposta;

    @Column(name = "DH_ATUALIZACAO", nullable = false)
    private Date dhAtualizacao;

    @Column(name = "IC_ASSINCRONO", nullable = false, precision = 1)
    private BigDecimal icAssincrono;

    @Column(name = "IC_PARALELISMO", nullable = false, precision = 1)
    private BigDecimal icParalelismo;

    @Column(name = "IC_SITUACAO", nullable = false, precision = 1)
    private BigDecimal icSituacao;
    
    @Column(name = "DE_XSLT_PARAMETRO", nullable = false, length = 200)
    private String deXsltParametro;
    
    @Column(name = "CO_VERSAO_BARRAMENTO", nullable = false, length = 10)
    private String versaoBarramento;

    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "NU_TAREFA_DESFAZIMENTO_012", referencedColumnName = "NU_TAREFA_002",
                insertable = false, updatable = false),
        @JoinColumn(name = "NU_VERSAO_TAREFA_DSFZO_012", referencedColumnName = "NU_VERSAO_TAREFA",
                insertable = false, updatable = false) })
    private Mtxtb012VersaoTarefa versaoTarefaDesfazimento;

    // bi-directional many-to-one association to Mtxtb003ServicoTarefa
    @OneToMany(mappedBy = "mtxtb012VersaoTarefa")
    private List<Mtxtb003ServicoTarefa> mtxtb003ServicoTarefas;

    // bi-directional many-to-one association to Mtxtb010VrsoTarfaMeioEntra
    @OneToMany(mappedBy = "mtxtb012VersaoTarefa")
    private List<Mtxtb010VrsoTarfaMeioEntra> mtxtb010VrsoTarfaMeioEntras;

    // bi-directional many-to-one association to Mtxtb002Tarefa
    @ManyToOne
    @JoinColumn(name = "NU_TAREFA_002", nullable = false, insertable = false, updatable = false)
    private Mtxtb002Tarefa mtxtb002Tarefa;

    // bi-directional many-to-one association to Mtxtb015VrsoSrvcoTrnsoTrfa
    @OneToMany(mappedBy = "mtxtb012VersaoTarefa")
    private List<Mtxtb015SrvcoTrnsoTrfa> mtxtb015VrsoSrvcoTrnsoTrfas;
    
    // bi-directional many-to-one association to Mtxtb003ServicoTarefa
    @OneToMany(mappedBy = "mtxtb012VersaoTarefa", fetch = FetchType.EAGER)
    private List<Mtxtb024TarefaFila> mtxtb024TarefaFila;

    public Mtxtb012VersaoTarefaPK getId() {
        return this.id;
    }

    public void setId(Mtxtb012VersaoTarefaPK id) {
        this.id = id;
    }

    public String getDeXsdRequisicao() {
        return this.deXsdRequisicao;
    }

    public void setDeXsdRequisicao(String deXsdRequisicao) {
        this.deXsdRequisicao = deXsdRequisicao;
    }

    public String getDeXsdResposta() {
        return this.deXsdResposta;
    }

    public void setDeXsdResposta(String deXsdResposta) {
        this.deXsdResposta = deXsdResposta;
    }

    public String getDeXsltRequisicao() {
        return this.deXsltRequisicao;
    }

    public void setDeXsltRequisicao(String deXsltRequisicao) {
        this.deXsltRequisicao = deXsltRequisicao;
    }

    public String getDeXsltResposta() {
        return this.deXsltResposta;
    }

    public void setDeXsltResposta(String deXsltResposta) {
        this.deXsltResposta = deXsltResposta;
    }

    public Date getDhAtualizacao() {
        return this.dhAtualizacao;
    }

    public void setDhAtualizacao(Date dhAtualizacao) {
        this.dhAtualizacao = dhAtualizacao;
    }

    public BigDecimal getIcAssincrono() {
        return this.icAssincrono;
    }

    public void setIcAssincrono(BigDecimal icAssincrono) {
        this.icAssincrono = icAssincrono;
    }

    public BigDecimal getIcParalelismo() {
        return this.icParalelismo;
    }

    public void setIcParalelismo(BigDecimal icParalelismo) {
        this.icParalelismo = icParalelismo;
    }

    public BigDecimal getIcSituacao() {
        return this.icSituacao;
    }

    public void setIcSituacao(BigDecimal icSituacao) {
        this.icSituacao = icSituacao;
    }
    public List<Mtxtb003ServicoTarefa> getMtxtb003ServicoTarefas() {
        return this.mtxtb003ServicoTarefas;
    }

    public void setMtxtb003ServicoTarefas(List<Mtxtb003ServicoTarefa> mtxtb003ServicoTarefas) {
        this.mtxtb003ServicoTarefas = mtxtb003ServicoTarefas;
    }

    public Mtxtb003ServicoTarefa addMtxtb003ServicoTarefa(Mtxtb003ServicoTarefa mtxtb003ServicoTarefa) {
        getMtxtb003ServicoTarefas().add(mtxtb003ServicoTarefa);
        mtxtb003ServicoTarefa.setMtxtb012VersaoTarefa(this);

        return mtxtb003ServicoTarefa;
    }

    public Mtxtb003ServicoTarefa removeMtxtb003ServicoTarefa(Mtxtb003ServicoTarefa mtxtb003ServicoTarefa) {
        getMtxtb003ServicoTarefas().remove(mtxtb003ServicoTarefa);
        mtxtb003ServicoTarefa.setMtxtb012VersaoTarefa(null);

        return mtxtb003ServicoTarefa;
    }

    public List<Mtxtb010VrsoTarfaMeioEntra> getMtxtb010VrsoTarfaMeioEntras() {
        return this.mtxtb010VrsoTarfaMeioEntras;
    }

    public void setMtxtb010VrsoTarfaMeioEntras(List<Mtxtb010VrsoTarfaMeioEntra> mtxtb010VrsoTarfaMeioEntras) {
        this.mtxtb010VrsoTarfaMeioEntras = mtxtb010VrsoTarfaMeioEntras;
    }

    public Mtxtb010VrsoTarfaMeioEntra addMtxtb010VrsoTarfaMeioEntra(
        Mtxtb010VrsoTarfaMeioEntra mtxtb010VrsoTarfaMeioEntra) {
        getMtxtb010VrsoTarfaMeioEntras().add(mtxtb010VrsoTarfaMeioEntra);
        mtxtb010VrsoTarfaMeioEntra.setMtxtb012VersaoTarefa(this);

        return mtxtb010VrsoTarfaMeioEntra;
    }

    public Mtxtb010VrsoTarfaMeioEntra removeMtxtb010VrsoTarfaMeioEntra(
        Mtxtb010VrsoTarfaMeioEntra mtxtb010VrsoTarfaMeioEntra) {
        getMtxtb010VrsoTarfaMeioEntras().remove(mtxtb010VrsoTarfaMeioEntra);
        mtxtb010VrsoTarfaMeioEntra.setMtxtb012VersaoTarefa(null);

        return mtxtb010VrsoTarfaMeioEntra;
    }

    public Mtxtb002Tarefa getMtxtb002Tarefa() {
        return this.mtxtb002Tarefa;
    }

    public void setMtxtb002Tarefa(Mtxtb002Tarefa mtxtb002Tarefa) {
        this.mtxtb002Tarefa = mtxtb002Tarefa;
    }

    public List<Mtxtb015SrvcoTrnsoTrfa> getMtxtb015VrsoSrvcoTrnsoTrfas() {
        return this.mtxtb015VrsoSrvcoTrnsoTrfas;
    }

    public void setMtxtb015VrsoSrvcoTrnsoTrfas(List<Mtxtb015SrvcoTrnsoTrfa> mtxtb015VrsoSrvcoTrnsoTrfas) {
        this.mtxtb015VrsoSrvcoTrnsoTrfas = mtxtb015VrsoSrvcoTrnsoTrfas;
    }

    public String getDeXsltParametro() {
		return deXsltParametro;
	}

	public void setDeXsltParametro(String deXsltParametro) {
		this.deXsltParametro = deXsltParametro;
	}

	public Mtxtb015SrvcoTrnsoTrfa addMtxtb015VrsoSrvcoTrnsoTrfa(
        Mtxtb015SrvcoTrnsoTrfa mtxtb015VrsoSrvcoTrnsoTrfa) {
        getMtxtb015VrsoSrvcoTrnsoTrfas().add(mtxtb015VrsoSrvcoTrnsoTrfa);
        mtxtb015VrsoSrvcoTrnsoTrfa.setMtxtb012VersaoTarefa(this);

        return mtxtb015VrsoSrvcoTrnsoTrfa;
    }

    public Mtxtb015SrvcoTrnsoTrfa removeMtxtb015VrsoSrvcoTrnsoTrfa(
        Mtxtb015SrvcoTrnsoTrfa mtxtb015VrsoSrvcoTrnsoTrfa) {
        getMtxtb015VrsoSrvcoTrnsoTrfas().remove(mtxtb015VrsoSrvcoTrnsoTrfa);
        mtxtb015VrsoSrvcoTrnsoTrfa.setMtxtb012VersaoTarefa(null);

        return mtxtb015VrsoSrvcoTrnsoTrfa;
    }

    public List<Mtxtb024TarefaFila> getMtxtb024TarefaFila() {
		return mtxtb024TarefaFila;
	}

	public void setMtxtb024TarefaFila(List<Mtxtb024TarefaFila> mtxtb024TarefaFila) {
		this.mtxtb024TarefaFila = mtxtb024TarefaFila;
	}
	
	public String getVersaoBarramento() {
		return versaoBarramento;
	}

	public void setVersaoBarramento(String versaoBarramento) {
		this.versaoBarramento = versaoBarramento;
	}
	

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb012VersaoTarefa [");
        if (id != null) {
            builder.append("id=");
            builder.append(id);
            builder.append(", ");
        }
        if (deXsdRequisicao != null) {
            builder.append("deXsdRequisicao=");
            builder.append(deXsdRequisicao);
            builder.append(", ");
        }
        if (deXsdResposta != null) {
            builder.append("deXsdResposta=");
            builder.append(deXsdResposta);
            builder.append(", ");
        }
        if (deXsltRequisicao != null) {
            builder.append("deXsltRequisicao=");
            builder.append(deXsltRequisicao);
            builder.append(", ");
        }
        if (deXsltResposta != null) {
            builder.append("deXsltResposta=");
            builder.append(deXsltResposta);
            builder.append(", ");
        }
        if (dhAtualizacao != null) {
            builder.append("dhAtualizacao=");
            builder.append(dhAtualizacao);
            builder.append(", ");
        }
        if (icAssincrono != null) {
            builder.append("icAssincrono=");
            builder.append(icAssincrono);
            builder.append(", ");
        }
        if (icParalelismo != null) {
            builder.append("icParalelismo=");
            builder.append(icParalelismo);
            builder.append(", ");
        }
        if (icSituacao != null) {
            builder.append("icSituacao=");
            builder.append(icSituacao);
            builder.append(", ");
        }
        if (mtxtb003ServicoTarefas != null) {
            builder.append("mtxtb003ServicoTarefas=");
            builder.append(mtxtb003ServicoTarefas);
            builder.append(", ");
        }
        if (mtxtb010VrsoTarfaMeioEntras != null) {
            builder.append("mtxtb010VrsoTarfaMeioEntras=");
            builder.append(mtxtb010VrsoTarfaMeioEntras);
            builder.append(", ");
        }
        if (mtxtb002Tarefa != null) {
            builder.append("mtxtb002Tarefa=");
            builder.append(mtxtb002Tarefa);
            builder.append(", ");
        }
        if (mtxtb015VrsoSrvcoTrnsoTrfas != null) {
            builder.append("mtxtb015VrsoSrvcoTrnsoTrfas=");
            builder.append(mtxtb015VrsoSrvcoTrnsoTrfas);
        }
        builder.append("]");
        return builder.toString();
    }

	public Mtxtb012VersaoTarefa getVersaoTarefaDesfazimento() {
		return versaoTarefaDesfazimento;
	}

	public void setVersaoTarefaDesfazimento(Mtxtb012VersaoTarefa versaoTarefaDesfazimento) {
		this.versaoTarefaDesfazimento = versaoTarefaDesfazimento;
	}
	
}