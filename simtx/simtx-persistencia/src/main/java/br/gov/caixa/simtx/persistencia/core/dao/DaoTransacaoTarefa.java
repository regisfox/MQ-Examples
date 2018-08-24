/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;

public interface DaoTransacaoTarefa {
    public Mtxtb015SrvcoTrnsoTrfa salvar(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa);
    public Mtxtb015SrvcoTrnsoTrfa alterar(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa);
    public Mtxtb015SrvcoTrnsoTrfa buscarPorFiltro(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa);
    public List<Mtxtb015SrvcoTrnsoTrfa> buscarPorFiltroCorp(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa);
    List<Mtxtb015SrvcoTrnsoTrfa> buscarTarefasPorNsu(long nsu);
    
}
