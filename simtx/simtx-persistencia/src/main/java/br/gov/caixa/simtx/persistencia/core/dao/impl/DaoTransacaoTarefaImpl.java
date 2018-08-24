/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoTransacaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;

@Stateless
public class DaoTransacaoTarefaImpl implements DaoTransacaoTarefa {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    
    private static final String NU_NSU_TRANSACAO = "nuNsuTransacao";
    

    @Override
    public Mtxtb015SrvcoTrnsoTrfa salvar(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
        em.persist(transacaoTarefa);
        em.flush();
        return transacaoTarefa;
    }

    @Override
    public Mtxtb015SrvcoTrnsoTrfa alterar(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
        em.merge(transacaoTarefa);
        em.flush();
        return transacaoTarefa;
    }

    @Override
    public Mtxtb015SrvcoTrnsoTrfa buscarPorFiltro(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
        return em
            .createNamedQuery("Mtxtb015SrvcoTrnsoTrfa.buscarPorFiltro", Mtxtb015SrvcoTrnsoTrfa.class)
            .setParameter("nuServico", transacaoTarefa.getId().getNuServico017())
            .setParameter("nuVersaoServico", transacaoTarefa.getId().getNuVersaoServico017())
            .setParameter("nuTarefa", transacaoTarefa.getId().getNuTarefa012())
            .setParameter("nuVersaoTarefa", transacaoTarefa.getId().getNuVersaoTarefa012())
            .setParameter(NU_NSU_TRANSACAO, transacaoTarefa.getId().getNuNsuTransacao017()).getSingleResult();
    }
    
    @Override
    public List<Mtxtb015SrvcoTrnsoTrfa> buscarTarefasPorNsu(long nsu) {
		return em.createNamedQuery("Mtxtb015SrvcoTrnsoTrfa.buscarTarefasDoServico", Mtxtb015SrvcoTrnsoTrfa.class)
		.setParameter(NU_NSU_TRANSACAO, nsu).getResultList();
    }
    
    /**
     * Buscar tarefas por nsu.
     *
     * @param nsu the nsu
     * @param nsuCorp the nsu corp
     * @return the list
     * @throws Exception the exception
     */
	public List<Mtxtb015SrvcoTrnsoTrfa> buscarTarefasPorNsu(long nsu, long nsuCorp) {
		return em.createNamedQuery("Mtxtb015SrvcoTrnsoTrfa.buscarPorNSUCorp", Mtxtb015SrvcoTrnsoTrfa.class)
				.setParameter(NU_NSU_TRANSACAO, nsu)
				.setParameter("nsuCorp", nsuCorp)
				.getResultList();

	}
    
    @Override
    public List<Mtxtb015SrvcoTrnsoTrfa> buscarPorFiltroCorp(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
		return em
            .createNamedQuery("Mtxtb015SrvcoTrnsoTrfa.buscarPorFiltroCorp", Mtxtb015SrvcoTrnsoTrfa.class)
            .setParameter("nsuCorp", transacaoTarefa.getNsuCorp())
            .setParameter(NU_NSU_TRANSACAO, transacaoTarefa.getMtxtb017VersaoSrvcoTrnso().getMtxtb014Transacao().getNuNsuTransacao())
            .getResultList();
    }
}
