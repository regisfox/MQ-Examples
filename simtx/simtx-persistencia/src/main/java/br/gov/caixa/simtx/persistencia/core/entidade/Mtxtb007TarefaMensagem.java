/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.entidade;

import java.io.Serializable;

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
//@Cacheable
@Table(name = "MTXTB007_TAREFA_MENSAGEM")
@NamedQueries({ 
	@NamedQuery(name = "Mtxtb007TarefaMensagem.findAll", query = "SELECT m FROM Mtxtb007TarefaMensagem m"),
	@NamedQuery(name = "Mtxtb007TarefaMensagem.buscarPorTarefa", query = "SELECT m FROM Mtxtb007TarefaMensagem m join m.mtxtb006Mensagem ms where m.id.nuTarefa012 = :nuTarefa and m.id.nuVersaoTarefa012 = :nuVersaoTarefa and ms.icTipoMensagem = 3"),
	@NamedQuery(name = "Mtxtb007TarefaMensagem.buscarPorNumeroTarefa", query = "SELECT m FROM Mtxtb007TarefaMensagem m join m.mtxtb006Mensagem ms where m.id.nuTarefa012 = :nuTarefa"),
	@NamedQuery(name = "Mtxtb007TarefaMensagem.buscarPorTarefaCodRetorno", query = "SELECT m FROM Mtxtb007TarefaMensagem m join m.mtxtb006Mensagem ms where m.id.nuTarefa012 = :nuTarefa and m.id.nuVersaoTarefa012 = :nuVersaoTarefa and ms.coMensagem = :coMensagem"),
	})
public class Mtxtb007TarefaMensagem implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Mtxtb007TarefaMensagemPK id;
    
    @Column(name = "NO_CAMPO_RETORNO")
    private String noCampoRetorno;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "NU_TAREFA_012", referencedColumnName = "NU_TAREFA_002", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "NU_VERSAO_TAREFA_012", referencedColumnName = "NU_VERSAO_TAREFA", nullable = false, insertable = false, updatable = false) })
    private Mtxtb012VersaoTarefa mtxtb012VersaoTarefa;
    
    @ManyToOne
    @JoinColumn(name = "NU_MENSAGEM_006", nullable = false, insertable = false, updatable = false)
    private Mtxtb006Mensagem mtxtb006Mensagem;

	public Mtxtb007TarefaMensagemPK getId() {
		return id;
	}

	public void setId(Mtxtb007TarefaMensagemPK id) {
		this.id = id;
	}

	public String getNoCampoRetorno() {
		return noCampoRetorno;
	}

	public void setNoCampoRetorno(String noCampoRetorno) {
		this.noCampoRetorno = noCampoRetorno;
	}

	public Mtxtb012VersaoTarefa getMtxtb012VersaoTarefa() {
		return mtxtb012VersaoTarefa;
	}

	public void setMtxtb012VersaoTarefa(Mtxtb012VersaoTarefa mtxtb012VersaoTarefa) {
		this.mtxtb012VersaoTarefa = mtxtb012VersaoTarefa;
	}

	public Mtxtb006Mensagem getMtxtb006Mensagem() {
		return mtxtb006Mensagem;
	}

	public void setMtxtb006Mensagem(Mtxtb006Mensagem mtxtb006Mensagem) {
		this.mtxtb006Mensagem = mtxtb006Mensagem;
	}
}
