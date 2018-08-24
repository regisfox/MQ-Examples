/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.caixa.simtx.persistencia.core.dao.DaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;

@Stateless
public class DaoServicoImpl implements DaoServico {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    
    
    @Override
    public Mtxtb001Servico buscarPorPK(Mtxtb001Servico servico) {
    	return em.find(Mtxtb001Servico.class, servico.getNuServico());
    }
    
    /**
     * Obtem o nome do servico.
     *
     * @param nuServico the nu servico
     * @return String representando o nome do servico
     * @throws SQLException the SQL exception
     */
    @Override
    public String buscarNome(Long nuServico) {
		Query query = em.createQuery("SELECT s.noServico FROM " + Mtxtb001Servico.class.getSimpleName() + " s WHERE s.nuServico = :nuServico", String.class);
		query.setParameter("nuServico", nuServico);
		return (String) query.getSingleResult();
    }
    
    /**
     * {@inheritDoc} 
     * 
     */
    @Override
    public Mtxtb001Servico buscarPorNomeOperacao(Mtxtb001Servico servico) {
    	Mtxtb001Servico retorno = null;
    	try {
			retorno = this.em.createNamedQuery("Mtxtb001Servico.buscarPorNomeOperacao", Mtxtb001Servico.class)
					.setParameter("noServicoBarramento", servico.getNoServicoBarramento())
					.setParameter("noOperacaoBarramento", servico.getNoOperacaoBarramento())
					.getSingleResult();
        }
    	catch (NoResultException nre) {
    		return null;
        }
    	return retorno;
    }

	public List<Mtxtb001Servico> buscarPorServicoCancelamento(int cancelamento) {
		return this.em.createNamedQuery("Mtxtb001Servico.buscaPorCancelamento", Mtxtb001Servico.class)
				.setParameter("icCancelamento", cancelamento).getResultList();
	}
	
	public List<Mtxtb001Servico> buscarTodosServicos() {
		List<Mtxtb001Servico> listaServicos = null;
		try {
			listaServicos = em.createNamedQuery("Mtxtb001Servico.findAll", Mtxtb001Servico.class).getResultList();
		} catch (NoResultException nre) {
			listaServicos = null;
		}
		return listaServicos;
	}
}
