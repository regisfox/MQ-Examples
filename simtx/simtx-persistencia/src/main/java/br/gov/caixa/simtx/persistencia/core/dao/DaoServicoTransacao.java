/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnso;

public interface DaoServicoTransacao {
    
	public Mtxtb017VersaoSrvcoTrnso salvar(Mtxtb017VersaoSrvcoTrnso versaoServicoTransacao);
    
	public Mtxtb017VersaoSrvcoTrnso buscarPorNSU(Mtxtb017VersaoSrvcoTrnso versaoServicoTransacao);

}
