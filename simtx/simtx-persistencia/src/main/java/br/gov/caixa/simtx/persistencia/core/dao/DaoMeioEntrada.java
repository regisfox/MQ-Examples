/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;

public interface DaoMeioEntrada {
	
	/**
	 * Busca pelo Id.
	 * 
	 * @param meioEntrada
	 * @return
	 */
    public Mtxtb008MeioEntrada buscarPorPK(Mtxtb008MeioEntrada meioEntrada);
    
    /**
     * Busca pelo nome.
     * 
     * @param meioEntrada
     * @return
     * @throws Exception
     */
    public Mtxtb008MeioEntrada buscarPorNome(Mtxtb008MeioEntrada meioEntrada);
    
}
