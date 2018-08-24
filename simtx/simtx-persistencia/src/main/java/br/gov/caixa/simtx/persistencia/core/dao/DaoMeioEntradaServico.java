/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.sql.SQLException;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvco;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvcoPK;

public interface DaoMeioEntradaServico {
	
	/**
	 * Busca pelo Id.
	 * 
	 * @param meioEntradaServicoPK
	 * @return
	 * @throws SQLException
	 */
    public Mtxtb018VrsoMeioEntraSrvco buscarPorPK(Mtxtb018VrsoMeioEntraSrvcoPK meioEntradaServicoPK);

}
