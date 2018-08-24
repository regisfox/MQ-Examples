/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the MTXTB011_VERSAO_SERVICO database table.
 * 
 */
@Entity
@Cacheable
@Table(name = "MTXTB011_VERSAO_SERVICO")
@NamedQueries({
	@NamedQuery(name = "Mtxtb011VersaoServico.findAll", query = "SELECT m FROM Mtxtb011VersaoServico m"),
	//@NamedQuery(name = "Mtxtb011VersaoServico.buscarPorSituacaoServico", query = "SELECT vs FROM Mtxtb011VersaoServico vs join vs.mtxtb001Servico s where s.nuServico = :nuServico and vs.icStcoVrsoSrvco = :situacaoServico"),
	@NamedQuery(name = "Mtxtb011VersaoServico.buscarVersaoServicoPorNome", query ="SELECT vs FROM Mtxtb011VersaoServico vs " +
			 "  join vs.mtxtb001Servico s " +
			 " where s.noServico = :nomeServico"),
	@NamedQuery(name = "Mtxtb011VersaoServico.buscarVersaoServicoPorSituacao", query ="SELECT vs FROM Mtxtb011VersaoServico vs " +
			 " where vs.icStcoVrsoSrvco = :situacaoServico"),
		
	@NamedQuery(name = "Mtxtb011VersaoServico.buscarPorNomeOperacao", query ="SELECT vs FROM Mtxtb011VersaoServico vs " +
																			 "  join vs.mtxtb001Servico s " +
																			 " where s.noServicoBarramento = :noServicoBarramento " +
																			 "   and s.noOperacaoBarramento = :noOperacaoBarramento " +
																			 "   and vs.id.nuVersaoServico = :nuVersaoServico")
})
public class Mtxtb011VersaoServico implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb011VersaoServicoPK id;

    @Column(name = "DE_XSD_REQUISICAO", nullable = false, length = 200)
    private String deXsdRequisicao;

    @Column(name = "DE_XSD_RESPOSTA", nullable = false, length = 200)
    private String deXsdResposta;

    @Column(name = "DE_XSLT_REQUISICAO", nullable = false, length = 200)
    private String deXsltRequisicao;

    @Column(name = "DE_XSLT_RESPOSTA", nullable = false, length = 200)
    private String deXsltResposta;

    @Column(name = "DH_ATUALIZACAO", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dhAtualizacao;

    @Column(name = "IC_STCO_VRSO_SRVCO", nullable = false, precision = 1)
    private Integer icStcoVrsoSrvco;
    
    // bi-directional many-to-one association to Mtxtb003ServicoTarefa
    @OneToMany(mappedBy = "mtxtb011VersaoServico")
    private List<Mtxtb003ServicoTarefa> mtxtb003ServicoTarefas;

    // bi-directional many-to-one association to Mtxtb001Servico
    @ManyToOne
    @JoinColumn(name = "NU_SERVICO_001", nullable = false, insertable = false, updatable = false)
    private Mtxtb001Servico mtxtb001Servico;

    // bi-directional many-to-one association to Mtxtb017VersaoSrvcoTrnso
    @OneToMany(mappedBy = "mtxtb011VersaoServico")
    private List<Mtxtb017VersaoSrvcoTrnso> mtxtb017VersaoSrvcoTrnsos;

    // bi-directional many-to-one association to Mtxtb018VrsoMeioEntraSrvco
    @OneToMany(mappedBy = "mtxtb011VersaoServico")
    private List<Mtxtb018VrsoMeioEntraSrvco> mtxtb018VrsoMeioEntraSrvcos;
    
    @Column(name = "IC_SERVICO_MIGRADO", nullable = false)
    private Integer migrado;


    public Mtxtb011VersaoServicoPK getId() {
        return this.id;
    }

    public void setId(Mtxtb011VersaoServicoPK id) {
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

    public Integer getIcStcoVrsoSrvco() {
        return this.icStcoVrsoSrvco;
    }

    public void setIcStcoVrsoSrvco(Integer icStcoVrsoSrvco) {
        this.icStcoVrsoSrvco = icStcoVrsoSrvco;
    }
    
    public Integer getMigrado() {
		return migrado;
	}

	public void setMigrado(Integer migrado) {
		this.migrado = migrado;
	}

	public List<Mtxtb003ServicoTarefa> getMtxtb003ServicoTarefas() {
        return this.mtxtb003ServicoTarefas;
    }

    public void setMtxtb003ServicoTarefas(List<Mtxtb003ServicoTarefa> mtxtb003ServicoTarefas) {
        this.mtxtb003ServicoTarefas = mtxtb003ServicoTarefas;
    }

    public Mtxtb003ServicoTarefa addMtxtb003ServicoTarefa(Mtxtb003ServicoTarefa mtxtb003ServicoTarefa) {
        getMtxtb003ServicoTarefas().add(mtxtb003ServicoTarefa);
        mtxtb003ServicoTarefa.setMtxtb011VersaoServico(this);

        return mtxtb003ServicoTarefa;
    }

    public Mtxtb003ServicoTarefa removeMtxtb003ServicoTarefa(Mtxtb003ServicoTarefa mtxtb003ServicoTarefa) {
        getMtxtb003ServicoTarefas().remove(mtxtb003ServicoTarefa);
        mtxtb003ServicoTarefa.setMtxtb011VersaoServico(null);

        return mtxtb003ServicoTarefa;
    }

    public Mtxtb001Servico getMtxtb001Servico() {
        return this.mtxtb001Servico;
    }

    public void setMtxtb001Servico(Mtxtb001Servico mtxtb001Servico) {
        this.mtxtb001Servico = mtxtb001Servico;
    }

    public List<Mtxtb017VersaoSrvcoTrnso> getMtxtb017VersaoSrvcoTrnsos() {
        return this.mtxtb017VersaoSrvcoTrnsos;
    }

    public void setMtxtb017VersaoSrvcoTrnsos(List<Mtxtb017VersaoSrvcoTrnso> mtxtb017VersaoSrvcoTrnsos) {
        this.mtxtb017VersaoSrvcoTrnsos = mtxtb017VersaoSrvcoTrnsos;
    }

    public Mtxtb017VersaoSrvcoTrnso addMtxtb017VersaoSrvcoTrnso(Mtxtb017VersaoSrvcoTrnso mtxtb017VersaoSrvcoTrnso) {
        getMtxtb017VersaoSrvcoTrnsos().add(mtxtb017VersaoSrvcoTrnso);
        mtxtb017VersaoSrvcoTrnso.setMtxtb011VersaoServico(this);

        return mtxtb017VersaoSrvcoTrnso;
    }

    public Mtxtb017VersaoSrvcoTrnso removeMtxtb017VersaoSrvcoTrnso(Mtxtb017VersaoSrvcoTrnso mtxtb017VersaoSrvcoTrnso) {
        getMtxtb017VersaoSrvcoTrnsos().remove(mtxtb017VersaoSrvcoTrnso);
        mtxtb017VersaoSrvcoTrnso.setMtxtb011VersaoServico(null);

        return mtxtb017VersaoSrvcoTrnso;
    }

    public List<Mtxtb018VrsoMeioEntraSrvco> getMtxtb018VrsoMeioEntraSrvcos() {
        return this.mtxtb018VrsoMeioEntraSrvcos;
    }

    public void setMtxtb018VrsoMeioEntraSrvcos(List<Mtxtb018VrsoMeioEntraSrvco> mtxtb018VrsoMeioEntraSrvcos) {
        this.mtxtb018VrsoMeioEntraSrvcos = mtxtb018VrsoMeioEntraSrvcos;
    }

    public Mtxtb018VrsoMeioEntraSrvco addMtxtb018VrsoMeioEntraSrvco(
        Mtxtb018VrsoMeioEntraSrvco mtxtb018VrsoMeioEntraSrvco) {
        getMtxtb018VrsoMeioEntraSrvcos().add(mtxtb018VrsoMeioEntraSrvco);
        mtxtb018VrsoMeioEntraSrvco.setMtxtb011VersaoServico(this);

        return mtxtb018VrsoMeioEntraSrvco;
    }

    public Mtxtb018VrsoMeioEntraSrvco removeMtxtb018VrsoMeioEntraSrvco(
        Mtxtb018VrsoMeioEntraSrvco mtxtb018VrsoMeioEntraSrvco) {
        getMtxtb018VrsoMeioEntraSrvcos().remove(mtxtb018VrsoMeioEntraSrvco);
        mtxtb018VrsoMeioEntraSrvco.setMtxtb011VersaoServico(null);

        return mtxtb018VrsoMeioEntraSrvco;
    }
    
    public boolean isMigrado() {
    	return this.migrado.equals(1);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mtxtb011VersaoServico [");
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
        if (icStcoVrsoSrvco != null) {
            builder.append("icStcoVrsoSrvco=");
            builder.append(icStcoVrsoSrvco);
            builder.append(", ");
        }
        if (mtxtb003ServicoTarefas != null) {
            builder.append("mtxtb003ServicoTarefas=");
            builder.append(mtxtb003ServicoTarefas);
            builder.append(", ");
        }
        if (mtxtb001Servico != null) {
            builder.append("mtxtb001Servico=");
            builder.append(mtxtb001Servico);
            builder.append(", ");
        }
        if (mtxtb017VersaoSrvcoTrnsos != null) {
            builder.append("mtxtb017VersaoSrvcoTrnsos=");
            builder.append(mtxtb017VersaoSrvcoTrnsos);
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
