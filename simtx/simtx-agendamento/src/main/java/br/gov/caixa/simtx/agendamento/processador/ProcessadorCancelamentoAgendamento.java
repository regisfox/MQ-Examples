package br.gov.caixa.simtx.agendamento.processador;

import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.agendamento.entidade.CancelamentoAgendamentoWebSaida;
import br.gov.caixa.simtx.agendamento.entidade.MtxCancelamentoAgendamentoWeb;
import br.gov.caixa.simtx.persistencia.agendamento.entidade.Mtxtb034TransacaoAgendamento;
import br.gov.caixa.simtx.persistencia.atualizador.dados.AtualizadorDadosTransacao;
import br.gov.caixa.simtx.persistencia.cache.agendamento.FornecedorDadosAgendamento;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb036TransacaoAuditada;
import br.gov.caixa.simtx.util.ConstantesAgendamento;
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class ProcessadorCancelamentoAgendamento extends GerenciadorTransacao implements Serializable {
	
	private static final long serialVersionUID = 7789715945073138593L;
	
	private static final Logger logger = Logger.getLogger(ProcessadorCancelamentoAgendamento.class);

	@Inject
	private TratadorDeExcecao tratadorDeExcecao;

	@Inject
	private FornecedorDadosAgendamento fornecedorDadosAgendamento;

	@Inject
	private SimtxConfig simtxConfig;
	
	@Inject
	private GerenciadorFilasMQ execucaoMq;

	@Inject
	private ValidadorRegrasNegocio validadorRegrasNegocio;
	
	@Inject
	private GeradorPassosMigrado geradorPassosMigrado;
	
	@Inject
	private RepositorioArquivo repositorioArquivo;
	
	@Inject
	private AtualizadorDadosTransacao atualizadorDadosTransacao;
	
	private DadosBarramento dadosBarramento = new DadosBarramento();

	
	public List<CancelamentoAgendamentoWebSaida> processar(List<MtxCancelamentoAgendamentoWeb> listaCancelamentos) {
		logger.info(" ==== Processo Cancelamento Agendamento Iniciado ==== ");

		List<CancelamentoAgendamentoWebSaida> retornoAgendamentos = new ArrayList<>();

		for (MtxCancelamentoAgendamentoWeb agendamentoWeb : listaCancelamentos) {
			this.dadosBarramento = new DadosBarramento();
			agendamentoWeb.getCanal().setNuCanal(Constantes.CODIGO_CANAL_SIMTX);
			agendamentoWeb.getServico().setNuServico(Constantes.CODIGO_SERVICO_CANCELAMENTO_AGENDAMENTO_SIMTX_WEB);
			agendamentoWeb.setDataEfetivacao(DataUtil.obterFormatacaoDataSTRSIBAR(agendamentoWeb.getDataEfetivacao()));
			agendamentoWeb.setDataAgendamento(DataUtil.obterFormatacaoDataSTRSIBAR(agendamentoWeb.getDataAgendamento()));
			retornoAgendamentos.add(processarCancelamento(agendamentoWeb));
		}

		logger.info(" ==== Processo Cancelamento Agendamento Finalizado ==== ");
		return retornoAgendamentos;
	}

	private CancelamentoAgendamentoWebSaida processarCancelamento(MtxCancelamentoAgendamentoWeb entrada) {
		Mtxtb011VersaoServico servicoOrigem = null;
		Mtxtb014Transacao transacao = null;
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		String xmlSaidaSibar = null;
		ParametrosAdicionais parametrosAdicionais = new ParametrosAdicionais();
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		try {
			servicoOrigem = buscarServico(entrada.getServico().getNuServico(),entrada.getServico().getNuVersaoServico());
			String xmlEntrada = carregarAgendamentoDadosBarramento(entrada);
			transacao = salvarTransacao(entrada);
			iteracaoCanal = salvarIteracaoCanal(null, xmlEntrada, transacao);
			salvarTransacaoServico(transacao, servicoOrigem);
			
			this.validadorRegrasNegocio.validarParametrosDoCanal(servicoOrigem, this.canalSimtx, xmlEntrada);
			
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, this.canalSimtx, null);
			
			logger.info("Preparando xml de requisicao para enviar ao Sibar");
			String xmlEntradaSibar = transformarXml(servicoOrigem.getDeXsltRequisicao());
			xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, servicoOrigem, listaTarefas);
			
			this.dadosBarramento.escrever(xmlEntradaSibar);
			
			xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servicoOrigem.getMtxtb001Servico());
			this.dadosBarramento.escrever(xmlSaidaSibar);
			
			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoOrigem, transacao, this.dadosBarramento, xmlSaidaSibar);
			this.gerenciadorTarefas.validarTarefasServicoResposta(tarefasServicoResposta);
			
			logger.info("Atualizando informacoes das tarefas e transacao");
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			CancelamentoAgendamentoWebSaida saida = new CancelamentoAgendamentoWebSaida(entrada.getNuNsuTransacaoAgendamento(), true);
			Mtxtb016IteracaoCanal mtxtb016IteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, saidaParaXml(saida));
			Mtxtb034TransacaoAgendamento mtxtb034TransacaoAgendamento = montarAgendamentoRepositorio(entrada);
			Mtxtb036TransacaoAuditada mtxtb036TransacaoAuditada = montarTransacaoAuditada(entrada, transacao.getNuNsuTransacao(), entrada.getCodigoMaquina(), entrada.getCodigoUsuario(), mtxtb034TransacaoAgendamento);
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoCancelamentoAgendamento(
					transacao,
					tarefasServicoResposta.getListaTransacaoTarefas(), 
					mtxtb016IteracaoCanal,
					mtxtb034TransacaoAgendamento, 
					mtxtb036TransacaoAuditada);
			
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, xmlSaidaSibar);

			return saida;
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, false);			
			return new CancelamentoAgendamentoWebSaida(entrada.getNuNsuTransacaoAgendamento(), false);
		} 
		catch(Exception e) {
			logger.error(e.getMessage(), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, false);			
			return new CancelamentoAgendamentoWebSaida(entrada.getNuNsuTransacaoAgendamento(), false);
		}
	}

	private String entradaParaXml(MtxCancelamentoAgendamentoWeb entradaWeb) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(entradaWeb.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter w = new StringWriter();
		jaxbMarshaller.marshal(entradaWeb, w);
		return w.toString();
	}
	
	private String saidaParaXml(CancelamentoAgendamentoWebSaida saida) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(CancelamentoAgendamentoWebSaida.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter w = new StringWriter();
		jaxbMarshaller.marshal(saida, w);
		return w.toString();
	}

	private String carregarAgendamentoDadosBarramento(MtxCancelamentoAgendamentoWeb entradaWeb) throws JAXBException {
		String xmlEntrada = entradaParaXml(entradaWeb);
		logger.info("XML recebido da web");
		logger.info(xmlEntrada);
		this.dadosBarramento.escrever(xmlEntrada);
		return xmlEntrada;
	}

	private Mtxtb034TransacaoAgendamento buscarAgendamento(MtxCancelamentoAgendamentoWeb cancelamentoAgendamentoWeb) {
		Mtxtb034TransacaoAgendamento transacaoAgendamento = new Mtxtb034TransacaoAgendamento();
		transacaoAgendamento.setNuNsuTransacaoAgendamento(cancelamentoAgendamentoWeb.getNuNsuTransacaoAgendamento());

		return this.fornecedorDadosAgendamento.buscaTransacaoAgendamentoPorPK(transacaoAgendamento);
	}
	
	private Mtxtb034TransacaoAgendamento montarAgendamentoRepositorio(MtxCancelamentoAgendamentoWeb entrada) {
		Mtxtb034TransacaoAgendamento agendamento = buscarAgendamento(entrada);
		agendamento.setIcSituacao(ConstantesAgendamento.SITUACAO_CANCELADO);
		return agendamento;
	}

	private Mtxtb014Transacao salvarTransacao(MtxCancelamentoAgendamentoWeb cancelamentoAgendamentoWeb)
			throws ServicoException {
		try {
			logger.info("Gravando transacao");

			Mtxtb014Transacao transacao = criarTransacao(cancelamentoAgendamentoWeb);
			transacao = this.fornecedorDados.salvarTransacao(transacao);
			logger.info("NSUMTX gerado: " + transacao.getNuNsuTransacao());
			return transacao;
		} catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.NSU_NAO_GERADO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}

	private Mtxtb014Transacao criarTransacao(MtxCancelamentoAgendamentoWeb cancelamentoAgendamentoWeb) {
		Mtxtb014Transacao transacao = new Mtxtb014Transacao();
		transacao.setCoCanalOrigem(String.valueOf(this.canalSimtx.getNuCanal()));
		transacao.setNuNsuTransacaoPai(cancelamentoAgendamentoWeb.getNuNsuTransacaoAgendamento());
		transacao.setIcSituacao(Constantes.IC_SERVICO_EM_ANDAMENTO);
		transacao.setDhMultiCanal(DataUtil.getDataAtual());
		transacao.setDtReferencia(DataUtil.getDataAtual());
		transacao.setDhTransacaoCanal(DataUtil.getDataAtual());
		transacao.setIcEnvio(BigDecimal.ZERO);
		transacao.setIcRetorno(BigDecimal.ZERO);
		transacao.setTsAtualizacao(new Date());
		return transacao;
	}

	private String transformarXml(String xslt) throws ServicoException {
		try {
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl);
		} catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem("erro.transformar.xml"), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}

	private Mtxtb036TransacaoAuditada montarTransacaoAuditada(MtxCancelamentoAgendamentoWeb cancelamentoAgendamentoWeb,
			Long nsuTransacao, String ip, String usuario, Mtxtb034TransacaoAgendamento agendamento) {
		logger.info("montando dados para Mtxtb036TransacaoAuditada");

		Mtxtb036TransacaoAuditada transacaoAuditada = new Mtxtb036TransacaoAuditada();
		transacaoAuditada.setNuTransacaoAuditada(nsuTransacao);
		transacaoAuditada.setNuServico(cancelamentoAgendamentoWeb.getServico().getNuServico());
		transacaoAuditada.setNuVersaoServico(cancelamentoAgendamentoWeb.getServico().getNuVersaoServico());
		transacaoAuditada.setNuTransacaoOrigem(cancelamentoAgendamentoWeb.getNuNsuTransacaoAgendamento());
		
		transacaoAuditada.setNuCanalOrigem(agendamento.getNuCanal());
		transacaoAuditada.setNuServicoOrigem(agendamento.getNuServico());
		transacaoAuditada.setNuVersaoServicoOrigem(agendamento.getNuVersaoServico());

		transacaoAuditada.setCoUsuario(usuario);
		transacaoAuditada.setCoMaquinaInclusao(ip);

		transacaoAuditada.setTsInclusao(DataUtil.getDataAtual());

		return transacaoAuditada;
	}
}
