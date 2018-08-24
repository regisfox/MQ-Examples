/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntra;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntraPK;

public interface DaoVersaoTarefaMeioEntrada {
    public List<Mtxtb010VrsoTarfaMeioEntra> buscarTarefasMeioEntradaPorPK(Mtxtb010VrsoTarfaMeioEntraPK vrsoTarfaMeioEntraPK);

}
