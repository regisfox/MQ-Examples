package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * The persistent class for the MTXTB037_GRUPO_ACESSO database table.
 * 
 */
@Entity
@Cacheable
@Table(name = "MTXTB038_GRUPO_ACESSO")
public class Mtxtb038GrupoAcesso implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "NU_GRUPO_ACESSO", unique = true, nullable = false)
	private int nuGrupoAcesso;
	
	@Column(name = "NO_GRUPO_ACESSO", unique = true, nullable = true)
	private String noGrupoAcesso;
	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "MTXTB039_PERMISSAO_ACESSO", joinColumns = { 
			@JoinColumn(name = "NU_GRUPO_ACESSO", nullable = false, updatable = false) }, inverseJoinColumns = { 
			@JoinColumn(name = "NU_FUNCIONALIDADE", nullable = false, updatable = false) })
	private List<Mtxtb037Funcionalidade> mtxtb037Funcionalidade;


	public int getNuGrupoAcesso() {
		return nuGrupoAcesso;
	}


	public void setNuGrupoAcesso(int nuGrupoAcesso) {
		this.nuGrupoAcesso = nuGrupoAcesso;
	}


	public String getNoGrupoAcesso() {
		return noGrupoAcesso;
	}


	public void setNoGrupoAcesso(String noGrupoAcesso) {
		this.noGrupoAcesso = noGrupoAcesso;
	}


	public List<Mtxtb037Funcionalidade> getMtxtb037Funcionalidade() {
		return mtxtb037Funcionalidade;
	}


	public void setMtxtb037Funcionalidade(List<Mtxtb037Funcionalidade> mtxtb037Funcionalidade) {
		this.mtxtb037Funcionalidade = mtxtb037Funcionalidade;
	}
}