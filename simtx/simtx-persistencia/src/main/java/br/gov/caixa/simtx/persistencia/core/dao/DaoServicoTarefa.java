/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao;

import java.sql.SQLException;
import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefaPK;

public interface DaoServicoTarefa {
	
	/**
	 * Busca as tarefas negociais.
	 * 
	 * @param servicoTarefaPK
	 * @return
	 */
    public List<Mtxtb003ServicoTarefa> buscarTarefasNegocialPorServico(Mtxtb003ServicoTarefaPK servicoTarefaPK);

    public Mtxtb003ServicoTarefa buscarTarefasPorPK(Mtxtb003ServicoTarefaPK servicoTarefaPK);
    
	public List<Mtxtb003ServicoTarefa> buscarTarefasMeioEntradaPorServico(long nuMeioEntrada, long nuVersaoMeioEntrada,
			long nuServico, long nuVersaoServico) throws SQLException;
    
    public List<Mtxtb003ServicoTarefa> listarTarefaNegocialCanal(long nuServico, long nuVersaoServico, long nuCanal);
    
    /**
     * Busca as tarefas por Servico e Meio Entrada.
     * 
     * @param nuMeioEntrada
     * @param nuServico
     * @param nuVersaoServico
     * @return
     * @throws SQLException
     */
	public List<Mtxtb003ServicoTarefa> buscarTarefasMeioEntradaPorServico(long nuMeioEntrada, long nuServico,
			long nuVersaoServico);
    
    /**
     * Lista todas as tarefas do Servico com o Canal.
     * 
     * @param nuServico
     * @param nuVersaoServico
     * @param nuCanal
     * @return
     * @throws Exception
     */
    public List<Mtxtb003ServicoTarefa> listarTarefasServico(long nuServico, long nuVersaoServico, long nuCanal);
}
