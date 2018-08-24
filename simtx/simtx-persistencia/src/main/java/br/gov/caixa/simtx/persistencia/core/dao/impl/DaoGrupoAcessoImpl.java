package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.caixa.simtx.persistencia.core.dao.DaoGrupoAcesso;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb038GrupoAcesso;

public class DaoGrupoAcessoImpl implements DaoGrupoAcesso {

	@PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
	EntityManager entityManager;
	
	public Mtxtb038GrupoAcesso buscaGrupoAcesso(String grupo) {
		try {
			CriteriaBuilder qb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Mtxtb038GrupoAcesso> cq = qb.createQuery(Mtxtb038GrupoAcesso.class);
			Root<Mtxtb038GrupoAcesso> customer = cq.from(Mtxtb038GrupoAcesso.class);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(qb.equal(customer.get("noGrupoAcesso"), grupo));
			cq.select(customer).where(predicates.toArray(new Predicate[] {}));
	
			return entityManager.createQuery(cq).getSingleResult();
		} 
		catch (NoResultException e) {
			return null;
		}
		
	}
}
