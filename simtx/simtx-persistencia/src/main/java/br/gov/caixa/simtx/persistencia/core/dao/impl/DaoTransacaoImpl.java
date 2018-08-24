/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import br.gov.caixa.simtx.persistencia.core.dao.DaoTransacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;

@Stateless
public class DaoTransacaoImpl implements DaoTransacao {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    
    @Resource(lookup = "java:jboss/jdbc/SIMTX04")
	protected DataSource dataSource;
    
    private static final String DATA_REFERENCIA = "dataReferencia";

    private static final String NSU_PAI = "nuNsuTransacaoPai";

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Mtxtb014Transacao buscarPorPK(Mtxtb014Transacao transacao) {
        Mtxtb014Transacao retorno = null;
        try {
            retorno = em.find(Mtxtb014Transacao.class, transacao.getNuNsuTransacao());
        }
        catch (NoResultException e) {
            retorno = null;
        }
        return retorno;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mtxtb014Transacao salvar(Mtxtb014Transacao transacao) {
        em.persist(transacao);
        em.flush();
        return transacao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alterar(Mtxtb014Transacao transacao) {
    	transacao.setTsAtualizacao(new Date());

    	Query query = em.createQuery("update Mtxtb014Transacao set "
    			+ "nuNsuTransacaoPai =:nuNsuTransacaoPai, "
    			+ "coCanalOrigem =:coCanalOrigem, "
    			+ "icSituacao =:icSituacao, "
    			+ "icEnvio =:icEnvio, "
    			+ "icRetorno =:icRetorno, "
    			+ "tsAtualizacao =:tsAtualizacao, "
    			+ "dtReferencia =:dtReferencia, "
    			+ "dhMultiCanal =:dhMultiCanal, "
    			+ "dhTransacaoCanal =:dhTransacaoCanal, "
    			+ "dtContabil =:dtContabil "
    			+ "where nuNsuTransacao =:nuNsuTransacao");
		
    	query.setParameter("nuNsuTransacao", transacao.getNuNsuTransacao());
    	query.setParameter(NSU_PAI, transacao.getNuNsuTransacaoPai());
    	query.setParameter("coCanalOrigem", transacao.getCoCanalOrigem());
    	query.setParameter("icSituacao", transacao.getIcSituacao());
    	query.setParameter("icEnvio", transacao.getIcEnvio());
    	query.setParameter("icRetorno", transacao.getIcRetorno());
    	query.setParameter("tsAtualizacao", transacao.getTsAtualizacao());
    	query.setParameter("dtReferencia", transacao.getDtReferencia());
    	query.setParameter("dhMultiCanal", transacao.getDhMultiCanal());
    	query.setParameter("dhTransacaoCanal", transacao.getDhTransacaoCanal());
    	query.setParameter("dtContabil", transacao.getDtContabil());
    	query.executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Mtxtb014Transacao buscarPorNSUPAI(Mtxtb014Transacao transacao) {
    	Mtxtb014Transacao retorno = null;
    	try {
        	retorno = em
                    .createNamedQuery("Mtxtb014Transacao.buscarPorNSUPAI", Mtxtb014Transacao.class)
                    .setParameter(NSU_PAI, transacao.getNuNsuTransacaoPai())
                    .getSingleResult();
        }
    	catch (NoResultException nre) {
            retorno = null;
        }
    	return retorno;
    }
    
    @Override
    public Mtxtb014Transacao buscarTransacaoOrigem(Long nsu) {
    	try {
        	return em
                    .createNamedQuery("Mtxtb014Transacao.buscarTransacaoOrigem", Mtxtb014Transacao.class)
                    .setParameter(NSU_PAI, nsu)
                    .getSingleResult();
        }
    	catch (NoResultException nre) {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean possuiInformacoesParaEnvioCCO(Date dataReferencia) {
        boolean resposta = true;
        try {
			em.createNamedQuery("Mtxtb014Transacao.possuiEnvioCCO", Mtxtb014Transacao.class)
					.setParameter(DATA_REFERENCIA, dataReferencia)
					.setMaxResults(2)
					.getResultList();
        }
        catch (NoResultException nre) {
        	resposta = false;
        }
        catch (Exception e) {
            return false;
        }
        return resposta;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public List<Mtxtb014Transacao> buscarParaEnvioSicco(Date dataReferencia) {
		return em
            .createNamedQuery("Mtxtb014Transacao.buscarParaEnvioCCO", Mtxtb014Transacao.class)
            .setParameter(DATA_REFERENCIA, dataReferencia)
            .getResultList();
	}

    /**
     * {@inheritDoc}
     */
    @Override
	public List<Mtxtb014Transacao> buscarInformacoesParaLimpeza(Date dataReferencia) {
		return em
            .createNamedQuery("Mtxtb014Transacao.buscarInformacoesParaLimpeza", Mtxtb014Transacao.class)
            .setParameter(DATA_REFERENCIA, dataReferencia)
            .getResultList();
	}

	/**
     * {@inheritDoc}
     */
     @Override
	public int limparParticoes(String particoes) {
		String sql = "{call MTX.MTXPROC_EXPURGO(?,?)}";
		try (
				CallableStatement callableStatement = this.dataSource.getConnection().prepareCall(sql);
			)
		{
			callableStatement.setString(1, particoes);
			callableStatement.registerOutParameter(2, Types.INTEGER);
			callableStatement.executeUpdate();
			
			return callableStatement.getInt(2);
		}
		catch (SQLException e) {
			return 0;
		}
	}

}
