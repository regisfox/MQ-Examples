/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.sql.SQLException;
import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServicoPK;

public interface DaoVersaoServico {
	
	/**
	 * Busca pelo Id.
	 * 
	 * @param versaoServicoPK
	 * @return
	 * @throws SQLException
	 */
    public Mtxtb011VersaoServico buscarPorPK(Mtxtb011VersaoServicoPK versaoServicoPK);
    
    /**
     * Busca a versaoServico e Servico pelo NomeServicoBarramento e OperacaoBarramento.
     * 
     * @param versaoServico
     * @return
     * @throws Exception
     */
    public Mtxtb011VersaoServico buscarPorNomeOperacao(Mtxtb011VersaoServico versaoServico);
    
    /**
     * Atualiza a situacao do servico
     * 
     * @return lista de servicos
     * @throws Exception
     */
    public void alteraSituacaoServico(List<Mtxtb011VersaoServico> listaServicos);
    
    /**
     * Busca todas versoes servico por nome do servico.
     * 
     *  @param nomeServico Nome do servico.
     *  
     *  @throws Exception Se ocorrerem erros.
     *  
     *  @return Lista de versao servico.  
     * 
     */
    public List<Mtxtb011VersaoServico> buscarVersaoServicoPorNome(String nomeServico);
    
    /**
     * Busca todas versoes servico por situacao da versao servico.
     * 
     * @param situacaoServico Situacao da versao servico.
     * 
     * @throws Exception Se ocorrerem erros.
     * 
     * @return Lista de versao servico.
     *  
     */
    public List<Mtxtb011VersaoServico> buscarVersaoServicoPorSituacao(int situacaoVersaoServico);
    
    /**
     * Busca todas as versoes servicos.
     * 
     *  @return Lista de versao servico.
     *  
     *  @throws Exception Se ocorrer erros.
     */
    public List<Mtxtb011VersaoServico> buscarTodosVersaoServico();
}
