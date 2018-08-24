package br.gov.caixa.simtx.persistencia.core.dao;

import java.util.List;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingencia;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingenciaPK;

public interface DaoPrmtoPgtoCntngncia {
	
    public Mtxtb040PrmtoPgtoContingencia buscarPorCanaleBoleto(Mtxtb040PrmtoPgtoContingenciaPK prmtoPgtoCntngncia);
    
    /**
     * Busca todos os parametros de boletos contingencia.
     * 
     *  @return Lista de todos os parametros de boletos contingencia.
     */
    public List<Mtxtb040PrmtoPgtoContingencia> buscarTodosParametrosBoletosContingencia();
    
    /**
	 * Atualiza os parametros boletos de contingencia.
	 * 
	 *  @param parametros Parametros de boletos de contingencia a serem atualizados (somente altera valor limite e status).
	 */
	public void updateParametrosBoletosContingencia(List<Mtxtb040PrmtoPgtoContingencia> parametros);

}
