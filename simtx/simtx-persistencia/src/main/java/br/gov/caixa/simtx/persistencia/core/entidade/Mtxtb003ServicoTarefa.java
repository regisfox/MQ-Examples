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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.gov.caixa.simtx.persistencia.constante.Constantes;

/**
 * The persistent class for the MTXTB003_SERVICO_TAREFA database table.
 * 
 */
@Entity
@Cacheable
@Table(name = "MTXTB003_SERVICO_TAREFA")
@NamedQueries({ @NamedQuery(name = "Mtxtb003ServicoTarefa.findAll", query = "SELECT m FROM Mtxtb003ServicoTarefa m"),
		@NamedQuery(name = "Mtxtb003ServicoTarefa.buscarTarefasNegocial", query = "SELECT m FROM Mtxtb003ServicoTarefa m where m.id.nuServico011 = :nuServico and m.id.nuVersaoServico011 = :nuVersaoServico"),
		@NamedQuery(name = "Mtxtb003ServicoTarefa.buscarTarefaMeioEntrada", query = "SELECT st FROM Mtxtb010VrsoTarfaMeioEntra m join m.mtxtb012VersaoTarefa vt join vt.mtxtb003ServicoTarefas st join vt.mtxtb024TarefaFila tf where m.id.nuMeioEntrada008 = :nuMeioEntrada and st.id.nuServico011 = :nuServico and st.id.nuVersaoServico011 = :nuVersaoServico AND st.icSituacao =:icSituacao"),
		@NamedQuery(name = "Mtxtb003ServicoTarefa.listarTarefaNegocialCanal", query = "SELECT st FROM Mtxtb020SrvcoTarfaCanal stc JOIN stc.mtxtb003ServicoTarefa st JOIN st.mtxtb012VersaoTarefa vt JOIN vt.mtxtb002Tarefa t JOIN stc.mtxtb004Canal c WHERE stc.id.nuServico003 = :nuServico AND stc.id.nuVersaoServico003 = :nuVersaoServico AND c.nuCanal = :nuCanal AND st.icSituacao =:icSituacao AND t.icTipoTarefa IN :icTipoTarefas ORDER BY st.nuSequenciaExecucao"),
		@NamedQuery(name = "Mtxtb003ServicoTarefa.listarTarefasServico", query = "SELECT st FROM Mtxtb020SrvcoTarfaCanal stc JOIN stc.mtxtb003ServicoTarefa st JOIN st.mtxtb012VersaoTarefa vt JOIN vt.mtxtb002Tarefa t JOIN stc.mtxtb004Canal c WHERE stc.id.nuServico003 = :nuServico AND stc.id.nuVersaoServico003 = :nuVersaoServico AND c.nuCanal = :nuCanal AND st.icSituacao =:icSituacao ORDER BY st.nuSequenciaExecucao") })
