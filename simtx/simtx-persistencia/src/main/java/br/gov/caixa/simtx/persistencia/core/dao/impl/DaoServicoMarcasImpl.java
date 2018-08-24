package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoServicoMarcas;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb032MarcaConta;

@Stateless
public class DaoServicoMarcasImpl implements DaoServicoMarcas {


    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager entityManager;
    
	public List<Mtxtb032MarcaConta> buscaServicos(long nuServico) {
        
		List<Mtxtb032MarcaConta> retorno = null;
        
        try {
            retorno = entityManager.createNamedQuery("Mtxtb033ServicoMarcaConta.buscaServicoMarca", Mtxtb032MarcaConta.class)
            		.setParameter("numeroServico", nuServico).getResultList();
        }
        catch (NoResultException nre) {
            retorno = null;
        }
		return retorno;
	}

}
