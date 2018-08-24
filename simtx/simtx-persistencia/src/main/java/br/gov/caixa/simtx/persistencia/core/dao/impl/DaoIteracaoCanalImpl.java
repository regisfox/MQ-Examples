/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.gov.caixa.simtx.persistencia.core.dao.DaoIteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
 
@Stateless
public class DaoIteracaoCanalImpl implements DaoIteracaoCanal {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    
    
    private static final String SELECT_TRANSACAO = " select ic from Mtxtb016IteracaoCanal IC, Mtxtb014Transacao T ";
    private static final String WHERE_TRANSACAO = " WHERE IC.mtxtb014Transacao.nuNsuTransacao = T.nuNsuTransacao ";

    @Override
    public Mtxtb016IteracaoCanal salvar(Mtxtb016IteracaoCanal iteracaoCanal) {
        try {
            em.persist(iteracaoCanal);
            em.flush();
        }
        catch (Exception e) {
            iteracaoCanal = null;
        }
        return iteracaoCanal;
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public void alterar(Mtxtb016IteracaoCanal iteracaoCanal) {
    	Query query = em.createQuery("update Mtxtb016IteracaoCanal set "
    			+ "deRetorno =:deRetorno, "
    			+ "tsRetornoSolicitacao =:tsRetornoSolicitacao "
    			+ "where id.nuIteracaoCanal =:nuIteracaoCanal and id.nuNsuTransacao014 =:nuNsuTransacao014");
		
    	query.setParameter("deRetorno", iteracaoCanal.getDeRetorno());
    	query.setParameter("tsRetornoSolicitacao", iteracaoCanal.getTsRetornoSolicitacao());
		query.setParameter("nuIteracaoCanal", iteracaoCanal.getId().getNuInteracaoCanal());
		query.setParameter("nuNsuTransacao014", iteracaoCanal.getId().getNuNsuTransacao014());
    	query.executeUpdate();
    }

    /**
     * Insira aqui a descrição do método.
     *
     * @return the list
     * @see br.gov.caixa.simtx.persistencia.core.api.dao.DaoIteracaoCanal#buscarTransacoesPendentes()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Mtxtb016IteracaoCanal> buscarTransacoesPendentes(Date data) {
        List<Mtxtb016IteracaoCanal> retorno = null;
        try {
        	Query query = em.createQuery("Select ic from Mtxtb016IteracaoCanal IC, Mtxtb014Transacao T "
        			+ "WHERE IC.mtxtb014Transacao.nuNsuTransacao = T.nuNsuTransacao "
        			+ "AND T.icSituacao = 3 "
        			+ "AND trunc(T.dhMultiCanal) = trunc(:data) "
        			+ "order by IC.mtxtb004Canal.nuCanal");
			
        	query.setParameter("data", data);
            return query.getResultList();
        }
        catch (NoResultException e) {
            retorno = null;
        }
        return retorno;
    }

    @SuppressWarnings("unchecked")
    @Override
	public List<Mtxtb016IteracaoCanal> buscarTransacoesPendentesParaEnvio() {
        List<Mtxtb016IteracaoCanal> retorno = null;
        try {
            StringBuilder strB = new StringBuilder();

            strB = selectIteracaoCanal(strB);
            strB.append(" AND T.icEnvio = 0  ");
            strB.append(" order by IC.mtxtb004Canal.nuCanal ");

            Query query = em.createQuery(strB.toString());
            return query.getResultList();
        }
        catch (NoResultException e) {
            retorno = null;
        }
        return retorno;
    }
    
    
    public StringBuilder selectIteracaoCanal(StringBuilder sb){
        sb.append(SELECT_TRANSACAO);
        sb.append(WHERE_TRANSACAO);
        return sb;
    }
    
    @Override
    public Mtxtb016IteracaoCanal buscarPorPK(Mtxtb016IteracaoCanal iteracaoCanal) {
        return em.find(Mtxtb016IteracaoCanal.class, iteracaoCanal.getId());
    }
    
    @Override
    public long buscarMaxPK() {
        long codigo = em.createNamedQuery("Mtxtb016IteracaoCanal.buscarMaxPK").getFirstResult();
        codigo += 1;
        return codigo;
    }

    @Override
    public List<Mtxtb016IteracaoCanal> buscarIteracao(long nsu){
    	 StringBuilder strB = new StringBuilder();

         strB = selectIteracaoCanal(strB);
         strB.append(" and T.nuNsuTransacao = ");
         strB.append(nsu);
         String squery = strB.toString();
         TypedQuery<Mtxtb016IteracaoCanal> query = em.createQuery(squery, Mtxtb016IteracaoCanal.class);
         return query.getResultList();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mtxtb016IteracaoCanal> buscarTransacoesParaEnvioCCO(Date dataReferencia) {
        return em
            .createNamedQuery("Mtxtb016IteracaoCanal.buscarParaEnvioCCO", Mtxtb016IteracaoCanal.class)
            .setParameter("dataReferencia", dataReferencia)
            .getResultList();
    }

	@Override
	public Mtxtb016IteracaoCanal buscaUltimoMtxtb016IteracaoCanal(long nsu) {
		 StringBuilder strB = new StringBuilder(500);

         strB = selectIteracaoCanal(strB);
         strB.append(" and T.nuNsuTransacao in (select max(Taux.nuNsuTransacao) from Mtxtb014Transacao Taux where Taux.nuNsuTransacaoPai = ");
         strB.append(nsu);
         strB.append(")");
         
         final String squery = strB.toString();
         
         final TypedQuery<Mtxtb016IteracaoCanal> query = em.createQuery(squery, Mtxtb016IteracaoCanal.class);
         query.setMaxResults(1);
         
         final List<Mtxtb016IteracaoCanal> result = query.getResultList();
         
         if(result != null && !result.isEmpty()) {
        	 return result.get(0);
         }
         
         return null;
	}

}
