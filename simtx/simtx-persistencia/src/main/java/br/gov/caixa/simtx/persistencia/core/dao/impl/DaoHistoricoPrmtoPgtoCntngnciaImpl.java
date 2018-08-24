package br.gov.caixa.simtx.persistencia.core.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.core.dao.DaoHistoricoPrmtoPgtoCntngncia;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb041HistoricoPrmtoPgtoContingencia;


/**
 * Implementacao do DAO para salvar auditoria em parametros de boleto contingencia.
 * 
 *  @author joseoliveirajunior
 */
@Stateless
public class DaoHistoricoPrmtoPgtoCntngnciaImpl implements DaoHistoricoPrmtoPgtoCntngncia {

	
	@PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager entityManager;
	
    private static final Logger logger = Logger.getLogger(DaoHistoricoPrmtoPgtoCntngnciaImpl.class);

	@Override
	public void salvaHistoricoParametroBoleto(Mtxtb041HistoricoPrmtoPgtoContingencia parametroBoletoContingencia) {
		try {
			entityManager.persist(parametroBoletoContingencia);
			entityManager.flush();
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
