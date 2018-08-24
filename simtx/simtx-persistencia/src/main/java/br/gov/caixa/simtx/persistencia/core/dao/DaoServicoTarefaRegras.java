/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegras;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegrasPK;

public interface DaoServicoTarefaRegras {
	
	/**
	 * Busca pelo Id.
	 * 
	 * @param canal
	 * @return
	 * @throws Exception
	 */
    public Mtxtb026ServicoTarefaRegras buscarPorPK(Mtxtb026ServicoTarefaRegrasPK regrasPK);
    
    /**
     * Busca as regras por Servico Tarefa.
     * 
     * @param canal
     * @return
     * @throws Exception
     */
    public List<Mtxtb026ServicoTarefaRegras> buscarPorServicoTarefa(Mtxtb026ServicoTarefaRegrasPK regrasPK);
    
}
