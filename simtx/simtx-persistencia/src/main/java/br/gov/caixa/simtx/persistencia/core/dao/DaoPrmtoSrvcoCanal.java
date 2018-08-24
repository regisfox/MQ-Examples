/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.sql.SQLException;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb005ServicoCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb013PrmtoSrvcoCanal;

public interface DaoPrmtoSrvcoCanal {
	
	/**
	 * Busca pelo Id.
	 * 
	 * @param servicoCanalPK
	 * @return
	 * @throws SQLException
	 */
    public Mtxtb013PrmtoSrvcoCanal buscarPorServicoCanal(Mtxtb005ServicoCanalPK servicoCanalPK);
}
