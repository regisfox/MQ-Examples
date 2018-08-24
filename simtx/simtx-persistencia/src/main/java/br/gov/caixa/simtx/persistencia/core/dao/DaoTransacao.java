/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;


public interface DaoTransacao {
	
	/**
	 * Busca a transacao pelo id.
	 * @param transacao
	 * @return {@link Mtxtb014Transacao}
	 * @throws SQLException
	 */
    public Mtxtb014Transacao buscarPorPK(Mtxtb014Transacao transacao);

    /**
     * Grava a transacao.
     * 
     * @param transacao
     * @return
     */
    public Mtxtb014Transacao salvar(Mtxtb014Transacao transacao);

    /**
     * Altera a transacao recebida e atualiza o campo Atualizacao.
     * 
     * @param transacao
     * @throws SQLException
     */
    public void alterar(Mtxtb014Transacao transacao);
    
    /**
     * Busca a transacao pelo campo NSU Origem.
     * 
     * @param transacao
     * @return {@link Mtxtb014Transacao}
     * @throws SQLException
     */
    public Mtxtb014Transacao buscarPorNSUPAI(Mtxtb014Transacao transacao);
    
    /**
	 * Verifica se possui transacoes para envio ao SICCO limitado a 1 linha para
	 * melhor performance.
	 * 
	 * @param dataReferencia
	 * @return
	 * @throws Exception
	 */
    public boolean possuiInformacoesParaEnvioCCO(Date dataReferencia);
    
    /**
     * Busca as transacoes pendentes de envio para o Sicco.
     * 
     * @param dataReferencia
     * @return
     */
    public List<Mtxtb014Transacao> buscarParaEnvioSicco(Date dataReferencia);
    
    /**
     * Busca as transacoes com as informações para limpeza (Expurgo).
     * 
     * @param dataReferencia
     * @return {@link List(Mtxtb014Transacao)}
     * @throws Exception
     */
	public List<Mtxtb014Transacao> buscarInformacoesParaLimpeza(Date dataReferencia);
	
	/**
	 * Remove as particoes informadas no processo de Expurgo.
	 * 
	 * @param particoes {@link String}.
	 * @throws Exception
	 */
	public int limparParticoes(String particoes);

    public Mtxtb014Transacao buscarTransacaoOrigem(Long nsu);
}
