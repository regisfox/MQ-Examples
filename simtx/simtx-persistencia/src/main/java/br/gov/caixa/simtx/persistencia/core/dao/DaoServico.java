/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.sql.SQLException;
import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;

public interface DaoServico {

    public Mtxtb001Servico buscarPorPK(Mtxtb001Servico servico);
    
    /**
     * Obtem o nome do servico
     * @param nuServico
     * @return String representando o nome do servico
     * @throws SQLException
     */
    public String buscarNome(Long nuServico);
    
    /**
     * Busca o servico pelo NomeBarramento e OperacaoBarramento.
     * 
     * @param servico
     * @return
     * @throws Exception
     */
    public Mtxtb001Servico buscarPorNomeOperacao(Mtxtb001Servico servico);

    /**
     * Busca os serviços pelo indentificador do cancelamento
     * 
     * @param servico
     * @return
     * @throws Exception
     */
    public List<Mtxtb001Servico> buscarPorServicoCancelamento(int cancelamento);
    
    /**
     * Busca todos os servicos
     * 
     * @return lista de servicos
     * @throws Exception
     */
    public List<Mtxtb001Servico> buscarTodosServicos();
    
}
