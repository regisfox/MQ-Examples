package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoPrmtoPgtoCntngncia;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingencia;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingenciaPK;


@Stateless
public class DaoPrmtoPgtoCntngnciaImpl implements DaoPrmtoPgtoCntngncia {

	@PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private transient EntityManager entityManager;
	


	@Override
	public Mtxtb040PrmtoPgtoContingencia buscarPorCanaleBoleto(Mtxtb040PrmtoPgtoContingenciaPK prmtoPgtoCntngncia) {
		try {
			return entityManager
					.createNamedQuery("Mtxtb040PrmtoPgtoContingencia.buscarPorCanaleBoleto", Mtxtb040PrmtoPgtoContingencia.class)
					.setParameter("nuCanal004", prmtoPgtoCntngncia.getNuCanal004())
					.setParameter("icTipoBoleto", prmtoPgtoCntngncia.getIcTipoBoleto())
					.setParameter("icTipoContingencia", prmtoPgtoCntngncia.getIcOrigemContingencia())
					.getSingleResult();
		} 
		catch (NoResultException e) {
			return null;
		}
	}
	
	/**
     * Busca todos os canais.
     * 
     * @param canal
     * @throws Exception
     */
	public List<Mtxtb040PrmtoPgtoContingencia> buscarTodosParametrosBoletosContingencia() {
		return this.entityManager.createNamedQuery("Mtxtb040PrmtoPgtoContingencia.findAll", Mtxtb040PrmtoPgtoContingencia.class).getResultList();
	}

	@Override
	public void updateParametrosBoletosContingencia(List<Mtxtb040PrmtoPgtoContingencia> parametros) {
		for (final Mtxtb040PrmtoPgtoContingencia parametro : parametros) {
			this.entityManager.merge(parametro);
		}
		
		this.entityManager.flush();
	}
}
