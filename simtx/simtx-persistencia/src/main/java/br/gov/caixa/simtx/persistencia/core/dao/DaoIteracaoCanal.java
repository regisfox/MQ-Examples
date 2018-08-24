/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.util.Date;
import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;


public interface DaoIteracaoCanal {
	
    public Mtxtb016IteracaoCanal salvar(Mtxtb016IteracaoCanal iteracaoCanal);

    /**
     * Altera a entidade.
     * 
     * @param iteracaoCanal
     */
    public void alterar(Mtxtb016IteracaoCanal iteracaoCanal);

    long buscarMaxPK();

    public List<Mtxtb016IteracaoCanal> buscarTransacoesPendentes(Date data);

    public List<Mtxtb016IteracaoCanal> buscarTransacoesPendentesParaEnvio();

    public Mtxtb016IteracaoCanal buscarPorPK(Mtxtb016IteracaoCanal iteracaoCanal);
    
    public List<Mtxtb016IteracaoCanal> buscarIteracao(long nsu);
    
	public Mtxtb016IteracaoCanal buscaUltimoMtxtb016IteracaoCanal(long nsu);
    
    /**
     * Busca as transacoes pendentes (Campos Envio e Recebimento) para envio ao SICCO.
     * 
     * @param diaAnterior
     * @return {@link List(Mtxtb016IteracaoCanal)}
     * @throws Exception
     */
    public List<Mtxtb016IteracaoCanal> buscarTransacoesParaEnvioCCO(Date diaAnterior);
}
