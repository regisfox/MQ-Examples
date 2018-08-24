/*******************************************************************************
 * Copyright (C)  2017 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.core.dao.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import br.gov.caixa.simtx.persistencia.core.dao.DaoServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefaPK;
import br.gov.caixa.simtx.persistencia.vo.TipoTarefaEnum;

@Stateless
public class DaoServicoTarefaImpl implements DaoServicoTarefa {

    @PersistenceContext(name = "simtx_dao", unitName = "simtx_dao")
    private EntityManager em;
    
    private static final String NU_SERVICO = "nuServico";
    
    private static final String NU_VERSAO_SERVICO = "nuVersaoServico";
    
    private static final String IC_SITUACAO = "icSituacao";
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mtxtb003ServicoTarefa> buscarTarefasNegocialPorServico(Mtxtb003ServicoTarefaPK servicoTarefaPK) {
        return this.em.createNamedQuery("Mtxtb003ServicoTarefa.buscarTarefasNegocial", Mtxtb003ServicoTarefa.class)
                .setParameter(NU_SERVICO, servicoTarefaPK.getNuServico011())
                .setParameter(NU_VERSAO_SERVICO, servicoTarefaPK.getNuVersaoServico011()).getResultList();
    }
    
    public List<Mtxtb003ServicoTarefa> buscarTarefasMeioEntradaPorServico(long nuMeioEntrada, long nuVersaoMeioEntrada, long nuServico, long nuVersaoServico) throws SQLException{
        List<Mtxtb003ServicoTarefa> retorno = null;
        try {
            retorno = em.createNamedQuery("Mtxtb003ServicoTarefa.buscarTarefaMeioEntrada", Mtxtb003ServicoTarefa.class)
            		.setParameter("nuMeioEntrada", nuMeioEntrada)
                    .setParameter("nuVersaoMeioEntrada", nuVersaoMeioEntrada)
                    .setParameter(NU_SERVICO, nuServico)
                    .setParameter(NU_VERSAO_SERVICO, nuVersaoServico)
                    .getResultList();
        }
        catch (NoResultException nre) {
            retorno = null;
        }
        return retorno;
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public List<Mtxtb003ServicoTarefa> buscarTarefasMeioEntradaPorServico(long nuMeioEntrada, long nuServico, long nuVersaoServico) {
    	return em.createNamedQuery("Mtxtb003ServicoTarefa.buscarTarefaMeioEntrada", Mtxtb003ServicoTarefa.class)
            		.setParameter("nuMeioEntrada", nuMeioEntrada)
                    .setParameter(NU_SERVICO, nuServico)
                    .setParameter(NU_VERSAO_SERVICO, nuVersaoServico)
                    .setParameter(IC_SITUACAO, new BigDecimal(1))
                    .getResultList();
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public List<Mtxtb003ServicoTarefa> listarTarefasServico(long nuServico, long nuVersaoServico, long nuCanal) {
    	return em.createNamedQuery("Mtxtb003ServicoTarefa.listarTarefasServico", Mtxtb003ServicoTarefa.class)
            		.setParameter(NU_SERVICO, nuServico)
                    .setParameter(NU_VERSAO_SERVICO, nuVersaoServico)
                    .setParameter("nuCanal", nuCanal)
                    .setParameter(IC_SITUACAO, new BigDecimal(1))
                    .getResultList();
    }
    
    /**
     * Insira aqui a descrição do método.
     */
    @Override
    public List<Mtxtb003ServicoTarefa> listarTarefaNegocialCanal(long nuServico, long nuVersaoServico, long nuCanal) {
    	List<Integer> icTipoTarefas = new ArrayList<>();
    	icTipoTarefas.add(TipoTarefaEnum.NEGOCIAL.getCodigo());
    	
        return em.createNamedQuery("Mtxtb003ServicoTarefa.listarTarefaNegocialCanal", Mtxtb003ServicoTarefa.class)
        		.setParameter(NU_SERVICO, nuServico)
                .setParameter(NU_VERSAO_SERVICO, nuVersaoServico)
                .setParameter("nuCanal", nuCanal)
                .setParameter("icTipoTarefas", icTipoTarefas)
                .setParameter(IC_SITUACAO, new BigDecimal(1))
                .getResultList();
    }
    

    /**
     * Insira aqui a descrição do método.
     * @see br.gov.caixa.simtx.persistencia.core.api.dao.DaoServicoTarefa#buscarTarefasPorPK(br.gov.caixa.simtx.persistencia.core.api.dataobject.Mtxtb003ServicoTarefaPK)
     */
    @Override
    public Mtxtb003ServicoTarefa buscarTarefasPorPK(Mtxtb003ServicoTarefaPK servicoTarefaPK) {
        Mtxtb003ServicoTarefa retorno = null;
        try {
            retorno = em.find(Mtxtb003ServicoTarefa.class, servicoTarefaPK);
        }
        catch (NoResultException nre) {
            retorno = null;
        }
        return retorno;
    }
}
