package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the MTXTB039_PERMISSAO_ACESSO database table.
 * 
 */
@Entity
@Cacheable
@Table(name = "MTXTB039_PERMISSAO_ACESSO")
public class Mtxtb039PermissaoAcesso implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "NU_GRUPO_ACESSO")
	private int nuGrupoAcesso;

	@Column(name = "NU_FUNCIONALIDADE")
	private int nuFuncionalidade;

	public int getNuGrupoAcesso() {
		return nuGrupoAcesso;
	}

	public void setNuGrupoAcesso(int nuGrupoAcesso) {
		this.nuGrupoAcesso = nuGrupoAcesso;
	}

	public int getNuFuncionalidade() {
		return nuFuncionalidade;
	}

	public void setNuFuncionalidade(int nuFuncionalidade) {
		this.nuFuncionalidade = nuFuncionalidade;
	}
}