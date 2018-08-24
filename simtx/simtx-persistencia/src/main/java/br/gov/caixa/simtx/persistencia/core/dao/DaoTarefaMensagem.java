/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagemPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;

public interface DaoTarefaMensagem {
	
    /**
     * Busca as mensagens Autorizadoras pelo Codigo da tarefa.
     * 
     * @param tarefaMensagemPK
     * @return
     */
    public List<Mtxtb007TarefaMensagem> buscarAutorizadorasPorTarefa(Mtxtb007TarefaMensagemPK tarefaMensagemPK);
    
    /**
     * Busca por Tarefa e Codigo de Retorno.
     * 
     * @param codigoMensagem
     * @param versaoTarefa
     * @return
     */
    public Mtxtb007TarefaMensagem buscarPorTarefaCodRetorno(String codigoMensagem, Mtxtb012VersaoTarefa versaoTarefa) throws SemResultadoException;
    
    /**
     * Busca as mensagens por numero da tarefa.
     * 
     * @param nuTarefa Numero da tarefa.
     * @return Lista de tarefa mensagens.
     */
    public List<Mtxtb007TarefaMensagem> buscarTarefaMensagensPorNumeroTarefa(long nuTarefa);
}
