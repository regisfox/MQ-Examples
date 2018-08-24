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
@Table(name = "MTXTB024_TAREFA_FILA")
@NamedQueries({ @NamedQuery(name = "Mtxtb024TarefaFila.findAll", query = "SELECT m FROM Mtxtb024TarefaFila m"),
	@NamedQuery(name = "Mtxtb024TarefaFila.buscarTarefasFilas", query = "SELECT m FROM Mtxtb024TarefaFila m WHERE m.id.nuTarefa012 = :nuTarefa AND m.id.nuVersaoTarefa012 = :nuVersaoTarefa")
	})
public class Mtxtb024TarefaFila implements Serializable {
		
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId	
	private Mtxtb024TarefaFilaPK id;
	
	@Column(name = "NO_FILA_REQUISICAO", nullable = false, length = 50)
    private String noFilaRequisicao;

    @Column(name = "NO_FILA_RESPOSTA", nullable = false, length = 50)
    private String noFilaResposta;

    @Column(name = "QT_SGNDO_LMTE_REQUISICAO", precision = 4)
    private Integer nuTimeoutRequisicao;

    @Column(name = "QT_SGNDO_LMTE_RESPOSTA", precision = 4)
    private BigDecimal nuTimeoutResposta;
    
    @Column(name = "NO_CONEXAO", nullable = false, length = 100)
    private String noConnectionFactory;
    
    @Column(name = "NO_MODO_INTEGRACAO", nullable = false, length = 50)
    private String noModoIntegracao;
    
    @Column(name = "NO_RECURSO", nullable = false, length = 50)
    private String noRecurso;
    
    @Column(name = "QT_TEMPO_ESPERA", nullable = false, length = 3)
    private Integer qtdeTempoEspera;
    
    // bi-directional many-to-one association to Mtxtb012VersaoTarefa
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "NU_TAREFA_012", referencedColumnName = "NU_TAREFA_002", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "NU_VERSAO_TAREFA_012", referencedColumnName = "NU_VERSAO_TAREFA", nullable = false, insertable = false, updatable = false) })
    private Mtxtb012VersaoTarefa mtxtb012VersaoTarefa;
    

	public Mtxtb024TarefaFilaPK getId() {
		return id;
	}

	public void setId(Mtxtb024TarefaFilaPK id) {
		this.id = id;
	}

	public String getNoFilaRequisicao() {
		return noFilaRequisicao;
	}

	public void setNoFilaRequisicao(String noFilaRequisicao) {
		this.noFilaRequisicao = noFilaRequisicao;
	}

	public String getNoFilaResposta() {
		return noFilaResposta;
	}

	public void setNoFilaResposta(String noFilaResposta) {
		this.noFilaResposta = noFilaResposta;
	}

	public Integer getNuTimeoutRequisicao() {
		return nuTimeoutRequisicao;
	}

	public void setNuTimeoutRequisicao(Integer nuTimeoutRequisicao) {
		this.nuTimeoutRequisicao = nuTimeoutRequisicao;
	}

	public BigDecimal getNuTimeoutResposta() {
		return nuTimeoutResposta;
	}

	public void setNuTimeoutResposta(BigDecimal nuTimeoutResposta) {
		this.nuTimeoutResposta = nuTimeoutResposta;
	}

	public String getNoConnectionFactory() {
		return noConnectionFactory;
	}

	public void setNoConnectionFactory(String noConnectionFactory) {
		this.noConnectionFactory = noConnectionFactory;
	}

	public String getNoModoIntegracao() {
		return noModoIntegracao;
	}

	public void setNoModoIntegracao(String noModoIntegracao) {
		this.noModoIntegracao = noModoIntegracao;
	}

	public String getNoRecurso() {
		return noRecurso;
	}

	public void setNoRecurso(String noRecurso) {
		this.noRecurso = noRecurso;
	}

	public Integer getQtdeTempoEspera() {
		return qtdeTempoEspera;
	}

	public void setQtdeTempoEspera(Integer qtdeTempoEspera) {
		this.qtdeTempoEspera = qtdeTempoEspera;
	}

	public Mtxtb012VersaoTarefa getMtxtb012VersaoTarefa() {
		return mtxtb012VersaoTarefa;
	}

	public void setMtxtb012VersaoTarefa(Mtxtb012VersaoTarefa mtxtb012VersaoTarefa) {
		this.mtxtb012VersaoTarefa = mtxtb012VersaoTarefa;
	}

	@Override
	public String toString() {
		return "Mtxtb024TarefaFila [id=" + id + ", noFilaRequisicao=" + noFilaRequisicao + ", noFilaResposta="
				+ noFilaResposta + ", nuTimeoutRequisicao=" + nuTimeoutRequisicao + ", nuTimeoutResposta="
				+ nuTimeoutResposta + ", noConnectionFactory=" + noConnectionFactory + ", noModoIntegracao="
				+ noModoIntegracao + ", noRecurso=" + noRecurso + ", qtdeTempoEspera=" + qtdeTempoEspera
				+ ", mtxtb012VersaoTarefa=" + mtxtb012VersaoTarefa + "]";
	}
}
