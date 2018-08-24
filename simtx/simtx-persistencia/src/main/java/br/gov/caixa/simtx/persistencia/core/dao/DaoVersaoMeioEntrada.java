/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.sql.SQLException;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb009VrsoMeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb009VrsoMeioEntradaPK;

public interface DaoVersaoMeioEntrada {
    public Mtxtb009VrsoMeioEntrada buscarPorPK(Mtxtb009VrsoMeioEntradaPK versaoMeioEntradaPK) throws SQLException;

}
