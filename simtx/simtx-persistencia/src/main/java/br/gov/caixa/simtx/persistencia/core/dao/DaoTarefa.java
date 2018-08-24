/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb002Tarefa;

public interface DaoTarefa {
	
	public Mtxtb002Tarefa buscarPorPK(Mtxtb002Tarefa tarefa);
	
	public List<Mtxtb002Tarefa> findAll();

}
