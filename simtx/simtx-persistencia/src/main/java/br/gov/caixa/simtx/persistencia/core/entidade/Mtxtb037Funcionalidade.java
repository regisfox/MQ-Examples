package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the MTXTB038_FUNCIONALIDADE_ACESSO database table.
 * 
 */

@Entity
@Cacheable
@Table(name = "MTXTB037_FUNCIONALIDADE")
public class Mtxtb037Funcionalidade implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "NU_FUNCIONALIDADE", unique = true, nullable = false)
	private int nuFuncionalidade;
	
	@Column(name = "NO_FUNCIONALIDADE", unique = true, nullable = false)
	private String noFuncionalidade;

	public int getNuFuncionalidade() {
		return nuFuncionalidade;
	}

	public void setNuFuncionalidade(int nuFuncionalidade) {
		this.nuFuncionalidade = nuFuncionalidade;
	}

	public String getNoFuncionalidade() {
		return noFuncionalidade;
	}

	public void setNoFuncionalidade(String noFuncionalidade) {
		this.noFuncionalidade = noFuncionalidade;
	}
}