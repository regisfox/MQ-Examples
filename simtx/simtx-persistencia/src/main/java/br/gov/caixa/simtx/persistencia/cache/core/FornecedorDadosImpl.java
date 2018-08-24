/*******************************************************************************
 * Copyright (C)  2017 - CAIXA EconÃƒÂ´mica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.cache.core;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.dao.DaoCanal;
import br.gov.caixa.simtx.persistencia.core.dao.DaoGrupoAcesso;
import br.gov.caixa.simtx.persistencia.core.dao.DaoHistoricoPrmtoPgtoCntngncia;
import br.gov.caixa.simtx.persistencia.core.dao.DaoIteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.dao.DaoMeioEntrada;
import br.gov.caixa.simtx.persistencia.core.dao.DaoMeioEntradaServico;
import br.gov.caixa.simtx.persistencia.core.dao.DaoMensagem;
import br.gov.caixa.simtx.persistencia.core.dao.DaoParammetro;
import br.gov.caixa.simtx.persistencia.core.dao.DaoPrmtoPgtoCntngncia;
import br.gov.caixa.simtx.persistencia.core.dao.DaoPrmtoSrvcoCanal;
import br.gov.caixa.simtx.persistencia.core.dao.DaoServico;
import br.gov.caixa.simtx.persistencia.core.dao.DaoServicoCanal;
import br.gov.caixa.simtx.persistencia.core.dao.DaoServicoMarcas;
import br.gov.caixa.simtx.persistencia.core.dao.DaoServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.dao.DaoServicoTarefaCanal;
import br.gov.caixa.simtx.persistencia.core.dao.DaoServicoTarefaRegras;
import br.gov.caixa.simtx.persistencia.core.dao.DaoServicoTransacao;
import br.gov.caixa.simtx.persistencia.core.dao.DaoSituacaoCanal;
import br.gov.caixa.simtx.persistencia.core.dao.DaoSituacaoServico;
import br.gov.caixa.simtx.persistencia.core.dao.DaoTarefa;
import br.gov.caixa.simtx.persistencia.core.dao.DaoTarefaFila;
import br.gov.caixa.simtx.persistencia.core.dao.DaoTarefaMensagem;
import br.gov.caixa.simtx.persistencia.core.dao.DaoTransacao;
import br.gov.caixa.simtx.persistencia.core.dao.DaoTransacaoAuditada;
import br.gov.caixa.simtx.persistencia.core.dao.DaoTransacaoConta;
import br.gov.caixa.simtx.persistencia.core.dao.DaoTransacaoTarefa;
import br.gov.caixa.simtx.persistencia.core.dao.DaoVersaoServico;
import br.gov.caixa.simtx.persistencia.core.dao.DaoVersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.dao.DaoVersaoTarefaMeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb002Tarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb005ServicoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb005ServicoCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagemPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntra;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb010VrsoTarfaMeioEntraPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServicoPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb013PrmtoSrvcoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnso;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvco;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvcoPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb020SrvcoTarfaCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb020SrvcoTarfaCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFila;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFilaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegras;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegrasPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb029SituacaoVersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb030SituacaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb032MarcaConta;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb036TransacaoAuditada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb038GrupoAcesso;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingencia;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb040PrmtoPgtoContingenciaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb041HistoricoPrmtoPgtoContingencia;
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;
import br.gov.caixa.simtx.persistencia.vo.ParametrosFatorVencimento;


/**
 * Prove a infra-estrutura transacional para os daos necessarios para o {@link FornecedorDados}
 * executar sua funcionalidade.
 * 
 * @author rgrosz@brq.com
 * @author diegojesus
 * 
 * @since 19/dez/2013
 * @since 13/10/2015
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class FornecedorDadosImpl implements FornecedorDados {

    private static final long serialVersionUID = 1L;

    /** The dao tarefa. */
    @Inject
    private DaoTarefa daoTarefa;

    /** The dao meio entrada. */
    @Inject
    private DaoMeioEntrada daoMeioEntrada;

    /** The dao servico. */
    @Inject
    private DaoServico daoServico;

    /** The dao canal. */
    @Inject
    private DaoCanal daoCanal;

    /** The dao iteracao canal. */
    @Inject
    private DaoIteracaoCanal daoIteracaoCanal;

    /** The dao servico canal. */
    @Inject
    private DaoServicoCanal daoServicoCanal;

    /** The dao mensagem. */
    @Inject
    private DaoMensagem daoMensagem;

    /** The dao parametro. */
    @Inject
    private DaoPrmtoSrvcoCanal daoPrmtoSrvcoCanal;

    /** The dao versao servico. */
    @Inject
    private DaoVersaoServico daoVersaoServico;

    /** The dao transacao. */
    @Inject
    private DaoTransacao daoTransacao;

    /** The dao meio entrada servico. */
    @Inject
    private DaoMeioEntradaServico daoMeioEntradaServico;

    /** The dao versao tarefa. */
    @Inject
    private DaoVersaoTarefa daoVersaoTarefa;

    /** The dao versao tarefa meio entrada. */
    @Inject
    private DaoVersaoTarefaMeioEntrada daoVersaoTarefaMeioEntrada;

    /** The dao servico tarefa. */
    @Inject
    private DaoServicoTarefa daoServicoTarefa;

    /** The dao servico tarefa canal. */
    @Inject
    private DaoServicoTarefaCanal daoServicoTarefaCanal;

    /** The dao versao servico transacao. */
    @Inject
    private DaoServicoTransacao daoVersaoServicoTransacao;

    /** The dao transacao tarefa. */
    @Inject
    private DaoTransacaoTarefa daoTransacaoTarefa;

    /** The dao fila corporativo. */
    @Inject
    private DaoTarefaFila daoTarefaFila;

    /** The dao pmt. */
    @Inject
    private DaoParammetro daoParammetro;
    
    @Inject
    private DaoServicoTarefaRegras daoServicoTarefaRegras;
    
    /** DaoTarefaMensagem */
    @Inject
    private DaoTarefaMensagem daoTarefaMensagem;
    
    @Inject
    private DaoServicoMarcas daoServicoMarcas;
    
    @Inject
    private DaoTransacaoConta daoTransacaoConta;
    
    @Inject
    private DaoTransacaoAuditada daoTransacaoAuditada;
    
    @Inject
    private DaoGrupoAcesso daoGrupoAcesso;

    @Inject
    private DaoSituacaoServico daoSituacaoServico;
    
    @Inject
    private DaoSituacaoCanal daoSituacaoCanal;
    
    @Inject
    private DaoPrmtoPgtoCntngncia daoPrmtoPgtoCntngncia;
    
    @Inject
    private DaoHistoricoPrmtoPgtoCntngncia daoHistoricoPrmtoPgtoCntngncia;
    

    
    
    /**
     * Insira aqui a descricao do metodo.
     *
     * @param nuTarefa the nu tarefa
     * @return the mtxtb 002 tarefa
     * @throws Exception the exception
     */
    public Mtxtb002Tarefa buscarTarefaPorPK(final long nuTarefa) {
        final Mtxtb002Tarefa tarefa = new Mtxtb002Tarefa();
        tarefa.setNuTarefa(nuTarefa);
        return this.daoTarefa.buscarPorPK(tarefa);
    }

    public Mtxtb023Parametro buscarParametroPorPK(final long code) {
        return this.daoParammetro.loadParam(code);
    }

    /**
     * Obtem a mensagem de acordo com o codigo.
     *
     * @param codigoMensagem the codigo mensagem
     * @return Mtxtb006Mensagem ou null caso nao exista
     * @throws SQLException the SQL exception
     */
	public Mtxtb007TarefaMensagem buscarMensagemPorTarefaCodRetorno(String codigoMensagem,
			Mtxtb012VersaoTarefa versaoTarefa) throws SemResultadoException {
		return this.daoTarefaMensagem.buscarPorTarefaCodRetorno(codigoMensagem, versaoTarefa);
    }
    
    public Mtxtb006Mensagem buscarMensagem(final MensagemRetorno mensagemRetorno) {
    	if(MensagemRetorno.ERRO_INTERNO.equals(mensagemRetorno)) {
    		return buscarMensagemErroInterno();
    	}
    	
    	try {
    		return this.daoMensagem.findbyNsu(mensagemRetorno.getCodigo());
		} 
    	catch (SemResultadoException e) {
			return buscarMensagemErroInterno();
		}
    }

    /**
     * {@inheritDoc}
     */
	public Mtxtb006Mensagem buscarMensagemErroInterno() {
		Mtxtb006Mensagem mensagem = new Mtxtb006Mensagem();
		mensagem.setCodigoRetorno(Constantes.CODIGO_ERRO_DESCONHECIDO);
		mensagem.setCoMensagem(Constantes.CODIGO_ERRO_DESCONHECIDO);
		mensagem.setDeMensagemNegocial("TRANSACAO NEGADA");
		mensagem.setDeMensagemTecnica(Constantes.MENSAGEM_ERRO_DESCONHECIDO);
		mensagem.setIcTipoMensagem(AcaoRetorno.IMPEDITIVA.getTipo());
		return mensagem;
	}

    public Mtxtb006Mensagem buscarMensagemPorCodigoMensagem(String codigoMensagem) throws SemResultadoException {
		return this.daoMensagem.findbyCodigoMensagem(codigoMensagem);
    }

    /**
     * Insira aqui a descricao do metodo.
     *
     * @param nuMeioEntrada the nu meio entrada
     * @return the mtxtb 008 meio entrada
     * @throws Exception the exception
     * @see br.gov.caixa.simtx.repositorio.FornecedorDados#buscarMeioEntrada(int)
     */
    public Mtxtb008MeioEntrada buscarMeioEntrada(long nuMeioEntrada) {
        Mtxtb008MeioEntrada meioEntrada = new Mtxtb008MeioEntrada();
        meioEntrada.setNuMeioEntrada(nuMeioEntrada);
        return daoMeioEntrada.buscarPorPK(meioEntrada);
    }

    /**
     * Insira aqui a descricao do metodo.
     *
     * @param nuServico the nu servico
     * @return the mtxtb 001 servico
     * @throws Exception the exception
     */
    public Mtxtb001Servico buscarServicoPorPK(long nuServico) {
        Mtxtb001Servico servico = new Mtxtb001Servico();
        servico.setNuServico(nuServico);
        return daoServico.buscarPorPK(servico);
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public Mtxtb012VersaoTarefa buscarVersaoTarefaPorPK(Mtxtb012VersaoTarefaPK versaoTarefaPK) {
		return daoVersaoTarefa.buscarPorPK(versaoTarefaPK);
	}

    /**
     * Insira aqui a descricao do metodo.
     *
     * @param transacao the transacao
     * @return the mtxtb 014 transacao
     * @throws Exception the exception
     */
	public Mtxtb014Transacao buscarTransacaoPorPK(Mtxtb014Transacao transacao) {
		return daoTransacao.buscarPorPK(transacao);
	}
    
	/**
	 * buscarTransacaoPorNSUPai
	 * 
	 **/
	public Mtxtb014Transacao buscarTransacaoPorNSUPai(Mtxtb014Transacao transacao) {
        return daoTransacao.buscarPorNSUPAI(transacao);
	}
	
	public Mtxtb014Transacao buscarTransacaoOrigem(Long nsu)  {
        return daoTransacao.buscarTransacaoOrigem(nsu);
	}

    /**
     * Insira aqui a descricao do metodo.
     *
     * @param nuVersaoServico the nu versao servico
     * @param nuServico the nu servico
     * @return the mtxtb 011 versao servico
     * @throws Exception the exception
     */
    public Mtxtb011VersaoServico buscarVersaoServico(int nuVersaoServico,
                    long nuServico) {
        Mtxtb011VersaoServicoPK versaoServicoPK = new Mtxtb011VersaoServicoPK();
        versaoServicoPK.setNuServico001(nuServico);
        versaoServicoPK.setNuVersaoServico(nuVersaoServico);
        return daoVersaoServico.buscarPorPK(versaoServicoPK);
    }

    /**
     * {@inheritDoc}
     */
    public void alterarIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal) {
    	this.daoIteracaoCanal.alterar(iteracaoCanal);
    }

    /**
     * Insira aqui a descricao do metodo.
     *
     * @param nuMeioEntrada the nu meio entrada
     * @param nuVersaoMeioEntrada the nu versao meio entrada
     * @return the list
     * @throws Exception the exception
     */
    public List<Mtxtb010VrsoTarfaMeioEntra> buscarTarefasPorMeioEntrada(Mtxtb010VrsoTarfaMeioEntraPK versaoServicoPK) {
        return this.daoVersaoTarefaMeioEntrada.buscarTarefasMeioEntradaPorPK(versaoServicoPK);
    }

    /**
     * Insira aqui a descricao do metodo.
     *
     * @param nuServico the nu servico
     * @param nuVersaoServico the nu versao servico
     * @return the list
     * @throws Exception the exception
     */
    @Override
	public List<Mtxtb003ServicoTarefa> buscarTarefasNegocialPorServico(long nuServico, long nuVersaoServico, long nuCanal) {
        Mtxtb003ServicoTarefaPK versaoServicoPK = new Mtxtb003ServicoTarefaPK();
        versaoServicoPK.setNuServico011(nuServico);
        versaoServicoPK.setNuVersaoServico011(nuVersaoServico);
        return daoServicoTarefa.listarTarefaNegocialCanal(nuServico, nuVersaoServico, nuCanal);
    }
    

    /**
     * Listar as tarefas a serem executadas
     * @param nuServico
     * @param nuVersaoServico
     * @param nuMeioEntrada
     * @param nuVersaoMeioEntrada
     * @param nuCanal
     */
	public List<Mtxtb003ServicoTarefa> buscarTarefasExecutar(long nuServico, long nuVersaoServico, long nuMeioEntrada,
			long nuCanal) {
        
    	List<Mtxtb003ServicoTarefa> listaTarefasExecutar = new ArrayList<>(); 
    	
    	// OBTER AS TAREFAS NEGOCIAIS E SEGURANCA
    	Mtxtb003ServicoTarefaPK versaoServicoPK = new Mtxtb003ServicoTarefaPK();
        versaoServicoPK.setNuServico011(nuServico);
        versaoServicoPK.setNuVersaoServico011(nuVersaoServico);
		List<Mtxtb003ServicoTarefa> listaServicoTarefaNegocial = this.daoServicoTarefa
				.listarTarefaNegocialCanal(nuServico, nuVersaoServico, nuCanal);
    	if(listaServicoTarefaNegocial != null){
    		listaTarefasExecutar.addAll(listaServicoTarefaNegocial);
    	}
        
        // OBTER AS TAREFAS DE MEIO DE ENTRADA
		List<Mtxtb003ServicoTarefa> listaServicoTarefaEntrada = this.daoServicoTarefa
				.buscarTarefasMeioEntradaPorServico(nuMeioEntrada, nuServico, nuVersaoServico);
    	
    	if(listaServicoTarefaEntrada != null){
    		listaTarefasExecutar.addAll(listaServicoTarefaEntrada);
    	}
        
    	// ORDENA A LISTA
        Collections.sort(listaTarefasExecutar, new Comparator<Mtxtb003ServicoTarefa>() {

			@Override
			public int compare(Mtxtb003ServicoTarefa o1, Mtxtb003ServicoTarefa o2) {
				return o1.getNuSequenciaExecucao().compareTo(o2.getNuSequenciaExecucao());
			}
		});
        
        return listaTarefasExecutar;
    }
    
    /**
     * Listar as tarefas a serem executadas (sem versao Meio Entrada).
     * Novo Core.
     * 
     * @param nuServico
     * @param nuVersaoServico
     * @param nuMeioEntrada
     * @param nuCanal
     */
	public List<Mtxtb003ServicoTarefa> buscarTarefasExecutar(long nuServico, long nuVersaoServico,
			long nuCanal) {
        
    	List<Mtxtb003ServicoTarefa> listaTarefasExecutar = new ArrayList<>(); 
    	
		Mtxtb003ServicoTarefaPK versaoServicoPK = new Mtxtb003ServicoTarefaPK();
		versaoServicoPK.setNuServico011(nuServico);
		versaoServicoPK.setNuVersaoServico011(nuVersaoServico);
		
		List<Mtxtb003ServicoTarefa> listarTarefasServico = this.daoServicoTarefa
				.listarTarefasServico(nuServico, nuVersaoServico, nuCanal);
		
		if (listarTarefasServico != null) {
			listaTarefasExecutar.addAll(listarTarefasServico);
		}
    	
        
        /** ORDENA A LISTA */
        Collections.sort(listaTarefasExecutar, new Comparator<Mtxtb003ServicoTarefa>() {
			@Override
			public int compare(Mtxtb003ServicoTarefa o1, Mtxtb003ServicoTarefa o2) {
				return o1.getNuSequenciaExecucao().compareTo(o2.getNuSequenciaExecucao());
			}
		});
        return listaTarefasExecutar;
    }


    /**
     * Insira aqui a descricao do metodo.
     *
     * @param nuServico the nu servico
     * @param nuVersaoServico the nu versao servico
     * @param nuCanal the nu canal
     * @return the list
     * @throws Exception the exception
     */
    @Override
	public List<Mtxtb020SrvcoTarfaCanal> buscarTarefasPorServicoCanal(long nuServico, long nuVersaoServico,
			long nuCanal) {
        Mtxtb020SrvcoTarfaCanalPK servicoTarefaCanalPK = new Mtxtb020SrvcoTarfaCanalPK();
        servicoTarefaCanalPK.setNuServico003(nuServico);
        servicoTarefaCanalPK.setNuVersaoServico003(nuVersaoServico);
        servicoTarefaCanalPK.setNuCanal004(nuCanal);
        return daoServicoTarefaCanal.buscarTarefasPorServicoCanal(servicoTarefaCanalPK);
    }

    /**
     * Insira aqui a descricao do metodo.
     *
     * @param nuServico the nu servico
     * @param nuVersaoServico the nu versao servico
     * @param nuTarefa the nu tarefa
     * @param nuVersaoTarefa the nu versao tarefa
     * @return the mtxtb 003 servico tarefa
     * @throws Exception the exception
     */
    @Override
	public Mtxtb003ServicoTarefa buscarTarefasNegocialPorPK(long nuServico, long nuVersaoServico, long nuTarefa,
			long nuVersaoTarefa) {
        Mtxtb003ServicoTarefaPK servicoTarefaPK = new Mtxtb003ServicoTarefaPK();
        servicoTarefaPK.setNuServico011(nuServico);
        servicoTarefaPK.setNuVersaoServico011(nuVersaoServico);
        servicoTarefaPK.setNuTarefa012(nuTarefa);
        servicoTarefaPK.setNuVersaoTarefa012(nuVersaoTarefa);
        return daoServicoTarefa.buscarTarefasPorPK(servicoTarefaPK);
    }

    public void alterarTransacao(Mtxtb014Transacao transacao) {
        daoTransacao.alterar(transacao);
    }

    public Mtxtb017VersaoSrvcoTrnso salvarTransacaoServico(Mtxtb017VersaoSrvcoTrnso versaoServicoTransacao) {
        return daoVersaoServicoTransacao.salvar(versaoServicoTransacao);
    }

	public Mtxtb017VersaoSrvcoTrnso buscarSrvcoTransacaoPorNSU(Mtxtb017VersaoSrvcoTrnso versaoServicoTransacao) {
		return daoVersaoServicoTransacao.buscarPorNSU(versaoServicoTransacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Mtxtb015SrvcoTrnsoTrfa salvarTransacaoTarefa(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
        return daoTransacaoTarefa.salvar(transacaoTarefa);
    }

    /**
     * Insira aqui a descricao do metodo.
     *
     * @param transacaoTarefa the transacao tarefa
     * @return the mtxtb 015 srvco trnso trfa
     */
	@Override
	public Mtxtb015SrvcoTrnsoTrfa buscarTransacaoTarefaPorFiltro(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
		return daoTransacaoTarefa.buscarPorFiltro(transacaoTarefa);
	}

    /**
     * Insira aqui a descricao do metodo.
     *
     * @param nsu the nsu
     * @return the list
     */
    public List<Mtxtb015SrvcoTrnsoTrfa> buscarTarefasPorNsu(long nsu) {
        return daoTransacaoTarefa.buscarTarefasPorNsu(nsu);
    }

    /**
     * Insira aqui a descricao do metodo.
     *
     * @param transacaoTarefa the transacao tarefa
     * @return the list
     */
	public List<Mtxtb015SrvcoTrnsoTrfa> buscarTransacaoTarefaPorFiltroCorp(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
		return daoTransacaoTarefa.buscarPorFiltroCorp(transacaoTarefa);
	}

    /**
     * Insira aqui a descricao do metodo.
     *
     * @param transacaoTarefa the transacao tarefa
     * @return the mtxtb 015 srvco trnso trfa
     */
	public Mtxtb015SrvcoTrnsoTrfa atualizarTransacaoTarefa(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa) {
        return daoTransacaoTarefa.alterar(transacaoTarefa);
    }

    public List<Mtxtb016IteracaoCanal> buscarTransacoesPendentes(Date data) {
        return daoIteracaoCanal.buscarTransacoesPendentes(data);
    }

    /**
     * Buscar transacoes pendentes para envio.
     *
     * @return the list
     */
    public List<Mtxtb016IteracaoCanal> buscarTransacoesPendentesParaEnvio() {
        return daoIteracaoCanal.buscarTransacoesPendentesParaEnvio();
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Mtxtb014Transacao> buscarInformacoesParaLimpeza(Date diaAnterior) {
        return daoTransacao.buscarInformacoesParaLimpeza(diaAnterior);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Mtxtb014Transacao> buscarTransacoesParaEnvioSicco(Date dataAtual) {
		return daoTransacao.buscarParaEnvioSicco(dataAtual);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean possuiInformacoesParaEnvioCCO(Date diaAnterior) {
		return daoTransacao.possuiInformacoesParaEnvioCCO(diaAnterior);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Mtxtb016IteracaoCanal> buscarTransacoesParaEnvioCCO(Date diaAnterior) {
		return daoIteracaoCanal.buscarTransacoesParaEnvioCCO(diaAnterior);
	}

    /**
     * {@inheritDoc}
     * @throws Exception 
     * 
     */
    @Override
    public int limparParticoes(String particoes) {
        return this.daoTransacao.limparParticoes(particoes);
	}

    @Override
    public Mtxtb004Canal buscarCanalPorPK(Mtxtb004Canal mtxtb004Canal) {
        Mtxtb004Canal canal = new Mtxtb004Canal();
        canal.setNuCanal(mtxtb004Canal.getNuCanal());
        return daoCanal.buscarPorPK(canal);
    }

	public Mtxtb016IteracaoCanal buscarIteracaoCanalPorPK(final Mtxtb016IteracaoCanal iteracaoCanalPK) {
		return this.daoIteracaoCanal.buscarPorPK(iteracaoCanalPK);
	}

    public List<Mtxtb016IteracaoCanal> buscarIteracao(final long nsu) {
        return this.daoIteracaoCanal.buscarIteracao(nsu);
	}
	
	public List<Mtxtb024TarefaFila> buscarTarefasFilas(Mtxtb024TarefaFilaPK tarefaPK) {
		return daoTarefaFila.buscarTarefasFilas(tarefaPK);
    }

    /**
     * Obter o nome do servico.
     *
     * @param nuServico the nu servico
     * @return String representando o nome do servico
     */
    public String buscarNomeServico(Long nuServico){
		return daoServico.buscarNome(nuServico);
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public Mtxtb014Transacao salvarTransacao(Mtxtb014Transacao transacao) {
		return this.daoTransacao.salvar(transacao);
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public Mtxtb016IteracaoCanal salvarIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal) {
		return this.daoIteracaoCanal.salvar(iteracaoCanal);
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public Mtxtb001Servico buscarServicoPorNomeOperacao(Mtxtb001Servico servico) {
        return this.daoServico.buscarPorNomeOperacao(servico);
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public Mtxtb011VersaoServico buscarVersaoServicoPorNomeOperacao(Mtxtb011VersaoServico versaoServico) {
        return this.daoVersaoServico.buscarPorNomeOperacao(versaoServico);
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public Mtxtb004Canal buscarCanalPorSigla(Mtxtb004Canal canal) {
        return this.daoCanal.buscarPorSigla(canal);
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public Mtxtb005ServicoCanal buscarServicoCanalPorPK(Mtxtb005ServicoCanalPK servicoCanalPK) {
    	return this.daoServicoCanal.buscarPorPK(servicoCanalPK);
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public Mtxtb013PrmtoSrvcoCanal buscarParametroSrvcoCanal(Mtxtb005ServicoCanalPK parametro) {
        return this.daoPrmtoSrvcoCanal.buscarPorServicoCanal(parametro);
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public Mtxtb008MeioEntrada buscarMeioEntradaPorPK(Mtxtb008MeioEntrada meioEntrada) {
	    return this.daoMeioEntrada.buscarPorPK(meioEntrada);
	}
    
    /**
     * {@inheritDoc}
     * 
     */
    public Mtxtb008MeioEntrada buscarMeioEntradaPorNome(Mtxtb008MeioEntrada meioEntrada) {
	    return this.daoMeioEntrada.buscarPorNome(meioEntrada);
	}
    
    /**
     * {@inheritDoc}
     * 
     */
    public Mtxtb018VrsoMeioEntraSrvco buscarVersaoMeioEntraServcoPorPK(Mtxtb018VrsoMeioEntraSrvcoPK meioEntradaServicoPK) {
        return this.daoMeioEntradaServico.buscarPorPK(meioEntradaServicoPK);
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public List<Mtxtb020SrvcoTarfaCanal> buscarTarefasPorServicoCanal(Mtxtb020SrvcoTarfaCanalPK servicoTarefaCanalPK) {
        return this.daoServicoTarefaCanal.buscarTarefasPorServicoCanal(servicoTarefaCanalPK);
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public List<Mtxtb026ServicoTarefaRegras> buscarRegrasPorServicoTarefa(Mtxtb026ServicoTarefaRegrasPK regrasPK) {
        return this.daoServicoTarefaRegras.buscarPorServicoTarefa(regrasPK);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Mtxtb007TarefaMensagem> buscarAutorizadorasPorTarefa(Mtxtb007TarefaMensagemPK tarefaMensagemPK) {
    	return this.daoTarefaMensagem.buscarAutorizadorasPorTarefa(tarefaMensagemPK);
    }

	public List<Mtxtb032MarcaConta> buscarMarcasPorServico(long nuServico) {
		return this.daoServicoMarcas.buscaServicos(nuServico);
	}

	public List<Mtxtb004Canal> buscarPorSituacao(BigDecimal situacao) {
		return this.daoCanal.buscarPorSituacao(situacao);
	}

	public List<Mtxtb001Servico> buscarPorServicoCancelamento(int cancelamento) {
		return this.daoServico.buscarPorServicoCancelamento(cancelamento);
	}

	public List<Mtxtb035TransacaoConta> buscaTransacao(Mtxtb035TransacaoConta transacaoConta) {
		return this.daoTransacaoConta.buscaTransacao(transacaoConta);
	}

	public void alteraSituacaoCanal(List<Mtxtb004Canal> listaCanais) {
		this.daoCanal.alteraSituacaoCanal(listaCanais);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<Mtxtb003ServicoTarefa> buscarTarefasMeioEntrada(long nuMeioEntrada, long nuServico,
			long nuVersaoServico) {
		return this.daoServicoTarefa.buscarTarefasMeioEntradaPorServico(nuMeioEntrada, nuServico, nuVersaoServico);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Mtxtb035TransacaoConta salvarTransacaoConta(Mtxtb035TransacaoConta transacaoConta) {
		return this.daoTransacaoConta.salvar(transacaoConta);
	}

	/**
	 * {@inheritDoc}
	 */
	public Mtxtb036TransacaoAuditada salvarMtxtb036TransacaoAuditada(Mtxtb036TransacaoAuditada transacao) {
		return this.daoTransacaoAuditada.salvarMtxtb036TransacaoAuditada(transacao);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Mtxtb004Canal> buscarTodosCanais() {
		return this.daoCanal.buscarTodosCanais();
	}

	/**
	 * {@inheritDoc}
	 */
	public void atualizaStatusPagamento(Long nsuOrigem, Long icSituacao) {
		this.daoTransacaoConta.atualizaStatusPagamento(nsuOrigem, icSituacao);
	}

	/**
	 * {@inheritDoc}
	 */
	public Mtxtb038GrupoAcesso buscaGrupoAcesso(String grupo) {
		return this.daoGrupoAcesso.buscaGrupoAcesso(grupo);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Mtxtb001Servico> buscarTodosServicos() {
		return this.daoServico.buscarTodosServicos();
	}

	/**
	 * {@inheritDoc}
	 */
	public void alteraSituacaoServico(List<Mtxtb011VersaoServico> listaServicos) {
		 this.daoVersaoServico.alteraSituacaoServico(listaServicos);
	}

	/**
	 * {@inheritDoc}
	 */
	public void salvaSituacaoServico(Mtxtb029SituacaoVersaoServico situacao) {
		this.daoSituacaoServico.salvaSituacaoServico(situacao);
	}

	/**
	 * {@inheritDoc}
	 */
	public void salvaSituacaoCanal(Mtxtb030SituacaoCanal situacao) {
		this.daoSituacaoCanal.salvaSituacaoCanal(situacao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Mtxtb040PrmtoPgtoContingencia buscarPrmtoPgtoCntngnciaPorCanal(Mtxtb040PrmtoPgtoContingenciaPK prmtoPgtoCntngncia) {
		return this.daoPrmtoPgtoCntngncia.buscarPorCanaleBoleto(prmtoPgtoCntngncia);
	}
	
	public List<Mtxtb040PrmtoPgtoContingencia> buscarTodosParametrosBoletosContingencia() {
		return this.daoPrmtoPgtoCntngncia.buscarTodosParametrosBoletosContingencia();
	}

	@Override
	public void updateParametrosBoletosContingencia(List<Mtxtb040PrmtoPgtoContingencia> parametros) {
		this.daoPrmtoPgtoCntngncia.updateParametrosBoletosContingencia(parametros);
	}
	

    public ParametrosFatorVencimento buscarParametrosFatorVencimento() throws ParseException {
    	String dataBaseAtual = this.daoParammetro.loadParam(ParametrosFatorVencimento.PMT_DATA_BASE_ATUAL).getDeConteudoParam();
    	String dataBaseNova = this.daoParammetro.loadParam(ParametrosFatorVencimento.PMT_DATA_BASE_NOVA).getDeConteudoParam();
    	String rangeVencido = this.daoParammetro.loadParam(ParametrosFatorVencimento.PMT_RANGE_VENCIDO).getDeConteudoParam();
    	String rangeAVencer = this.daoParammetro.loadParam(ParametrosFatorVencimento.PMT_RANGE_A_VENCER).getDeConteudoParam();

    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    	
    	ParametrosFatorVencimento parametros = new ParametrosFatorVencimento();
    	parametros.setDataBaseAtual(format.parse(dataBaseAtual));
    	parametros.setDataBaseNova(format.parse(dataBaseNova));
    	parametros.setRangeVencido(Integer.parseInt(rangeVencido));
    	parametros.setRangeAVencer(Integer.parseInt(rangeAVencer));
    	return parametros;
    }
    
    public void alterarParametro(Mtxtb023Parametro parametro) {
    	this.daoParammetro.alterar(parametro);
    }
    
    @Override
	public void salvaHistoricoParametroBoleto(Mtxtb041HistoricoPrmtoPgtoContingencia parametroBoletoContingencia) {
		this.daoHistoricoPrmtoPgtoCntngncia.salvaHistoricoParametroBoleto(parametroBoletoContingencia);
	}

	@Override
	public List<Mtxtb011VersaoServico> buscarVersaoServicoPorNome(String nomeServico) {
		return this.daoVersaoServico.buscarVersaoServicoPorNome(nomeServico);
	}

	@Override
	public List<Mtxtb011VersaoServico> buscarVersaoServicoPorSituacao(int situacaoVersaoServico) {
		return this.daoVersaoServico.buscarVersaoServicoPorSituacao(situacaoVersaoServico);
	}

	@Override
	public Mtxtb035TransacaoConta buscarTransacaoContaPorNsu(Long nsuTransacaoConta) {
		return this.daoTransacaoConta.buscarPorNsu(nsuTransacaoConta);
	}
	
	@Override
	public List<Mtxtb011VersaoServico> buscarTodosVersaoServico() {
		return this.daoVersaoServico.buscarTodosVersaoServico();
	}

	@Override
	public List<Mtxtb002Tarefa> findAllTarefas() {
		return this.daoTarefa.findAll();
	}

	@Override
	public List<Mtxtb007TarefaMensagem> buscarTarefaMensagensPorNumeroTarefa(long nuTarefa) {
		return this.daoTarefaMensagem.buscarTarefaMensagensPorNumeroTarefa(nuTarefa);
	}

	@Override
	public void salvaMensagem(Mtxtb006Mensagem mensagem) {
		this.daoMensagem.salvaMensagem(mensagem);
	}
}