public class Mtxtb003ServicoTarefa implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private Mtxtb003ServicoTarefaPK id;

	@Column(name = "IC_IMPEDIMENTO", nullable = false, precision = 1)
	private BigDecimal icImpedimento;

	@Column(name = "IC_SITUACAO", nullable = false, precision = 1)
	private BigDecimal icSituacao;

	@Column(name = "NU_SEQUENCIA_EXECUCAO", nullable = false, precision = 5)
	private Integer nuSequenciaExecucao;

	@Column(name = "DE_XSLT_NOVA_TAREFA", nullable = false, length = 200)
	private String deXsltNovaTarefa;

	// bi-directional many-to-one association to Mtxtb011VersaoServico
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "NU_SERVICO_011", referencedColumnName = "NU_SERVICO_001", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "NU_VERSAO_SERVICO_011", referencedColumnName = "NU_VERSAO_SERVICO", nullable = false, insertable = false, updatable = false) })
	private Mtxtb011VersaoServico mtxtb011VersaoServico;

	// bi-directional many-to-one association to Mtxtb012VersaoTarefa
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "NU_TAREFA_012", referencedColumnName = "NU_TAREFA_002", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "NU_VERSAO_TAREFA_012", referencedColumnName = "NU_VERSAO_TAREFA", nullable = false, insertable = false, updatable = false) })
	private Mtxtb012VersaoTarefa mtxtb012VersaoTarefa;

	// bi-directional many-to-many association to Mtxtb004Canal
	@ManyToMany(mappedBy = "mtxtb003ServicoTarefas")
	private List<Mtxtb004Canal> mtxtb004Canals;


	public Mtxtb003ServicoTarefaPK getId() {
		return this.id;
	}

	public void setId(Mtxtb003ServicoTarefaPK id) {
		this.id = id;
	}

	public BigDecimal getIcImpedimento() {
		return this.icImpedimento;
	}

	public void setIcImpedimento(BigDecimal icImpedimento) {
		this.icImpedimento = icImpedimento;
	}

	public BigDecimal getIcSituacao() {
		return this.icSituacao;
	}

	public void setIcSituacao(BigDecimal icSituacao) {
		this.icSituacao = icSituacao;
	}

	public Integer getNuSequenciaExecucao() {
		return this.nuSequenciaExecucao;
	}

	public void setNuSequenciaExecucao(Integer nuSequenciaExecucao) {
		this.nuSequenciaExecucao = nuSequenciaExecucao;
	}

	public String getDeXsltNovaTarefa() {
		return deXsltNovaTarefa;
	}

	public void setDeXsltNovaTarefa(String deXsltNovaTarefa) {
		this.deXsltNovaTarefa = deXsltNovaTarefa;
	}

	public Mtxtb011VersaoServico getMtxtb011VersaoServico() {
		return this.mtxtb011VersaoServico;
	}

	public void setMtxtb011VersaoServico(Mtxtb011VersaoServico mtxtb011VersaoServico) {
		this.mtxtb011VersaoServico = mtxtb011VersaoServico;
	}

	public Mtxtb012VersaoTarefa getMtxtb012VersaoTarefa() {
		return this.mtxtb012VersaoTarefa;
	}

	public void setMtxtb012VersaoTarefa(Mtxtb012VersaoTarefa mtxtb012VersaoTarefa) {
		this.mtxtb012VersaoTarefa = mtxtb012VersaoTarefa;
	}

	public List<Mtxtb004Canal> getMtxtb004Canals() {
		return this.mtxtb004Canals;
	}

	public void setMtxtb004Canals(List<Mtxtb004Canal> mtxtb004Canals) {
		this.mtxtb004Canals = mtxtb004Canals;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Mtxtb003ServicoTarefa [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (icImpedimento != null) {
			builder.append("icImpedimento=");
			builder.append(icImpedimento);
			builder.append(", ");
		}
		if (icSituacao != null) {
			builder.append("icSituacao=");
			builder.append(icSituacao);
			builder.append(", ");
		}
		if (nuSequenciaExecucao != null) {
			builder.append("nuSequenciaExecucao=");
			builder.append(nuSequenciaExecucao);
			builder.append(", ");
		}
		if (deXsltNovaTarefa != null) {
			builder.append("deXsltNovo=");
			builder.append(deXsltNovaTarefa);
			builder.append(", ");
		}
		if (mtxtb011VersaoServico != null) {
			builder.append("mtxtb011VersaoServico=");
			builder.append(mtxtb011VersaoServico);
			builder.append(", ");
		}
		if (mtxtb012VersaoTarefa != null) {
			builder.append("mtxtb012VersaoTarefa=");
			builder.append(mtxtb012VersaoTarefa);
			builder.append(", ");
		}
		if (mtxtb004Canals != null) {
			builder.append("mtxtb004Canals=");
			builder.append(mtxtb004Canals);
		}
		builder.append("]");
		return builder.toString();
	}

	public boolean isMeioDeEntrada() {
		return Constantes.IC_TAREFA_MEIOENTRADA.equals(getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getIcTipoTarefa());
	}

	public boolean isNegocial() {
		return Constantes.IC_TAREFA_NEGOCIAL.equals(getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getIcTipoTarefa());
	}

	public boolean isImpeditiva() {
		return BigDecimal.ONE.equals(getIcImpedimento());
	}

}
