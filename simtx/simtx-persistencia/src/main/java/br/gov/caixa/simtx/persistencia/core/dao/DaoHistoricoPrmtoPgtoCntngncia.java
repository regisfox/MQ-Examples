package br.gov.caixa.simtx.persistencia.core.dao;

import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb041HistoricoPrmtoPgtoContingencia;

/**
 * DAO para salvar auditoria de parametros de boleto contingencia.
 * 
 * @author joseoliveirajunior 
 */
public interface DaoHistoricoPrmtoPgtoCntngncia {
	
	/**
	  * Grava a auditoria da atualizacao do historico de parametro boleto de contingencia.
	  * 
	  * @param parametroBoletoContingencia
	  * 
	  */
	public void salvaHistoricoParametroBoleto(Mtxtb041HistoricoPrmtoPgtoContingencia parametroBoletoContingencia);
}
