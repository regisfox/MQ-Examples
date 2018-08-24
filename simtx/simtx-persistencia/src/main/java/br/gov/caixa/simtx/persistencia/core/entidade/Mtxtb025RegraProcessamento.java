/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Cacheable
@Table(name = "MTXTB025_REGRA_PROCESSAMENTO")
@NamedQueries({
	@NamedQuery(name = "Mtxtb025RegraProcessamento.findAll", query = "SELECT r FROM Mtxtb025RegraProcessamento r")
})
public class Mtxtb025RegraProcessamento implements Serializable {
	
	/** Serial. */
	private static final long serialVersionUID = 1L;
	
	/** Id */
	@Id
    @Column(name = "NU_REGRA", unique = true, nullable = false, precision = 6)
    private long nuRegra;
	
	/** Campo Dependencia */
	@Column(name = "NO_CAMPO_DEPENDENCIA", nullable = false, precision = 50)
    private String noCampoDependencia;
	
	/** Servico Origem */
	@Column(name = "NO_SERVICO_ORIGEM", nullable = false, precision = 50)
    private String noServicoOrigem;
	
	/** Operacao Origem */
	@Column(name = "NO_OPERACAO_ORIGEM", nullable = false, precision = 50)
    private String noOperacaoOrigem;
	
	/** Caminho Informacao */
	@Column(name = "DE_CAMINHO_INFORMACAO", nullable = false, precision = 100)
    private String deCaminhoInformacao;

	public long getNuRegra() {
		return nuRegra;
	}

	public void setNuRegra(long nuRegra) {
		this.nuRegra = nuRegra;
	}

	public String getNoCampoDependencia() {
		return noCampoDependencia;
	}

	public void setNoCampoDependencia(String noCampoDependencia) {
		this.noCampoDependencia = noCampoDependencia;
	}

	public String getNoServicoOrigem() {
		return noServicoOrigem;
	}

	public void setNoServicoOrigem(String noServicoOrigem) {
		this.noServicoOrigem = noServicoOrigem;
	}

	public String getNoOperacaoOrigem() {
		return noOperacaoOrigem;
	}

	public void setNoOperacaoOrigem(String noOperacaoOrigem) {
		this.noOperacaoOrigem = noOperacaoOrigem;
	}

	public String getDeCaminhoInformacao() {
		return deCaminhoInformacao;
	}

	public void setDeCaminhoInformacao(String deCaminhoInformacao) {
		this.deCaminhoInformacao = deCaminhoInformacao;
	}
	
}
