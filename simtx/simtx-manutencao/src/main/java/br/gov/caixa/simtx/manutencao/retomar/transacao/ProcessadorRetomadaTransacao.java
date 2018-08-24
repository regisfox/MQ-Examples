package br.gov.caixa.simtx.manutencao.retomar.transacao;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.atualizador.dados.AtualizadorDadosTransacao;
import br.gov.caixa.simtx.persistencia.cache.agendamento.FornecedorDadosAgendamento;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb035TransacaoConta;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb036TransacaoAuditada;
import br.gov.caixa.simtx.util.ConstantesAgendamento;
import br.gov.caixa.simtx.util.DadosAuditoriaUsuario;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.exception.RetomadaException;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.gerenciador.servico.ProcessadorServicos;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class ProcessadorRetomadaTransacao extends GerenciadorTransacao implements ProcessadorServicos {
	
	public ProcessadorRetomadaTransacao() {
		super();
	}

	public ProcessadorRetomadaTransacao(AtualizadorDadosTransacao atualizadorDadosTransacao, FornecedorDadosAgendamento fornecedorDadosAgendamento, TratadorDeExcecao tratadorDeExcecao,
			ValidadorRegrasNegocio validadorRegrasNegocio, SimtxConfig simtxConfig, RepositorioArquivo repositorioArquivo) {
		super();
		this.atualizadorDadosTransacao = atualizadorDadosTransacao;
		this.fornecedorDadosAgendamento = fornecedorDadosAgendamento;
		this.tratadorDeExcecao = tratadorDeExcecao;
		this.validadorRegrasNegocio = validadorRegrasNegocio;
		this.simtxConfig = simtxConfig;
		this.repositorioArquivo = repositorioArquivo;
	}

	private static final Logger logger = Logger.getLogger(ProcessadorRetomadaTransacao.class);

	private static final long serialVersionUID = 5120954039856321840L;
	
	@Inject
	private FornecedorDadosAgendamento fornecedorDadosAgendamento;

	@Inject
	private TratadorDeExcecao tratadorDeExcecao;

	@Inject
	private SimtxConfig simtxConfig;

	@Inject
	private RepositorioArquivo repositorioArquivo;

	@Inject
	private AtualizadorDadosTransacao atualizadorDadosTransacao;
	
	private String tagInidadosMsgTransacaoTarefasResposta = "<DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>";
	
	private String tagFindadosMsgTransacaoTarefasResposta = "</DADOS_MENSAGEM_TRANSACAO_TAREFAS_RESPOSTA_SIBAR>";
	
	private DadosBarramento dadosBarramento;
	
	

	@Override
	public void processar(ParametrosAdicionais parametrosAdicionais) {
		
		logger.info(" ==== inicializando processador retomada ==== ");
		this.dadosBarramento = new DadosBarramento();
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		Mtxtb004Canal canal = null;
		Mtxtb008MeioEntrada meioEntrada = null;
		Mtxtb011VersaoServico servicoOrigem = null;
		Mtxtb014Transacao transacao = new Mtxtb014Transacao();
		Mtxtb034TransacaoAgendamento transacaoAgendamento = null;
		
		DadosAuditoriaUsuario dadosAuditoriaUsuario = null;
		boolean isUltimaExecucao = false;

			try {
				BuscadorTextoXml buscador = obtemValidaMensagemBuscador(parametrosAdicionais.getXmlMensagem());
				
				this.dadosBarramento.escrever(parametrosAdicionais.getXmlMensagem());
				
				parametrosAdicionais = new ParametrosAdicionais(dadosBarramento, null);
				
				transacao = buscarTransacaoXmlRetomada(buscador);
				
				transacaoAgendamento = obterTransacaoAgendamento(buscador);
				
				if (null != transacaoAgendamento) {
					String ehAgendamentoFinal = buscador.xpathTexto("/RETOMAR_TRANSACAO_ENTRADA/AGENDAMENTO_FINAL");
					
					isUltimaExecucao = "S".equals(ehAgendamentoFinal);	
				}
				
				iteracaoCanal = this.fornecedorDados.buscarIteracao(transacao.getNuNsuTransacao()).get(0);
				
				servicoOrigem = buscarServicoOrigemXmlRetomada(buscador);
	
				canal = buscarCanal(buscador);
			
				meioEntrada = buscarMeioEntradaOrigemXmlRetomada(buscador);

				dadosAuditoriaUsuario = buscarDadosAuditoriaUsuarioXmlRetomada(buscador);
				
				if (null != dadosAuditoriaUsuario) {
					canal = buscarCanalPorSigla(Constantes.SG_CANAL_SIMTX);		
				}

				DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(canal, meioEntrada, servicoOrigem, transacao, iteracaoCanal);
				
				String respostaSaidaBar = validaRespostaRetornoSiBar(parametrosAdicionais.getXmlMensagem());
				
				this.reprocessarServico(dadosTransacaoComuns, transacaoAgendamento, dadosAuditoriaUsuario, respostaSaidaBar, isUltimaExecucao);
				
			} catch (RetomadaException re) {
				logger.error(re);
				logger.info(" ==== Erro no processo de validacao retomada, mensagem será retirada da fila ==== ");
			} catch (ServicoException se) {
				logger.error(se.getMensagem().getDeMensagemTecnica());
				DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao, iteracaoCanal);
				parametrosAdicionais.setDadosBarramento(this.dadosBarramento);
				this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, false);	
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
				DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao, iteracaoCanal);
				parametrosAdicionais.setDadosBarramento(this.dadosBarramento);
				this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, false);	
			}
		logger.info(" ==== finalizando processador retomada ==== ");
	}
	
	private Date obtetDataAgendamento(String xmlEntradaCanal) throws RetomadaException{
		logger.info("Mensagem Entrada: " + xmlEntradaCanal);
		Date dataAgendamento = null;
		if (null!= xmlEntradaCanal && !xmlEntradaCanal.isEmpty()) {
			try {
				BuscadorTextoXml buscador = new BuscadorTextoXml(xmlEntradaCanal);
				String dataSTR = buscador.xpathTexto("/*[1]/VALIDA_BOLETO/DATA_PAGAMENTO");
				if (dataSTR.isEmpty()) {
					dataAgendamento = DataUtil.simpleDateFormat(DataUtil.FORMATO_DATA_XML).parse(dataSTR);
				}
			} catch (ParserConfigurationException | SAXException | IOException | ParseException e) {
				throw new RetomadaException("Erro no processamento da mensagem");
			}
		} else {
			throw new RetomadaException(" ==== Sem mensagem para reprocessamento ==== ");
		}
		return dataAgendamento;
	}
	

	private BuscadorTextoXml obtemValidaMensagemBuscador(String mensagemEntrada) throws RetomadaException{
		BuscadorTextoXml buscador = null;
		logger.info("Mensagem Entrada: " + mensagemEntrada);
		if (null!= mensagemEntrada && !mensagemEntrada.isEmpty()) {
			try {
				buscador = new BuscadorTextoXml(mensagemEntrada);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				throw new RetomadaException("Erro ao validar XML com XSD");
			}
		} else {
			throw new RetomadaException(" ==== Sem mensagem para reprocessamento ==== ");
		}
		return buscador;
	}
	
	private void reprocessarServico(DadosTransacaoComuns dadosTransacaoComuns, Mtxtb034TransacaoAgendamento transacaoAgendamento, DadosAuditoriaUsuario dadosAuditoriaUsuario, String respostaSaidaBar, boolean isUltimaExecucao) throws RetomadaException{
		Long idServicoOrigem = dadosTransacaoComuns.getMtxtb011VersaoServico().getId().getNuServico001();
		if (Constantes.getServicosCore().contains(idServicoOrigem)) {
			String xmlEntradaCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal().getDeRecebimento();
			if (DataUtil.dataEntradaEhDepoisDeDataHoje(obtetDataAgendamento(xmlEntradaCanal))) {
				reprocessarDadosValidaAgendamento(dadosTransacaoComuns, respostaSaidaBar);
			} else {
				reprocessarDadosCore(dadosTransacaoComuns, respostaSaidaBar);
			}
			
		} else if (Constantes.getServicosComprovantes().contains(idServicoOrigem)) {
			processarEntradaSaidaComprovante(dadosTransacaoComuns, respostaSaidaBar);

		} else if (Constantes.getServicosAgendamentosPagamentos().contains(idServicoOrigem)) {
			reprocessarDadosEfetivacaoAgendamento(dadosTransacaoComuns, transacaoAgendamento, respostaSaidaBar, isUltimaExecucao);

		} else if (Constantes.getServicosAgendamentosCancelamentoCanal().contains(idServicoOrigem)) {
			reprocessarDadosAgendamento(dadosTransacaoComuns, transacaoAgendamento, respostaSaidaBar, Constantes.AGENDAMENTO_IC_CANCELADO);

		} else if (Constantes.getServicosAgendamentos().contains(idServicoOrigem)) {
			reprocessarDadosAgendamento(dadosTransacaoComuns, respostaSaidaBar);

		} else if (Constantes.getServicosAgendamentosListaDetalhe().contains(idServicoOrigem)) {
			reprocessarDadosAgendamentoListaDetalhe(dadosTransacaoComuns, transacaoAgendamento, respostaSaidaBar);

		} else if (Constantes.getServicosCancelamentoWeb().contains(idServicoOrigem)) {
			reprocessarDadosCancelamentoAgendamento(dadosTransacaoComuns, transacaoAgendamento, respostaSaidaBar, dadosAuditoriaUsuario);

		} else if (Constantes.getServicosCancelamento().contains(idServicoOrigem)) {
			reprocessarCancelamento(dadosTransacaoComuns, respostaSaidaBar);
		}
	}
	
	
	

	/**
	 * 
	 * @param transacao
	 * @param iteracaoCanal
	 * @param canal
	 * @param servicoOrigem
	 * @param servicoAgendamento
	 * @param dadosBarramento
	 */
	private void reprocessarDadosCore(DadosTransacaoComuns dadosTransacaoComuns, String respostaSaidaBar) {
		logger.info(" ==== reprocessarDadosCore Iniciado ==== ");
		
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		Mtxtb004Canal canal = dadosTransacaoComuns.getMtxtb004Canal();
		Mtxtb008MeioEntrada meioEntrada = dadosTransacaoComuns.getMtxtb008MeioEntrada();
		Mtxtb011VersaoServico servicoOrigem = dadosTransacaoComuns.getMtxtb011VersaoServico();
		Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
		Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();
		
		String xmlSaidaCanal = null;
		
		try {
			
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, canal, meioEntrada);

			this.dadosBarramento.escrever(respostaSaidaBar);

			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoOrigem, transacao, this.dadosBarramento, respostaSaidaBar);
			this.gerenciadorTarefas.validarTarefasServicoResposta(tarefasServicoResposta);
			xmlSaidaCanal = transformarXml(servicoOrigem.getDeXsltResposta(), transacao, canal, tarefasServicoResposta.getResposta());

			logger.info("Atualizando informações das tarefas e transação");
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			String xmlEntradaCanal = iteracaoCanal.getDeRecebimento();
			Mtxtb035TransacaoConta mtxtb035TransacaoConta = montarDadosTransacaoConta(situacaoTransacao, transacao, iteracaoCanal, canal, servicoOrigem, xmlEntradaCanal);

			boolean statusAtualizacaoTransacao = atualizadorDadosTransacao.atualizarDadosTransacaoCore(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), mtxtb016IteracaoCanal, mtxtb035TransacaoConta);

			direcionaProcessadorMensagemTransacao(true, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, xmlSaidaCanal);

		} catch (ServicoException se) {
			logger.error(se.getMensagem());
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			DadosTransacaoComuns dadosTransacaoComunsTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomadaDesfazimentoTransacao(dadosTransacaoComunsTratarExcecao, se, parametrosAdicionaisRetomada, false);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			DadosTransacaoComuns dadosTransacaoComunsTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomadaDesfazimentoTransacao(dadosTransacaoComunsTratarExcecao, se, parametrosAdicionaisRetomada, false);		
		}
	}

	private void processarEntradaSaidaComprovante(DadosTransacaoComuns dadosTransacaoComuns, String respostaSaidaBar) {
		logger.info(" ==== reprocessarDadosCore Iniciado ==== ");
		
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		
		Mtxtb004Canal canal = dadosTransacaoComuns.getMtxtb004Canal();
		Mtxtb008MeioEntrada meioEntrada = dadosTransacaoComuns.getMtxtb008MeioEntrada();
		Mtxtb011VersaoServico servicoOrigem = dadosTransacaoComuns.getMtxtb011VersaoServico();
		Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
		Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();

		String xmlSaidaCanal = null;

		try {
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, canal, meioEntrada);

			this.dadosBarramento.escrever(respostaSaidaBar);

			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoOrigem, transacao, this.dadosBarramento, respostaSaidaBar);

			xmlSaidaCanal = transformarXml(servicoOrigem.getDeXsltResposta(), transacao, canal, tarefasServicoResposta.getResposta());

			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			boolean statusAtualizacaoTransacao = atualizadorDadosTransacao.atualizarDadosTransacaoTarefasIteracoes(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), mtxtb016IteracaoCanal);
			
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, xmlSaidaCanal);

		} catch (ServicoException se) {
			logger.error(se.getMensagem().getDeMensagemTecnica());
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);	
		} 
	}
	
	private void reprocessarCancelamento(DadosTransacaoComuns dadosTransacaoComuns, String respostaSaidaBar) {
		logger.info(" ==== processarEntradaSaidaCancelamento Iniciado ==== ");

		String xmlSaidaCanal = null;
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		
		Mtxtb004Canal canal = dadosTransacaoComuns.getMtxtb004Canal();
		Mtxtb008MeioEntrada meioEntrada = dadosTransacaoComuns.getMtxtb008MeioEntrada();
		Mtxtb011VersaoServico servicoOrigem = dadosTransacaoComuns.getMtxtb011VersaoServico();
		Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
		Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();

		try {
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, canal, meioEntrada);
			this.dadosBarramento.escrever(respostaSaidaBar);
			
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			Mtxtb035TransacaoConta mtxtb035TransacaoConta = buscarMtxtb035TransacaoConta(transacao.getNuNsuTransacaoPai());
			Mtxtb036TransacaoAuditada mtxtb036TransacaoAuditada = montarTransacaoAuditada(transacao, canal, transacao.getNuNsuTransacao(), servicoOrigem);
			mtxtb035TransacaoConta.setIcSituacao(2L);
			
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoCancelamento(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), mtxtb016IteracaoCanal, mtxtb035TransacaoConta, mtxtb036TransacaoAuditada);
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, xmlSaidaCanal);
		} catch (ServicoException se) {
			logger.error(se.getMensagem(), se);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomadaDesfazimentoTransacao(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomadaDesfazimentoTransacao(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);		
		}
	}
	
	private void reprocessarDadosAgendamentoListaDetalhe(DadosTransacaoComuns dadosTransacaoComuns, Mtxtb034TransacaoAgendamento transacaoAgendamento, String respostaSaidaBar) {
		logger.info(" ==== reprocessarDadosAgendamentoListaDetalhe ==== ");
		
		String xmlSaidaCanal = null;
		
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		
		Mtxtb004Canal canal = dadosTransacaoComuns.getMtxtb004Canal();
		Mtxtb008MeioEntrada meioEntrada = dadosTransacaoComuns.getMtxtb008MeioEntrada();
		Mtxtb011VersaoServico servicoOrigem = dadosTransacaoComuns.getMtxtb011VersaoServico();
		Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
		Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();
	
		try {
			
			listaTarefas = this.fornecedorDados.buscarTarefasMeioEntrada(meioEntrada.getNuMeioEntrada(), servicoOrigem.getId().getNuServico001(), servicoOrigem.getId().getNuVersaoServico());
			
			if (null == transacaoAgendamento) {
				List<Mtxtb003ServicoTarefa> listaTarefasNegociais = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem   , canal, null);
				listaTarefas.addAll(listaTarefasNegociais);
			}

			this.dadosBarramento.escrever(respostaSaidaBar);

			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoOrigem, transacao, this.dadosBarramento, respostaSaidaBar);
			
			this.gerenciadorTarefas.validarTarefasServicoResposta(tarefasServicoResposta);
			
			xmlSaidaCanal = transformarXml(servicoOrigem.getDeXsltResposta(), transacao, canal, tarefasServicoResposta.getResposta());

			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoTarefasIteracoes(transacao,
					tarefasServicoResposta.getListaTransacaoTarefas(), 
					mtxtb016IteracaoCanal);
			
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, respostaSaidaBar);
			
		} catch (ServicoException se) {
			logger.error(se.getMensagem().getDeMensagemTecnica());
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(servicoOrigem, transacao, null, iteracaoCanal);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);	
			
		} catch (Exception e) {
			
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(servicoOrigem, transacao, null, iteracaoCanal);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);
		} 
	}
	
	private void reprocessarDadosCancelamentoAgendamento(DadosTransacaoComuns dadosTransacaoComuns, Mtxtb034TransacaoAgendamento transacaoAgendamento, String respostaSaidaBar, DadosAuditoriaUsuario dadosAuditoriaUsuario) {
		logger.info(" ==== processarEntradaSaidaCancelamentoAgendamento Iniciado ==== ");
		String xmlSaidaCanal = null;
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		
		Mtxtb004Canal canal = dadosTransacaoComuns.getMtxtb004Canal();
		Mtxtb011VersaoServico servicoOrigem = dadosTransacaoComuns.getMtxtb011VersaoServico();
		Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
		Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();

		try {
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, canal, null);

			this.dadosBarramento.escrever(respostaSaidaBar);

			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoOrigem, transacao, this.dadosBarramento, respostaSaidaBar);
			this.gerenciadorTarefas.validarTarefasServicoResposta(tarefasServicoResposta);
			xmlSaidaCanal = transformarXml(servicoOrigem.getDeXsltResposta(), transacao, canal, tarefasServicoResposta.getResposta());

			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			transacaoAgendamento.setIcSituacao(Constantes.AGENDAMENTO_IC_CANCELADO);
			Mtxtb036TransacaoAuditada mtxtb036TransacaoAuditada = montarTransacaoAuditada(servicoOrigem, transacao.getNuNsuTransacao(), dadosAuditoriaUsuario, transacaoAgendamento);
			
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoCancelamentoAgendamento(transacao,
					tarefasServicoResposta.getListaTransacaoTarefas(), 
					mtxtb016IteracaoCanal,
					transacaoAgendamento, 
					mtxtb036TransacaoAuditada);
			
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, respostaSaidaBar);
		} catch (ServicoException se) {
			logger.error(se.getMensagem(), se);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomadaDesfazimentoTransacao(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);		
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomadaDesfazimentoTransacao(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);
		}
	}
	
	private void reprocessarDadosAgendamento(DadosTransacaoComuns dadosTransacaoComuns, String respostaSaidaBar) {
		
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		Mtxtb004Canal canal = dadosTransacaoComuns.getMtxtb004Canal();
		Mtxtb011VersaoServico servicoOrigem = dadosTransacaoComuns.getMtxtb011VersaoServico();
		Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
		Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();
		
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		
		String xmlSaidaCanal = null;

		try {
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, canal, null);

			this.dadosBarramento.escrever(respostaSaidaBar);

			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoOrigem, transacao, this.dadosBarramento, respostaSaidaBar);
			this.gerenciadorTarefas.validarTarefasServicoResposta(tarefasServicoResposta);
			xmlSaidaCanal = transformarXml(servicoOrigem.getDeXsltResposta(), transacao, canal, tarefasServicoResposta.getResposta());

			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			Mtxtb034TransacaoAgendamento mtxtb034TransacaoAgendamento = montarTransacaoAgendamento(transacao, servicoOrigem, canal, mtxtb016IteracaoCanal);
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoAgendamento(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), mtxtb016IteracaoCanal, mtxtb034TransacaoAgendamento);
			
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, respostaSaidaBar);
			
		} catch (ServicoException se) {
			logger.error(se.getMensagem(), se);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			DadosTransacaoComuns dadosTratarExecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTratarExecao, se, parametrosAdicionaisRetomada, false);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTratarExecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTratarExecao, se, parametrosAdicionaisRetomada, false);
		}
	}
	
	private void reprocessarDadosEfetivacaoAgendamento(DadosTransacaoComuns dadosTransacaoComuns, Mtxtb034TransacaoAgendamento transacaoAgendamento, String respostaSaidaBar, boolean isUltimaExecucao) {
		logger.info(" ==== Inicializando reprocessarDadosEfetivacaoAgendamento ==== ");
		
		String xmlSaidaCanal = null;
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		String xmlEntrada = null;
		Mtxtb004Canal canalSimtx = buscarCanalSIMTX();
		Mtxtb011VersaoServico servicoOrigem = dadosTransacaoComuns.getMtxtb011VersaoServico();
		Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
		Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();
		try {
			
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, canalSimtx, null);
			xmlEntrada = iteracaoCanal.getDeRecebimento();
			this.dadosBarramento.escrever(xmlEntrada);
			this.dadosBarramento.escrever(respostaSaidaBar);
			this.dadosBarramento = montarDadosXmlInformacoesAgendamento(transacaoAgendamento, isUltimaExecucao, dadosBarramento);
			
			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasRespostaSemValidacao(listaTarefas, servicoOrigem, transacao, dadosBarramento);
			tarefasServicoResposta.setResposta(gerenciadorTarefas.validarCodigoImpeditivoAgendamento(transacao, listaTarefas, this.dadosBarramento, respostaSaidaBar, isUltimaExecucao));			
			
			xmlSaidaCanal = transformarXml(servicoOrigem.getDeXsltResposta(), transacao, canalSimtx, tarefasServicoResposta.getResposta());
			
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			iteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			transacaoAgendamento.setIcSituacao(ConstantesAgendamento.SITUACAO_EFETIVADO);
			
			Mtxtb035TransacaoConta transacaoConta = montarDadosTransacaoConta(situacaoTransacao, transacao, iteracaoCanal, canalSimtx, servicoOrigem, xmlEntrada);
			
			boolean statusAtualizacaoTransacao = atualizadorDadosTransacao.atualizarDadosTransacaoEfetivaPagamentoAgendado(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal, transacaoAgendamento, transacaoConta);
			
			direcionaProcessadorMensagemTransacao(true, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, respostaSaidaBar);
			
		} catch (ServicoException se) {
			logger.error(se.getMensagem(), se);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			tratadorDeExcecao.tratarExcecaoEfetivaAgendamentoPagamentoRetomadaDesfazimento(dadosTratarExcecao, transacaoAgendamento, se, parametrosAdicionaisRetomada, isUltimaExecucao, false);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			tratadorDeExcecao.tratarExcecaoEfetivaAgendamentoPagamentoRetomadaDesfazimento(dadosTratarExcecao, transacaoAgendamento, se, parametrosAdicionaisRetomada, isUltimaExecucao, false);
		}
		
		logger.info(" ==== Finicializando reprocessarDadosEfetivacaoAgendamento ==== ");
	}
	
	private void reprocessarDadosAgendamento(DadosTransacaoComuns dadosTransacaoComuns, Mtxtb034TransacaoAgendamento transacaoAgendamento, String respostaSaidaBar, int icSituacao) {
		logger.info(" ==== Inicializando reprocessarDadosAgendamento ==== ");
		String xmlSaidaCanal = null;
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
	
		Mtxtb004Canal canal = dadosTransacaoComuns.getMtxtb004Canal();
		Mtxtb011VersaoServico servicoOrigem = dadosTransacaoComuns.getMtxtb011VersaoServico();
		Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
		Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();

		try {
			
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, canal, null);

			this.dadosBarramento.escrever(respostaSaidaBar);

			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoOrigem, transacao, this.dadosBarramento, respostaSaidaBar);
			this.gerenciadorTarefas.validarTarefasServicoResposta(tarefasServicoResposta);
			xmlSaidaCanal = transformarXml(servicoOrigem.getDeXsltResposta(), transacao, canal, tarefasServicoResposta.getResposta());

			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			iteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			transacaoAgendamento.setIcSituacao(icSituacao);
			
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoAgendamento(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal, transacaoAgendamento);
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, respostaSaidaBar);
			
		} catch (ServicoException se) {
			logger.error(se.getMensagem(), se);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(listaTarefas, servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);
		}
		logger.info(" ==== Finalizando reprocessarDadosAgendamento ==== ");
	}
	
	private void reprocessarDadosValidaAgendamento(DadosTransacaoComuns dadosTransacaoComuns, String respostaSaidaBar) {
		logger.info(" ==== inicializando reprocessarDadosValidaAgendamento ==== ");

		String xmlSaidaCanal = null;
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		Mtxtb004Canal canal = dadosTransacaoComuns.getMtxtb004Canal();
		Mtxtb008MeioEntrada meioEntrada = dadosTransacaoComuns.getMtxtb008MeioEntrada();
		Mtxtb011VersaoServico servicoOrigem = dadosTransacaoComuns.getMtxtb011VersaoServico();
		Mtxtb014Transacao transacao = dadosTransacaoComuns.getMtxtb014Transacao();
		Mtxtb016IteracaoCanal iteracaoCanal = dadosTransacaoComuns.getMtxtb016IteracaoCanal();
		
		try {
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, canal, meioEntrada);
			listaTarefas = this.gerenciadorTarefas.adicionarTarefaCalculaBoletoParaListaTarefas(servicoOrigem, listaTarefas);
			
			this.dadosBarramento.escrever(respostaSaidaBar);

			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoOrigem, transacao, this.dadosBarramento, respostaSaidaBar);

			xmlSaidaCanal = transformarXml(servicoOrigem.getDeXsltResposta(), transacao, canal, tarefasServicoResposta.getResposta());

			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			boolean statusAtualizacaoTransacao = atualizadorDadosTransacao.atualizarDadosTransacaoTarefasIteracoes(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), mtxtb016IteracaoCanal);
			
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, tarefasServicoResposta.getXmlSaidaSibar());

		} catch (ServicoException se) {
			logger.error(se.getMensagem().getDeMensagemTecnica());
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(servicoOrigem, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTratarExcecao = new DadosTransacaoComuns(servicoOrigem, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			ParametrosAdicionais parametrosAdicionaisRetomada = new ParametrosAdicionais(dadosBarramento, xmlSaidaCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTratarExcecao, se, parametrosAdicionaisRetomada, false);	
		}
		logger.info(" ==== finalizando reprocessarDadosValidaAgendamento ==== ");
	}
	
	private Mtxtb036TransacaoAuditada montarTransacaoAuditada(Mtxtb011VersaoServico servicoOrigem,
			Long nsuTransacao,  DadosAuditoriaUsuario dadosAuditoriaUsuario, Mtxtb034TransacaoAgendamento agendamento) {
		logger.info("montando dados para Mtxtb036TransacaoAuditada");

		Mtxtb036TransacaoAuditada transacaoAuditada = new Mtxtb036TransacaoAuditada();
		transacaoAuditada.setNuTransacaoAuditada(nsuTransacao);
		transacaoAuditada.setNuServico(servicoOrigem.getMtxtb001Servico().getNuServico());
		transacaoAuditada.setNuVersaoServico(servicoOrigem.getId().getNuVersaoServico());
		transacaoAuditada.setNuTransacaoOrigem(agendamento.getNuNsuTransacaoAgendamento());
		
		transacaoAuditada.setNuCanalOrigem(agendamento.getNuCanal());
		transacaoAuditada.setNuServicoOrigem(agendamento.getNuServico());
		transacaoAuditada.setNuVersaoServicoOrigem(agendamento.getNuVersaoServico());

		transacaoAuditada.setCoUsuario(dadosAuditoriaUsuario.getIdUsuario());
		transacaoAuditada.setCoMaquinaInclusao(dadosAuditoriaUsuario.getIpUsuarioRequisicao());

		transacaoAuditada.setTsInclusao(DataUtil.getDataAtual());

		return transacaoAuditada;
	}

	/**
	 * Transforma o xml em outro a partir do xslt.
	 * 
	 * @param xslt
	 * @param transacao
	 * @param canal
	 * @param mensagem
	 * @return
	 * @throws ServicoException
	 */
	private String transformarXml(String xslt, Mtxtb014Transacao transacao, Mtxtb004Canal canal, Resposta mensagem) throws ServicoException {
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl("nsuSimtx", String.valueOf(transacao.getNuNsuTransacao())));
			parametrosNovos.add(new ParametroXsl("pRedeTransmissora", String.valueOf(canal.getNuRedeTransmissora())));
			if (mensagem != null) {
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigo()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, mensagem.getOrigem()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL, mensagem.getMensagemNegocial()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getMensagemTecnica()));
			}
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];

			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl, parametrosNovos.toArray(pArr));
		} catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	private DadosBarramento montarDadosXmlInformacoesAgendamento(Mtxtb034TransacaoAgendamento transacaoAgendamento, boolean isUltimaExecucao, DadosBarramento dadosBarramento) {
		dadosBarramento.escrever("<NSU_AGENDAMENTO>"+transacaoAgendamento.getNuNsuTransacaoAgendamento()+"</NSU_AGENDAMENTO>");
		String agendamentoFinal = isUltimaExecucao?"S":"N";
		dadosBarramento.escrever("<AGENDAMENTO_FINAL>"+agendamentoFinal+"</AGENDAMENTO_FINAL>");
		
		return dadosBarramento;
	}
	
	private String validaRespostaRetornoSiBar(String mensagemEntrada) throws ServicoException {
		String respostaSaidaBar = null;
		int tamtag = tagInidadosMsgTransacaoTarefasResposta.length();
		if (mensagemEntrada.contains(tagInidadosMsgTransacaoTarefasResposta)) {
			respostaSaidaBar = mensagemEntrada.substring(
					mensagemEntrada.indexOf(tagInidadosMsgTransacaoTarefasResposta) + tamtag,
					mensagemEntrada.indexOf(tagFindadosMsgTransacaoTarefasResposta));
		}
		if (null == respostaSaidaBar) {
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
		
		return respostaSaidaBar;
	}
	
	private Mtxtb034TransacaoAgendamento obterTransacaoAgendamento(BuscadorTextoXml buscador) {
		Mtxtb034TransacaoAgendamento transacaoAgendamento = null;
		Long nsuAgendamento = null;
		
		String nsuAgendamentoSTR = buscador.xpathTexto("/RETOMAR_TRANSACAO_ENTRADA/NSUMTX_AGENDAMENTO");
		
		if (null != nsuAgendamentoSTR && !nsuAgendamentoSTR.isEmpty()) {
			nsuAgendamento = Long.valueOf(nsuAgendamentoSTR);
		}
		
		if (null != nsuAgendamento) {
			transacaoAgendamento = new Mtxtb034TransacaoAgendamento();	
			transacaoAgendamento.setNuNsuTransacaoAgendamento(nsuAgendamento);
			transacaoAgendamento = fornecedorDadosAgendamento.buscaTransacaoAgendamentoPorPK(transacaoAgendamento);
			logger.info("nsu transacao agendamento:" + nsuAgendamento);
		}
		
		return transacaoAgendamento;
	}
	
	private Mtxtb014Transacao buscarTransacaoXmlRetomada(BuscadorTextoXml buscador) throws Exception {
		Mtxtb014Transacao transacao = null;
		String nsuOrigemSTR = buscador.xpathTexto("/RETOMAR_TRANSACAO_ENTRADA/NSU_TRANSACAO");
		Long nsuOrigem;
		if (!nsuOrigemSTR.isEmpty()) {
			nsuOrigem = Long.parseLong(nsuOrigemSTR);
			transacao = new Mtxtb014Transacao();
			transacao.setNuNsuTransacao(nsuOrigem);
			logger.info("nsu transacao:" + nsuOrigem);
			transacao = this.fornecedorDados.buscarTransacaoPorPK(transacao);	
		}else {
			throw new RetomadaException("Nsu Transação não localizado" + nsuOrigemSTR);
		}
		
		return transacao;
		
	}
	
	private Mtxtb011VersaoServico buscarServicoOrigemXmlRetomada(BuscadorTextoXml buscador) throws ServicoException {
		String codigoServicoSTR = buscador.xpathTexto("/RETOMAR_TRANSACAO_ENTRADA/CODIGO_SERVICO");
		String versaoServicoSTR = buscador.xpathTexto("/RETOMAR_TRANSACAO_ENTRADA/VERSAO_SERVICO");
		
		return obterDadosServico(codigoServicoSTR, versaoServicoSTR);
	}
	
	
	private Mtxtb011VersaoServico obterDadosServico(String codigoServicoSTR, String versaoServicoSTR) throws ServicoException {
		Long codigoServico;
		Integer versaoServico;
		if (!codigoServicoSTR.isEmpty() && !versaoServicoSTR.isEmpty()) {
			codigoServico = Long.parseLong(codigoServicoSTR);
			versaoServico = Integer.parseInt(versaoServicoSTR);
		}else {
			throw new RetomadaException("Cancelamento Mensagem Retomada - Servico Não Informado");
		}
		return buscarServico(codigoServico, versaoServico);
	}

	private Mtxtb008MeioEntrada buscarMeioEntradaOrigemXmlRetomada(BuscadorTextoXml buscador){
		String meioEntradaSTR = buscador.xpathTexto("/RETOMAR_TRANSACAO_ENTRADA/HEADER/MEIOENTRADA");
		int nuMeioEntrada = meioEntradaSTR.isEmpty() ? 0 : Integer.valueOf(meioEntradaSTR);
		
		return this.fornecedorDados.buscarMeioEntrada(nuMeioEntrada);
	}
	
	private DadosAuditoriaUsuario buscarDadosAuditoriaUsuarioXmlRetomada(BuscadorTextoXml buscador) {
		DadosAuditoriaUsuario dadosAuditoriaUsuario = null;
		
		String idUsuario = buscador.xpathTexto("/RETOMAR_TRANSACAO_ENTRADA/CODIGO_USUARIO");
		String ipUsuarioRequisicao = buscador.xpathTexto("/RETOMAR_TRANSACAO_ENTRADA/CODIGO_MAQUINA");
		
			if (!idUsuario.isEmpty()) {
				dadosAuditoriaUsuario = new DadosAuditoriaUsuario(idUsuario, ipUsuarioRequisicao);
			}
		
		return dadosAuditoriaUsuario;
	}

	public void setValidadorRegrasNegocio(ValidadorRegrasNegocio validadorRegrasNegocio) {
		this.validadorRegrasNegocio = validadorRegrasNegocio;
	}
}
