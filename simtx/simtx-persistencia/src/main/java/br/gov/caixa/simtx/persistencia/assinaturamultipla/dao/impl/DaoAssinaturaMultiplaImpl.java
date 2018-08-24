/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.assinaturamultipla.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.assinaturamultipla.dao.DaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb027TransacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb028ControleAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.vo.SituacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;

@Stateless
public class DaoAssinaturaMultiplaImpl implements DaoAssinaturaMultipla {

	@PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
	private EntityManager em;
	
	private static final String UNIDADE = "unidade";
	
	private static final String PRODUTO = "produto";
	
	private static final String CONTA = "numeroConta";
	
	private static final String DV = "dv";
	
	private static final String TIPO_CONTA = "tipoConta";
	
	private static final String SITUACAO = "situacao";
	
	
    public Mtxtb027TransacaoAssinaturaMultipla salvar(Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla) {
        try {
            em.merge(assinaturaMultipla);
            em.flush();
        }
        catch (Exception e) {
            assinaturaMultipla = null;
        }
        return assinaturaMultipla;
    }
    
    public Mtxtb028ControleAssinaturaMultipla salvar(Mtxtb028ControleAssinaturaMultipla assinatura) {
        try {
            em.merge(assinatura);
            em.flush();
        }
        catch (Exception e) {
        	assinatura = null;
        }
        return assinatura;
    }
	
    public Mtxtb027TransacaoAssinaturaMultipla buscarAssinaturaMultipla(long nsu) throws SemResultadoException {
    	try {
    		return em.find(Mtxtb027TransacaoAssinaturaMultipla.class, nsu);
		} 
    	catch (NoResultException e) {
    		throw new SemResultadoException();
		}
    }

	public List<Mtxtb027TransacaoAssinaturaMultipla> buscarAssinaturasMultiplasPendentes(int unidade, int operacao,
			long numeroConta, int dv, int tipoConta) {
		return em.createNamedQuery("Mtxtb027TransacaoAssinaturaMultipla.buscarAssinaturasMultiplasPendentes", Mtxtb027TransacaoAssinaturaMultipla.class)
			.setParameter(UNIDADE, unidade)
			.setParameter(PRODUTO, operacao)
			.setParameter(CONTA, numeroConta)
			.setParameter(DV, dv)
			.setParameter(TIPO_CONTA, tipoConta)
			.setParameter(SITUACAO, SituacaoAssinaturaMultipla.PENDENTE_ASSINATURA.getRotulo())
			.getResultList();
	}

	@Override
	public List<Mtxtb027TransacaoAssinaturaMultipla> buscarAssinaturasMultiplasPendentesNaoRelacionadas(int unidade,
			int operacao, long numero, int dv, int indicadorTipoConta, List<Long> nsuListBarramento) {
		return em.createNamedQuery("Mtxtb027TransacaoAssinaturaMultipla.buscarAssinaturasMultiplasPendentesNaoRelacionadas", Mtxtb027TransacaoAssinaturaMultipla.class)
				.setParameter(UNIDADE, unidade)
				.setParameter(PRODUTO, operacao)
				.setParameter(CONTA, numero)
				.setParameter(DV, dv)
				.setParameter(TIPO_CONTA, indicadorTipoConta)
				.setParameter(SITUACAO, SituacaoAssinaturaMultipla.PENDENTE_ASSINATURA.getRotulo())
				.setParameter("nsuList", nsuListBarramento.isEmpty() ? 0L : nsuListBarramento)
				.getResultList();
	}

	
}
