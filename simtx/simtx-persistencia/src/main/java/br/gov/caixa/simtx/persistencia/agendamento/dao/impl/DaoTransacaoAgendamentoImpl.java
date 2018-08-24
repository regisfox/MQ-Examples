package br.gov.caixa.simtx.persistencia.agendamento.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.agendamento.dao.DaoTransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;

@Stateless
public class DaoTransacaoAgendamentoImpl implements DaoTransacaoAgendamento {

	private String icSituacao = "icSituacao";
	/** The em. */
	@PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
	private EntityManager em;
	
	private static final Logger logger = Logger.getLogger(DaoTransacaoAgendamentoImpl.class);


	@Override
	public Mtxtb034TransacaoAgendamento salvar(Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		this.em.persist(transacaoAgendamento);
		this.em.flush();
		this.em.refresh(transacaoAgendamento);
		return transacaoAgendamento;
	}

	/**
	 * {@inheritDoc}
	 */
	public Mtxtb034TransacaoAgendamento buscaTransacaoAgendamentoPorPK(Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		return this.em.find(Mtxtb034TransacaoAgendamento.class, transacaoAgendamento.getNuNsuTransacaoAgendamento());
	}
	
	@Override
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoPeriodo(Mtxtb034TransacaoAgendamento transacaoAgendamento, Date periodoInicio, Date periodoFinal) {
		return buscaTransacoesAgendamentoPeriodoAux(transacaoAgendamento, periodoInicio, periodoFinal, 1);
	}

	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamento(Mtxtb034TransacaoAgendamento transacaoAgendamento) {

		List<Mtxtb034TransacaoAgendamento> retorno = null;

		try {

			CriteriaBuilder qb = em.getCriteriaBuilder();
			CriteriaQuery<Mtxtb034TransacaoAgendamento> cq = qb.createQuery(Mtxtb034TransacaoAgendamento.class);
			Root<Mtxtb034TransacaoAgendamento> customer = cq.from(Mtxtb034TransacaoAgendamento.class);
			List<Predicate> predicates = new ArrayList<>();

			if (transacaoAgendamento.getNuUnidade() != 0) {
				predicates.add(qb.equal(customer.get("nuUnidade"), transacaoAgendamento.getNuUnidade()));
			}
			
			if (transacaoAgendamento.getNuProduto() != 0) {
				predicates.add(qb.equal(customer.get("nuProduto"), transacaoAgendamento.getNuProduto()));
			}
			
			if (transacaoAgendamento.getNuConta() != 0) {
				predicates.add(qb.equal(customer.get("nuConta"), transacaoAgendamento.getNuConta()));
			}
			
			if (transacaoAgendamento.getDvConta() != 0) {
				predicates.add(qb.equal(customer.get("dvConta"), transacaoAgendamento.getDvConta()));
			}
			
			if (transacaoAgendamento.getDtReferencia() != null) {
				Path<Date> dataReferencia = customer.get("dtReferencia");
				predicates.add(qb.between(dataReferencia, menorData(transacaoAgendamento.getDtReferencia()), maiorData(transacaoAgendamento.getDtReferencia())));
			}

			if (transacaoAgendamento.getDtEfetivacao() != null) {
				Path<Date> dataEfetivacao = customer.get("dtEfetivacao");
				predicates.add(qb.between(dataEfetivacao, menorData(transacaoAgendamento.getDtEfetivacao()), maiorData(transacaoAgendamento.getDtEfetivacao())));
			}

			if (transacaoAgendamento.getMtxtb004Canal().getNuCanal() != 0) {
				predicates.add(qb.equal(customer.get("nuCanal"), transacaoAgendamento.getMtxtb004Canal().getNuCanal()));
			}

			if (transacaoAgendamento.getMtxtb001Servico().getNuServico() != 0) {
				predicates.add(qb.equal(customer.get("nuServico"), transacaoAgendamento.getMtxtb001Servico().getNuServico()));
			}

			predicates.add(qb.equal(customer.get(icSituacao), 1));

			cq.select(customer).where(predicates.toArray(new Predicate[] {}));
			retorno = em.createQuery(cq).getResultList();

		} catch (NoResultException nre) {
			retorno = null;
		} catch (Exception e) {
			throw e;
		}
		return retorno;

	}

	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoPorData(Date date) {

		List<Mtxtb034TransacaoAgendamento> retorno = null;

		try {

			CriteriaBuilder qb = em.getCriteriaBuilder();
			CriteriaQuery<Mtxtb034TransacaoAgendamento> cq = qb.createQuery(Mtxtb034TransacaoAgendamento.class);
			Root<Mtxtb034TransacaoAgendamento> customer = cq.from(Mtxtb034TransacaoAgendamento.class);
			List<Predicate> predicates = new ArrayList<>();
			if (date != null) {
				Path<Date> dataReferencia = customer.get("dtEfetivacao");
				predicates.add(qb.between(dataReferencia, menorData(date), maiorData(date)));
				predicates.add(qb.equal(customer.get(icSituacao), 1));
			}

			cq.select(customer).where(predicates.toArray(new Predicate[] {}));
			retorno = em.createQuery(cq).getResultList();

		} catch (NoResultException nre) {
			retorno = null;
		} catch (Exception e) {
			throw e;
		}
		return retorno;

	}

