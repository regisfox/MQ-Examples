/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.caixa.simtx.persistencia.core.dao.DaoTransacaoConta;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;

@Stateless
public class DaoTransacaoContaImpl implements DaoTransacaoConta {

	@PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
	private EntityManager em;
	

	public List<Mtxtb035TransacaoConta> buscaTransacao(Mtxtb035TransacaoConta transacaoConta) {

		List<Mtxtb035TransacaoConta> retorno = null;

		try {

			CriteriaBuilder qb = em.getCriteriaBuilder();
			CriteriaQuery<Mtxtb035TransacaoConta> cq = qb.createQuery(Mtxtb035TransacaoConta.class);
			Root<Mtxtb035TransacaoConta> customer = cq.from(Mtxtb035TransacaoConta.class);
			List<Predicate> predicates = new ArrayList<>();
			
			if (transacaoConta.getNumeroUnidade() != 0) {
				predicates.add(qb.equal(customer.get("numeroUnidade"), transacaoConta.getNumeroUnidade()));		
			}
			if (transacaoConta.getOpProduto() != 0) {
				predicates.add(qb.equal(customer.get("opProduto"), transacaoConta.getOpProduto()));
			}
			if (transacaoConta.getNumeroConta() != 0) {
				predicates.add(qb.equal(customer.get("numeroConta"), transacaoConta.getNumeroConta()));
			}
			if (transacaoConta.getNuDvConta() != 0) {
				predicates.add(qb.equal(customer.get("nuDvConta"), transacaoConta.getNuDvConta()));
			}
			if (transacaoConta.getMtxtb004Canal().getNuCanal() != 0) {
				predicates.add(qb.equal(customer.get("nuCanal"), transacaoConta.getMtxtb004Canal().getNuCanal()));
			}
			if (transacaoConta.getMtxtb001Servico().getNuServico() != 0) {
				predicates.add(qb.equal(customer.get("numeroServico"), transacaoConta.getMtxtb001Servico().getNuServico()));
			}
			
			Path<Date> dateEntryPath = customer.get("dataReferencia");
			predicates.add(qb.between(dateEntryPath, retornaDataSemTime(), new Date()));
			predicates.add(qb.equal(customer.get("icSituacao"), 1));

			cq.select(customer).where(predicates.toArray(new Predicate[] {}));
			retorno = em.createQuery(cq).getResultList();

		} catch (NoResultException nre) {
			retorno = null;
		}
		return retorno;
	}

	public Date retornaDataSemTime() {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	@Override
	public Mtxtb035TransacaoConta salvar(Mtxtb035TransacaoConta transacaoConta) {
		this.em.persist(transacaoConta);
		this.em.flush();
		this.em.refresh(transacaoConta);
		return transacaoConta;
	}

	public void atualizaStatusPagamento(Long nsuOrigem, Long icSituacao) {
		Query query = em.createQuery("update Mtxtb035TransacaoConta set icSituacao =:situacao where nuNsuTransacao =:nsuTransacao");
		query.setParameter("situacao", icSituacao);
		query.setParameter("nsuTransacao", nsuOrigem);
		query.executeUpdate();
	}
	
	@Override
	public Mtxtb035TransacaoConta buscarPorNsu(Long nuNsuTransacao) {
		Mtxtb035TransacaoConta mtxtb035TransacaoConta = null;
		try {
			mtxtb035TransacaoConta = em.find(Mtxtb035TransacaoConta.class, nuNsuTransacao);
		} catch (NoResultException e) {
			mtxtb035TransacaoConta = null;
		} 
		return mtxtb035TransacaoConta;
	}

}
