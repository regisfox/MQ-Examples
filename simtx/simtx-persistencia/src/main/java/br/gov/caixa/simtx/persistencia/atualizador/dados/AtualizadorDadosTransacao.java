package br.gov.caixa.simtx.persistencia.atualizador.dados;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb036TransacaoAuditada;

/**
 * Controle de atualizacao das tabelas referentes a atualizacao do ciclo da transacao.
 * @author rsfagundes
 *
 */
@Stateless
public class AtualizadorDadosTransacao implements Serializable {

	private static final long serialVersionUID = -2572367238460699102L;

	private static final Logger logger = Logger.getLogger(AtualizadorDadosTransacao.class);

	@PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
	private transient EntityManager em;

	@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
	public boolean atualizarDadosTransacaoTarefasIteracoes(Mtxtb014Transacao transacao, List<Mtxtb015SrvcoTrnsoTrfa> listaTarefas,
			Mtxtb016IteracaoCanal iteracaoCanal) {
		logger.debug("inicio atualizarDadosTransacaoTarefasIteracoes");
		boolean sucesso = false;
		try {
			em.merge(transacao);
			em.merge(iteracaoCanal);
			if (listaTarefas != null) {
				for (Mtxtb015SrvcoTrnsoTrfa mtxtb015SrvcoTrnsoTrfa : listaTarefas) {
					em.persist(mtxtb015SrvcoTrnsoTrfa);
				}
			}
			em.flush();
			sucesso = true;
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("finalizando atualizarDadosTransacaoTarefasIteracoes");
		return sucesso;
	}

	@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
	public boolean atualizarDadosTransacaoCore(Mtxtb014Transacao transacao, List<Mtxtb015SrvcoTrnsoTrfa> listaTarefas, Mtxtb016IteracaoCanal iteracaoCanal,
			Mtxtb035TransacaoConta transacaoConta) {
		logger.debug("inicio atualizarDadosTransacaoCore");
		boolean sucesso = false;
		try {
			em.merge(transacao);
			em.merge(iteracaoCanal);
			if (listaTarefas != null) {
				for (Mtxtb015SrvcoTrnsoTrfa mtxtb015SrvcoTrnsoTrfa : listaTarefas) {
					em.persist(mtxtb015SrvcoTrnsoTrfa);
				}
			}
			if (transacaoConta != null) {
				em.persist(transacaoConta);
			}
			em.flush();
			sucesso = true;
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("finalizando atualizarDadosTransacaoCore");
		return sucesso;
	}

	@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
	public boolean atualizarDadosTransacaoCancelamento(Mtxtb014Transacao transacao, List<Mtxtb015SrvcoTrnsoTrfa> listaTarefas, Mtxtb016IteracaoCanal iteracaoCanal,
			Mtxtb035TransacaoConta transacaoConta, Mtxtb036TransacaoAuditada transacaoAuditada) {
		logger.debug("inicializando atualizarDadosTransacaoCancelamento");
		boolean sucesso = false;
		try {
			em.merge(transacao);
			em.merge(transacaoConta);
			em.merge(iteracaoCanal);
			if (listaTarefas != null) {
				for (Mtxtb015SrvcoTrnsoTrfa mtxtb015SrvcoTrnsoTrfa : listaTarefas) {
					em.persist(mtxtb015SrvcoTrnsoTrfa);
				}
			}
			em.persist(transacaoAuditada);
			em.flush();
			sucesso = true;
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("finalizando atualizarDadosTransacaoCancelamento");
		return sucesso;
	}

	@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
	public boolean atualizarDadosTransacaoCancelamentoAgendamento(Mtxtb014Transacao transacao, List<Mtxtb015SrvcoTrnsoTrfa> listaTarefas,
			Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb034TransacaoAgendamento transacaoAgendamento, Mtxtb036TransacaoAuditada transacaoAuditada) {
		logger.debug("inicializando atualizarDadosTransacaoCancelamentoAgendamento");
		boolean sucesso = false;
		try {
			em.merge(transacao);
			em.merge(transacaoAgendamento);
			em.merge(iteracaoCanal);
			if (listaTarefas != null) {
				for (Mtxtb015SrvcoTrnsoTrfa mtxtb015SrvcoTrnsoTrfa : listaTarefas) {
					em.persist(mtxtb015SrvcoTrnsoTrfa);
				}
			}
			em.persist(transacaoAuditada);
			em.flush();
			sucesso = true;
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("finalizando atualizarDadosTransacaoCancelamentoAgendamento");
		return sucesso;
	}

	@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
	public boolean atualizarDadosTransacaoAgendamento(Mtxtb014Transacao transacao, List<Mtxtb015SrvcoTrnsoTrfa> listaTarefas,
			Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb034TransacaoAgendamento transacaoAgendamento) {
		logger.debug("inicializando atualizarDadosTransacaoAgendamento");
		boolean sucesso = false;
		try {
			em.merge(transacao);
			em.merge(iteracaoCanal);
			if (listaTarefas != null) {
				for (Mtxtb015SrvcoTrnsoTrfa mtxtb015SrvcoTrnsoTrfa : listaTarefas) {
					em.persist(mtxtb015SrvcoTrnsoTrfa);
				}
			}
			em.persist(transacaoAgendamento);
			em.flush();
			sucesso = true;
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("finalizando atualizarDadosTransacaoAgendamento");
		return sucesso;
	}
	
	@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
	public boolean atualizarDadosTransacaoEfetivaPagamentoAgendado(Mtxtb014Transacao transacao, List<Mtxtb015SrvcoTrnsoTrfa> listaTarefas,
			Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb034TransacaoAgendamento agendamento, Mtxtb035TransacaoConta transacaoConta) {
		logger.debug("inicializando atualizarDadosTransacaoEfetivaPagamentoAgendado");
		boolean sucesso = false;
		try {
			em.merge(transacao);
			em.merge(iteracaoCanal);
			em.merge(agendamento);
			if (listaTarefas != null) {
				for (Mtxtb015SrvcoTrnsoTrfa mtxtb015SrvcoTrnsoTrfa : listaTarefas) {
					em.persist(mtxtb015SrvcoTrnsoTrfa);
				}
			}
			if (transacaoConta != null) {
				em.persist(transacaoConta);
			}
			em.flush();
			sucesso = true;
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("finalizando atualizarDadosTransacaoEfetivaPagamentoAgendado");
		return sucesso;
	}
}
