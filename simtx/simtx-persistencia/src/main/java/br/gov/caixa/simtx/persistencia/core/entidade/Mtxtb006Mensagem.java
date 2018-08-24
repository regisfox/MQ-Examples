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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;


/**
 * The persistent class for the MTXTB006_MENSAGEM database table.
 * 
 */
@Entity
@Cacheable
@Table(name = "MTXTB006_MENSAGEM")
@NamedQueries({ 
	@NamedQuery(name = "Mtxtb006Mensagem.findAll", query = "SELECT m FROM Mtxtb006Mensagem m"),
	@NamedQuery(name = "Mtxtb006Mensagem.buscarPorTarefaCodMensagem", query = "SELECT m FROM Mtxtb006Mensagem m where m.coMensagem = :coMensagem"),
})
public class Mtxtb006Mensagem implements Serializable {
    private static final String ERRO_HIBERNATE_ALTER = "Alterar este objeto somente se utilizar o new";

	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NU_NSU_MENSAGEM", unique = true, nullable = false, precision = 5)
    private long nuNsuMensagem;

    @Column(name = "CO_MENSAGEM", nullable = false, length = 3)
    private String coMensagem;

    @Column(name = "CO_RETORNO", nullable = false, length = 3)
    private String codigoRetorno;

    @Column(name = "DE_MENSAGEM_NEGOCIAL", nullable = false, length = 100)
    private String deMensagemNegocial;

    @Column(name = "DE_MENSAGEM_TECNICA", nullable = false, length = 100)
    private String deMensagemTecnica;

    @Column(name = "DH_ATUALIZACAO", nullable = false)
    private Date dhAtualizacao;

    @Column(name = "IC_TIPO_MENSAGEM", nullable = false, precision = 1)
    private Integer icTipoMensagem;


    public long getNuNsuMensagem() {
        return this.nuNsuMensagem;
    }

    public void setNuNsuMensagem(long nuNsuMensagem) {
        this.nuNsuMensagem = nuNsuMensagem;
    }

    public String getCoMensagem() {
        return this.coMensagem;
    }

    public void setCoMensagem(String coMensagem) {
        this.coMensagem = coMensagem;
    }

    public String getDeMensagemNegocial() {
        return this.deMensagemNegocial;
    }

    public void setDeMensagemNegocial(String deMensagemNegocial) {
    	if(this.nuNsuMensagem != 0) {
    		throw new IllegalStateException(ERRO_HIBERNATE_ALTER);
    	}
    	
        this.deMensagemNegocial = deMensagemNegocial;
    }

    public String getDeMensagemTecnica() {
        return this.deMensagemTecnica;
    }

    public void setDeMensagemTecnica(String deMensagemTecnica) {
    	if(this.nuNsuMensagem != 0) { 
    		throw new IllegalStateException(ERRO_HIBERNATE_ALTER);
    	}
    	
        this.deMensagemTecnica = deMensagemTecnica;
    }

    public Date getDhAtualizacao() {
        return this.dhAtualizacao;
    }

    public void setDhAtualizacao(Date dhAtualizacao) {
        this.dhAtualizacao = dhAtualizacao;
    }

    public Integer getIcTipoMensagem() {
        return this.icTipoMensagem;
    }

    public void setIcTipoMensagem(Integer icTipoMensagem) {
        this.icTipoMensagem = icTipoMensagem;
    }

	public boolean isImpeditiva() {
		return AcaoRetorno.IMPEDITIVA.getTipo().equals(this.icTipoMensagem);
	}

	public String getCodigoRetorno() {
		return codigoRetorno;
	}

	public void setCodigoRetorno(String codigoRetorno) {
		this.codigoRetorno = codigoRetorno;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Mtxtb006Mensagem [nuNsuMensagem=");
		builder.append(nuNsuMensagem);
		builder.append(", coMensagem=");
		builder.append(coMensagem);
		builder.append(", codigoRetorno=");
		builder.append(codigoRetorno);
		builder.append(", deMensagemNegocial=");
		builder.append(deMensagemNegocial);
		builder.append(", deMensagemTecnica=");
		builder.append(deMensagemTecnica);
		builder.append(", dhAtualizacao=");
		builder.append(dhAtualizacao);
		builder.append(", icTipoMensagem=");
		builder.append(icTipoMensagem);
		builder.append("]");
		return builder.toString();
	}
	
}
