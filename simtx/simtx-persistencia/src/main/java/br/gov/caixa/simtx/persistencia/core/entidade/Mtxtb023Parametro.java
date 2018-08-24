/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Cacheable
@Table(name = "MTXTB023_PARAMETRO_SISTEMA")
@NamedQuery(name = "Mtxtb023Parametro.findAll", query = "SELECT m FROM Mtxtb023Parametro m")
public class Mtxtb023Parametro implements Serializable {
    
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NU_PARAMETRO_SISTEMA", unique = true, nullable = false, precision = 5)
    private long nuParametro;

    @Column(name = "NO_PARAMETRO_SISTEMA")
    private String noParamSistema;
	
    @Column(name = "DE_CONTEUDO_PARAMETRO")
    private String deConteudoParam;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DH_ATUALIZACAO_PARAMETRO")
    private Date dhAtualizacaoParam;
    


    public long getNuParametro() {
		return nuParametro;
	}

	public void setNuParametro(long nuParametro) {
		this.nuParametro = nuParametro;
	}

	public String getNoParamSistema() {
		return noParamSistema;
	}

	public void setNoParamSistema(String noParamSistema) {
		this.noParamSistema = noParamSistema;
	}

	public String getDeConteudoParam() {
		return deConteudoParam;
	}

	public void setDeConteudoParam(String deConteudoParam) {
		this.deConteudoParam = deConteudoParam;
	}

	public Date getDhAtualizacaoParam() {
		return dhAtualizacaoParam;
	}

	public void setDhAtualizacaoParam(Date dhAtualizacaoParam) {
		this.dhAtualizacaoParam = dhAtualizacaoParam;
	}
}
