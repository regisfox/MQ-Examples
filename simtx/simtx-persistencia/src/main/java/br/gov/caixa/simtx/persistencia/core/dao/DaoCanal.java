/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.math.BigDecimal;
import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;

public interface DaoCanal {
	
	/**
	 * Busca pelo Id.
	 * 
	 * @param canal
	 * @return
	 * @throws Exception
	 */
    public Mtxtb004Canal buscarPorPK(Mtxtb004Canal canal);
    
    /**
     * Busca pela Sigla.
     * 
     * @param canal
     * @return
     * @throws Exception
     */
    public Mtxtb004Canal buscarPorSigla(Mtxtb004Canal canal);
    
    /**
     * Busca por Situação.
     * 
     * @param canal
     * @return
     * @throws Exception
     */
    public List<Mtxtb004Canal> buscarPorSituacao(BigDecimal situacao);
    
    /**
     * Atualizacao sitacao canal.
     * 
     * @param lista de canal
     * @return
     * @throws Exception
     */
    public void alteraSituacaoCanal(List<Mtxtb004Canal> listaCanais);
    
    /**
     * Busca todos os canais.
     * 
     * @param canal
     * @return
     * @throws Exception
     */
    public List<Mtxtb004Canal> buscarTodosCanais();
}
