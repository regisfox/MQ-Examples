/*******************************************************************************
 * Copyright (C)  2017 - CAIXA EconÃƒÂ´mica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.persistencia.cache.core;


import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb002Tarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
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
 * 
 * Atraves desta interface o pmac obtem os dados que necessita para trabalhar.<br/>
 * Tais podem ser obtidos em memoria RAM ou banco de dados (DAO)
 * 
 * @author rgrosz@brq.com
 * @author diegojesus
 *
 */
@Local
public interface FornecedorDados extends Serializable {
    
    public Mtxtb002Tarefa buscarTarefaPorPK(long nuTarefa);

    public Mtxtb008MeioEntrada buscarMeioEntrada(long codModoEntrada);

    public Mtxtb023Parametro buscarParametroPorPK(long code);

    public Mtxtb001Servico buscarServicoPorPK(long nuServico);

    /**
     * Busca a VersaoTarefa pelo Id.
     * 
     * @param versaoTarefaPK
     * @return
     */
    public Mtxtb012VersaoTarefa buscarVersaoTarefaPorPK(Mtxtb012VersaoTarefaPK versaoTarefaPK);

    /**
     * Atualiza a entidade iteracaoCanal.
     * 
     * @param iteracaoCanal
     */
    public void alterarIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal);

    public Mtxtb011VersaoServico buscarVersaoServico(int nuVersaoServico, long nuServico);

    public List<Mtxtb010VrsoTarfaMeioEntra> buscarTarefasPorMeioEntrada(Mtxtb010VrsoTarfaMeioEntraPK versaoServicoPK);

    /**
     * Busca todas as tarefas negociais do servico.
     * 
     * @param nuServico
     * @param nuVersaoServico
     * @return
     */
    public List<Mtxtb003ServicoTarefa> buscarTarefasNegocialPorServico(long nuServico, long nuVersaoServico, long nuCanal);

    public List<Mtxtb020SrvcoTarfaCanal> buscarTarefasPorServicoCanal(long nuServico, long nuVersaoServico, long nuCanal);

    public Mtxtb003ServicoTarefa buscarTarefasNegocialPorPK(long nuServico, long nuVersaoServico, long nuTarefa,
        long nuVersaoTarefa);

    public void alterarTransacao(Mtxtb014Transacao transacao);

    public Mtxtb014Transacao buscarTransacaoPorPK(Mtxtb014Transacao transacao);

    public Mtxtb017VersaoSrvcoTrnso salvarTransacaoServico(Mtxtb017VersaoSrvcoTrnso transacaoServico);

    public Mtxtb017VersaoSrvcoTrnso buscarSrvcoTransacaoPorNSU(Mtxtb017VersaoSrvcoTrnso transacaoServico);

    public Mtxtb015SrvcoTrnsoTrfa salvarTransacaoTarefa(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa);
    
    public Mtxtb015SrvcoTrnsoTrfa buscarTransacaoTarefaPorFiltro(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa)
       ;

    public Mtxtb015SrvcoTrnsoTrfa atualizarTransacaoTarefa(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa);

    public List<Mtxtb016IteracaoCanal> buscarTransacoesPendentes(Date data);

    public Mtxtb004Canal buscarCanalPorPK(Mtxtb004Canal mtxtb004Canal);
    
    public void alteraSituacaoCanal(List<Mtxtb004Canal> listaCanais);

    public Mtxtb016IteracaoCanal buscarIteracaoCanalPorPK(Mtxtb016IteracaoCanal iteracaoCanalPK);
    
	public List<Mtxtb015SrvcoTrnsoTrfa> buscarTransacaoTarefaPorFiltroCorp(Mtxtb015SrvcoTrnsoTrfa transacaoTarefa);
    
    public List<Mtxtb015SrvcoTrnsoTrfa> buscarTarefasPorNsu(long nsu);
    
    public Mtxtb014Transacao buscarTransacaoPorNSUPai(Mtxtb014Transacao transacao);
    
	public Mtxtb014Transacao buscarTransacaoOrigem(Long nsu); 

    public List<Mtxtb024TarefaFila> buscarTarefasFilas(Mtxtb024TarefaFilaPK tarefaPK);

	/**
	 * Retorna a lista de informaÃƒÂ§ÃƒÂµes para limpeza.
	 * 
	 * @param diaAnterior
	 *            {@link Date} Data anterior ÃƒÂ  data atual.
	 * @return {@link List} de {@link Mtxtb022ControleEnvio}.
	 */
	public List<Mtxtb014Transacao> buscarInformacoesParaLimpeza(Date diaAnterior);
	
	/**
	 * Recupera as transacoes pendentes de Envio para o Sicco.
	 * 
	 * @param diaAnterior
	 * @return
	 */
	public List<Mtxtb014Transacao> buscarTransacoesParaEnvioSicco(Date dataAtual);
	
	/**
	 * Verifica se possui transacoe para envio ao SICCO limitado a 1 linha por
	 * questao de performance.
	 * 
	 * @param diaAnterior
	 * @return
	 * @throws Exception
	 */
	public boolean possuiInformacoesParaEnvioCCO(Date diaAnterior);
	
	/**
	 * Recupera as transacoes pendentes de Envio para o Sicco.
	 * 
	 * @param diaAnterior
	 * @return
	 */
	public List<Mtxtb016IteracaoCanal> buscarTransacoesParaEnvioCCO(Date diaAnterior);
	
	public List<Mtxtb016IteracaoCanal> buscarIteracao(long nsu);
	
	/**
	 * Remove as particoes informadas.
	 * 
	 * @param particoes
	 */
	public int limparParticoes(String particoes);
	
	public List<Mtxtb003ServicoTarefa> buscarTarefasExecutar(long nuServico, long nuVersaoServico, long nuMeioEntrada, long nuCanal);
	
    /**
	 * Obtem a mensagem de acordo com o codigo
	 * @param codigoMensagem
	 * @return Mtxtb007TarefaMensagem ou null caso nao exista
	 */
	public Mtxtb007TarefaMensagem buscarMensagemPorTarefaCodRetorno(String codigoMensagem,
			Mtxtb012VersaoTarefa versaoTarefa) throws SemResultadoException;
    
    public Mtxtb006Mensagem buscarMensagem(final MensagemRetorno mensagem);
    
    /**
     * Monta a mensagem de erro interno
     * 
     * @return
     */
    public Mtxtb006Mensagem buscarMensagemErroInterno();
    
    /**
     * Obter o nome do servico
     * @param nuServico
     * @return String representando o nome do servico
     */
    public String buscarNomeServico(Long nuServico);
    
    /**
     * Salva a transacao com o HeaderSibar. 
     * Novo Core.
     * 
     * @param ({@link Mtxtb014Transacao}
     * @return
     * @throws Exception
     */
    public Mtxtb014Transacao salvarTransacao(Mtxtb014Transacao transacao);
    
    /**
     * Salva a entidade IteracaoCanal.
     * 
     * @param iteracaoCanal
     * @throws Exception
     */
    public Mtxtb016IteracaoCanal salvarIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal);
    
    /**
     * Busca a entidade Mtxtb001Servico pelo NomeBarramento e OperacaoBarramento.
     * 
     * @param servico
     * @return
     * @throws Exception
     */
    public Mtxtb001Servico buscarServicoPorNomeOperacao(Mtxtb001Servico servico);
    
    /**
     * Busca as entidades Mtxtb011VersaoServico e Mtxtb001Servico pelo NomeBarramento e OperacaoBarramento.
     * 
     * @param versaoServico
     * @return
     * @throws Exception
     */
    public Mtxtb011VersaoServico buscarVersaoServicoPorNomeOperacao(Mtxtb011VersaoServico versaoServico);
    
    /**
     * Busca a entidade Canal pela Sigla.
     * 
     * @param canal
     * @return
     * @throws Exception
     */
    public Mtxtb004Canal buscarCanalPorSigla(Mtxtb004Canal canal);
    
    /**
     * Busca os canais mediante a sua situacao.
     * 
     * @param canal
     * @return
     * @throws Exception
     */
    public List<Mtxtb004Canal> buscarPorSituacao(BigDecimal situacao);
    
    /**
     * Busca todos os canais.
     * 
     * @param canal
     * @return
     * @throws Exception
     */
    public List<Mtxtb004Canal> buscarTodosCanais();
    
    /**
     * Busca o ServicoCanal pelo Id.
     * 
     * @param servicoCanalPK
     * @return
     * @throws Exception
     */
    public Mtxtb005ServicoCanal buscarServicoCanalPorPK(Mtxtb005ServicoCanalPK servicoCanalPK);

    /**
     * Buscar PrmtoSrvcoCanal pelo Id.
     * 
     * @param parametro
     * @return
     * @throws Exception
     */
    public Mtxtb013PrmtoSrvcoCanal buscarParametroSrvcoCanal(Mtxtb005ServicoCanalPK parametro);
    
    /**
     * Busca o MeioEntrada pelo Id.
     * 
     * @param meioEntrada
     * @return
     * @throws Exception
     */
    public Mtxtb008MeioEntrada buscarMeioEntradaPorPK(Mtxtb008MeioEntrada meioEntrada);
    
    /**
     * Buscar MeioEntrada pelo Nome.
     * 
     * @param meioEntrada
     * @return
     * @throws Exception
     */
    public Mtxtb008MeioEntrada buscarMeioEntradaPorNome(Mtxtb008MeioEntrada meioEntrada);
    
    /**
     * Busca Versao Meio Entrada Servico pelo Id.
     * 
     * @param meioEntradaServicoPK
     * @return
     * @throws Exception
     */
    public Mtxtb018VrsoMeioEntraSrvco buscarVersaoMeioEntraServcoPorPK(Mtxtb018VrsoMeioEntraSrvcoPK meioEntradaServicoPK);
    
    /**
     * Busca as tarefas de com acordo com o Servico e Canal.
     * 
     * @param servicoTarefaCanalPK
     * @return
     * @throws Exception
     */
    public List<Mtxtb020SrvcoTarfaCanal> buscarTarefasPorServicoCanal(Mtxtb020SrvcoTarfaCanalPK servicoTarefaCanalPK);
    
    /**
     * Listar as tarefas a serem executadas (sem versao Meio Entrada).
     * Novo Core.
     * 
     * @param nuServico
     * @param nuVersaoServico
     * @param nuMeioEntrada
     * @param nuCanal
     */
    public List<Mtxtb003ServicoTarefa> buscarTarefasExecutar(long nuServico, long nuVersaoServico, long nuCanal);
    
    /**
     * Busca as regras por Servico Tarefa.
     * 
     * @param regrasPK
     * @return
     * @throws Exception
     */
    public List<Mtxtb026ServicoTarefaRegras> buscarRegrasPorServicoTarefa(Mtxtb026ServicoTarefaRegrasPK regrasPK);
    
    /**
     * Busca a TarefaMensagem por Codigo de Tarefa.
     * 
     * @param tarefaMensagemPK
     * @return
     */
    public List<Mtxtb007TarefaMensagem> buscarAutorizadorasPorTarefa(Mtxtb007TarefaMensagemPK tarefaMensagemPK);
    
    /**
     * Busca todos os servicos da Marca da conta.
     * 
     * @return
     */
    public List<Mtxtb032MarcaConta> buscarMarcasPorServico(long nuServico);
    
    /**
     * Busca os serviÃ§os pelo indentificador do cancelamento
     * 
     * @param servico
     * @return
     * @throws Exception
     */
    public List<Mtxtb001Servico> buscarPorServicoCancelamento(int cancelamento);
    
    
    /**
     * Busca transacoes 
     * 
     * @param transacao
     * @return
     * @throws Exception
     */
    public List<Mtxtb035TransacaoConta> buscaTransacao(Mtxtb035TransacaoConta transacaoConta);
    
    /**
     * Recupera as tarefas de acordo com Servico e Meio Entrada.
     * 
     * @param nuMeioEntrada
     * @param nuServico
     * @param nuVersaoServico
     * @return
     */
	public List<Mtxtb003ServicoTarefa> buscarTarefasMeioEntrada(long nuMeioEntrada, long nuServico,
			long nuVersaoServico);
	
	/**
	 * Salva a entidade TransacaoConta.
	 * 
	 * @param transacaoConta
	 * @return {@link Mtxtb035TransacaoConta}
	 */
	public Mtxtb035TransacaoConta salvarTransacaoConta(Mtxtb035TransacaoConta transacaoConta);
	
	
	 /**
	  * Grava a transacao.
	  * 
	  * @param transacao
	  * @return
	  */
	 public Mtxtb036TransacaoAuditada salvarMtxtb036TransacaoAuditada(Mtxtb036TransacaoAuditada transacao);

	 public Mtxtb006Mensagem buscarMensagemPorCodigoMensagem(String codigoMensagem) throws SemResultadoException;
	 
	/**
	 * Utilizado para cancelamento de pagamento. 
	 * Atualiza para status cancelado.
	 * 
	 * @param canal
	 * @throws Exception
	 */
	public void atualizaStatusPagamento(Long nsuOrigem, Long icSituacao);
	
	
	/**
	 * Utilizado para verificacao do grupo
	 * de acesso do usuario logado no servico web.
	 * 
	 * @param grupo
	 * @return boolean
	 */
	public Mtxtb038GrupoAcesso buscaGrupoAcesso(String grupo); 
	
	/**
	 * Busca todos os serviços
	 * 
	 * @return lista de servicos
	 */
	public List<Mtxtb001Servico> buscarTodosServicos();
	
    /**
     * Atualiza a situacao do servico
     * 
     * @return lista de servicos
     * @throws Exception
     */
    public void alteraSituacaoServico(List<Mtxtb011VersaoServico> listaServicos);
    
	/**
	  * Grava a auditoria da atualizacao da situacao do servico.
	  * 
	  * @param Mtxtb029SituacaoVersaoServico
	  * @return
	  */
	public void salvaSituacaoServico(Mtxtb029SituacaoVersaoServico situacao);

	/**
	  * Grava a auditoria da atualizacao da situacao do Canal.
	  * 
	  * @param Mtxtb030SituacaoCanal
	  * @return
	  */
	public void salvaSituacaoCanal(Mtxtb030SituacaoCanal situacao);
	
	/**
	 * Busca os Parametros de Pagamento em Contingencia por Canal e Boleto.
	 * 
	 * @param prmtoPgtoCntngncia
	 * @return
	 */
	public Mtxtb040PrmtoPgtoContingencia buscarPrmtoPgtoCntngnciaPorCanal(Mtxtb040PrmtoPgtoContingenciaPK prmtoPgtoCntngncia);
	
	/**
	 * Busca todos os parametros de boletos contingencia cadastrados.
	 * 
	 *  @return Lista de todos os parametros de boletos de contingencia cadastrados.
	 */
	public List<Mtxtb040PrmtoPgtoContingencia> buscarTodosParametrosBoletosContingencia();
	
	/**
	 * Atualiza os parametros boletos de contingencia.
	 * 
	 *  @param parametros Parametros de boletos de contingencia a serem atualizados (somente altera valor limite e status).
	 */
	public void updateParametrosBoletosContingencia(List<Mtxtb040PrmtoPgtoContingencia> parametros);

	public ParametrosFatorVencimento buscarParametrosFatorVencimento() throws ParseException;
	
	public void alterarParametro(Mtxtb023Parametro mtxtb023Parametro);

	/**
	  * Grava a auditoria da atualizacao do historico de parametro boleto de contingencia.
	  * 
	  * @param parametroBoletoContingencia
	  * 
	  */
	public void salvaHistoricoParametroBoleto(Mtxtb041HistoricoPrmtoPgtoContingencia parametroBoletoContingencia);

	/**
     * Busca todas versoes servico por nome do servico.
     * 
     *  @param nomeServico Nome do servico.
     *  
     *  @throws Exception Se ocorrerem erros.
     *  
     *  @return Lista de versao servico.  
     * 
     */
    public List<Mtxtb011VersaoServico> buscarVersaoServicoPorNome(String nomeServico);
    
    /**
     * Busca todas versoes servico por situacao da versao servico.
     * 
     * @param situacaoServico Situacao da versao servico.
     * 
     * @throws Exception Se ocorrerem erros.
     * 
     * @return Lista de versao servico.
     *  
     */
    public List<Mtxtb011VersaoServico> buscarVersaoServicoPorSituacao(int situacaoVersaoServico);
    
    /**
     * Busca todas as versoes servicos.
     * 
     *  @return Lista de versao servico.
     *  
     *  @throws Exception Se ocorrer erros.
     */
    public List<Mtxtb011VersaoServico> buscarTodosVersaoServico();

	public Mtxtb035TransacaoConta buscarTransacaoContaPorNsu(Long nsuTransacaoConta);
	
	/**
     * Busca todas as tarefas.
     * 
     * @return Lista de tarefas.
     */
    public List<Mtxtb002Tarefa> findAllTarefas();
    
    /**
     * Busca tarefa mensagens por numero da tarefa.
     * 
     * @param nuTarefa Numero da tarefa.
     * 
     * @return Lista de mensagem tarefas por numero de tarefa.
     * 
     */
    public List<Mtxtb007TarefaMensagem> buscarTarefaMensagensPorNumeroTarefa(long nuTarefa);
    
    /**
     * Salva mensagem.
     * 
     *  @param mensagem Mensagem.
     */
    public void salvaMensagem(Mtxtb006Mensagem mensagem);

}