	public void alterar(Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		this.em.merge(transacaoAgendamento);
		this.em.flush();
	}

	/**
	 * Retorna o valor do horário minimo para a data de referencia passada.
	 * 
	 * Por exemplo se a data for "30/01/2018 as 17h:33m:12s e 299ms" a data retornada por este metodo será
	 * "30/01/2018 as 00h:00m:00s e 000ms".
	 * 
	 * @param date
	 *            de referencia.
	 * @return {@link Date} que representa o horário minimo para dia informado.
	 */
	public Date menorData(Date date) {
		return dataSemTime(date);
	}

	/**
	 * Retorna o valor do horário maximo para a data de referencia passada.
	 * 
	 * Por exemplo se a data for "30/01/2018 as 17h:33m:12s e 299ms&quot; a data retornada por este metodo será
	 * "30/01/2018 as 23h:59m:59s e 999ms";.
	 * 
	 * @param date
	 *            de referencia.
	 * @return {@link Date} que representa o horário maximo para dia informado.
	 */
	public Date maiorData(Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * Zera todas as referencias de hora, minuto, segundo e milesegundo do {@link Calendar}.
	 * 
	 * @param date a ser modificado.
	 */
	public Date dataSemTime(Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	@Override
	public List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoNaoEfetivadas(
			Mtxtb034TransacaoAgendamento transacaoAgendamento) {

		// 1- Agendado e 4 - NEGADO.
		final List<Mtxtb034TransacaoAgendamento> list = buscaTransacoesAgendamentoPeriodoAux(transacaoAgendamento, transacaoAgendamento.getDtEfetivacao(), transacaoAgendamento.getDtEfetivacao(), 1, 4); 
		
		if(!list.isEmpty()) {
			Collections.sort(list, new Mtxtb034TransacaoAgendamentoComparator());
		}
		
		return list;
	}
	
	private List<Mtxtb034TransacaoAgendamento> buscaTransacoesAgendamentoPeriodoAux(Mtxtb034TransacaoAgendamento transacaoAgendamento, Date periodoInicio, Date periodoFinal, Integer ... situacoes) {
		List<Mtxtb034TransacaoAgendamento> retorno = null;

		try {

			CriteriaBuilder qb = em.getCriteriaBuilder();
			CriteriaQuery<Mtxtb034TransacaoAgendamento> cq = qb.createQuery(Mtxtb034TransacaoAgendamento.class);
			Root<Mtxtb034TransacaoAgendamento> customer = cq.from(Mtxtb034TransacaoAgendamento.class);
			List<Predicate> predicates = new ArrayList<>();
			
			if (0 != transacaoAgendamento.getNuUnidade()) {
				predicates.add(qb.equal(customer.get("nuUnidade"), transacaoAgendamento.getNuUnidade()));	
			}
			if (0 != transacaoAgendamento.getNuProduto()) {
				predicates.add(qb.equal(customer.get("nuProduto"), transacaoAgendamento.getNuProduto()));
			}	
			if (0 != transacaoAgendamento.getNuConta()) {
				predicates.add(qb.equal(customer.get("nuConta"), transacaoAgendamento.getNuConta()));
			}	
			if (0 != transacaoAgendamento.getDvConta()) {
				predicates.add(qb.equal(customer.get("dvConta"), transacaoAgendamento.getDvConta()));
			}
			
			if (periodoInicio != null && periodoFinal != null) {
				Path<Date> dataReferencia = customer.get("dtReferencia");
				predicates.add(qb.between(dataReferencia, menorData(periodoInicio), maiorData(periodoFinal)));
			}

			if (null != transacaoAgendamento.getMtxtb004Canal() && 0 != transacaoAgendamento.getMtxtb004Canal().getNuCanal()) {
				predicates.add(qb.equal(customer.get("nuCanal"), transacaoAgendamento.getMtxtb004Canal().getNuCanal()));
			}

			if (null != transacaoAgendamento.getMtxtb001Servico() && 0 != transacaoAgendamento.getMtxtb001Servico().getNuServico()) {
				predicates.add(qb.equal(customer.get("nuServico"), transacaoAgendamento.getMtxtb001Servico().getNuServico()));
			}

			// Situacoes.
			final List<Integer> situacoesList = Arrays.asList(situacoes);
			
			final Expression<String> situacaoExp = customer.get(icSituacao);
			
			final Predicate pSituacoes = situacaoExp.in(situacoesList);

		
			predicates.add(pSituacoes);

			cq.select(customer).where(predicates.toArray(new Predicate[] {}));
			
			retorno = em.createQuery(cq).getResultList();

		} catch (NoResultException nre) {
			retorno = null;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return retorno;
	}
	
	private static class Mtxtb034TransacaoAgendamentoComparator implements Comparator<Mtxtb034TransacaoAgendamento> {

		@Override
		public int compare(Mtxtb034TransacaoAgendamento o1, Mtxtb034TransacaoAgendamento o2) {
			final int result = -o1.getValorTransacao().compareTo(o2.getValorTransacao());
			
			return result;
		}
	}
}
