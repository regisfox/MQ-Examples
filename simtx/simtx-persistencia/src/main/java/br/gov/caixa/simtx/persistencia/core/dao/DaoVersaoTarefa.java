/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefaPK;

public interface DaoVersaoTarefa {
	
	/**
	 * Busca pelo Id.
	 * 
	 * @param versaoTarefaPK
	 * @return
	 */
    public Mtxtb012VersaoTarefa buscarPorPK(Mtxtb012VersaoTarefaPK versaoTarefaPK);

}
