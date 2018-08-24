/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.sql.SQLException;
import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntra;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntraPK;

public interface DaoTarefaMeioEntrada {
    public List<Mtxtb010VrsoTarfaMeioEntra> buscarPorMeioEntrada(Mtxtb010VrsoTarfaMeioEntraPK tarefaMeioEntrada) throws SQLException;

}
