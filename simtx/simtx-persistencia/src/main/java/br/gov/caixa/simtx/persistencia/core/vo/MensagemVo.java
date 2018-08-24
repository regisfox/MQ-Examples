/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.vo;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb002Tarefa;

public class MensagemVo {

    private long nuNsuMensagem;

    private String coMensagem;

    private String deMensagemNegocial;

    private String deMensagemTecnica;

    private Date dhAtualizacao;

    private BigDecimal icTipoMensagem;

    private List<Mtxtb002Tarefa> mtxtb002Tarefas;
    
    private String deOrigemRetorno;
    
    private String deMensagemRetorno;
    
    private String nameSpace1;
    
    private String nameSpace2;
    
    

	public long getNuNsuMensagem() {
		return nuNsuMensagem;
	}

	public void setNuNsuMensagem(long nuNsuMensagem) {
		this.nuNsuMensagem = nuNsuMensagem;
	}

	public String getCoMensagem() {
		return coMensagem;
	}

	public void setCoMensagem(String coMensagem) {
		this.coMensagem = coMensagem;
	}

	public String getDeMensagemNegocial() {
		return deMensagemNegocial;
	}

	public void setDeMensagemNegocial(String deMensagemNegocial) {
		this.deMensagemNegocial = deMensagemNegocial;
	}

	public String getDeMensagemTecnica() {
		return deMensagemTecnica;
	}

	public void setDeMensagemTecnica(String deMensagemTecnica) {
		this.deMensagemTecnica = deMensagemTecnica;
	}

	public Date getDhAtualizacao() {
		return dhAtualizacao;
	}

	public void setDhAtualizacao(Date dhAtualizacao) {
		this.dhAtualizacao = dhAtualizacao;
	}

	public BigDecimal getIcTipoMensagem() {
		return icTipoMensagem;
	}

	public void setIcTipoMensagem(BigDecimal icTipoMensagem) {
		this.icTipoMensagem = icTipoMensagem;
	}

	public List<Mtxtb002Tarefa> getMtxtb002Tarefas() {
		return mtxtb002Tarefas;
	}

	public void setMtxtb002Tarefas(List<Mtxtb002Tarefa> mtxtb002Tarefas) {
		this.mtxtb002Tarefas = mtxtb002Tarefas;
	}

	public String getDeOrigemRetorno() {
		return deOrigemRetorno;
	}

	public void setDeOrigemRetorno(String deOrigemRetorno) {
		this.deOrigemRetorno = deOrigemRetorno;
	}

	public String getDeMensagemRetorno() {
		return deMensagemRetorno;
	}

	public void setDeMensagemRetorno(String deMensagemRetorno) {
		this.deMensagemRetorno = deMensagemRetorno;
	}

	public String getNameSpace1() {
		return nameSpace1;
	}

	public void setNameSpace1(String nameSpace1) {
		this.nameSpace1 = nameSpace1;
	}

	public String getNameSpace2() {
		return nameSpace2;
	}

	public void setNameSpace2(String nameSpace2) {
		this.nameSpace2 = nameSpace2;
	}
}
