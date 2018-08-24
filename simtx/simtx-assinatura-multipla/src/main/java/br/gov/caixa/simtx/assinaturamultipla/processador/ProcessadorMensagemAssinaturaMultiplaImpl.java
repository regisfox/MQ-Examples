/*******************************************************************************
 * Copyright (C)  2017 - CAIXA EconÃ´mica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.assinaturamultipla.processador;

import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb027TransacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb028ControleAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.MtxtbAssinaturaPK;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.vo.SituacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.cache.assinaturamultipla.FornecedorDadosAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb016IteracaoCanalPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnso;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb017VersaoSrvcoTrnsoPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvco;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb018VrsoMeioEntraSrvcoPK;
import br.gov.caixa.simtx.persistencia.exception.SemResultadoException;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.StringUtil;
import br.gov.caixa.simtx.util.XmlValidador;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.exception.SimtxException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTarefas;
import br.gov.caixa.simtx.util.gerenciador.Mensagem;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.ValidadorMensagemCanal;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.integrador.sicco.ProcessadorEnvioSicco;
import br.gov.caixa.simtx.util.mensagem.FilaCanal;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.tarefa.TarefaUtils;
import br.gov.caixa.simtx.util.xml.BuscadorResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformaMensagemCanal;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;
import br.gov.caixa.simtx.util.xml.xsd.sibar.HeaderSibar;
import br.gov.caixa.simtx.util.xml.xsd.sibar.HeaderSibarFactory;

/**
 * 
 * Classe processadora de mensagens de assinatura multipla esta classe deve ser
 * chamada de forma remota, pois o assinatura multipla ficara em outro EAR.
 * 
 * @author cvoginski
 *
 */
@Stateful
public class ProcessadorMensagemAssinaturaMultiplaImpl implements ProcessadorMensagemAssinaturaMultipla, Serializable {
	private static final String XSL_PARAM_INIBIR_NEGOCIAL = "inibirNegocial";
	private static final long CODIGO_TAREFA_VALIDA_ASSINATURA = 100056L;

	private static final int CODIGO_MEIOENTRADA_AM = 5;

	/** Constante que representa codigo do servico valida assinatura */
	private static final String CODIGO_SERVICO_VALIDA_ASSINATURA = "110022";

	/** Constante que representa a codigo do servico lista transacoes pendentes */
	private static final String CODIGO_SERVICO_LISTA_TRANSACOES_PENDENTES = "110023";

	/** Constante que representa codigo do servico de consulta detalhe */
	private static final String CODIGO_SERVICO_CONSULTA_DETALHE = "110024";

	/** Constante que representa codigo do servico consulta detalhe */
	private static final String CODIGO_SERVICO_CANCELA_ASSINATURA_MULTIPLA = "110025";

	/**
	 * valor da tag classificacao que o SIPER devolve informando que todas
	 * assinaturas foram realizadas
	 */
	private static final String CLASSIFICACAO_ASSINATURA_FINAL = "ASSINATURA_FINAL";

	private static final long serialVersionUID = 7744549307523975709L;

	private static final Logger logger = Logger.getLogger(ProcessadorMensagemAssinaturaMultiplaImpl.class);

	/** Mensagem canal de assinatura multipla. */
	private String mensagemCanal;

	/** VersaoServico solicitado pelo canal */
	protected Mtxtb011VersaoServico versaoServicoChamado;

	/** Transforma mensagem canal */
	protected TransformaMensagemCanal transformaMensagemCanal;

	/** Transacao. */
	protected Mtxtb014Transacao transacao;

	/** Fornecedor dados. */
	@Inject
	protected FornecedorDados fornecedorDados;

	/** Servico solicitado pelo canal */
	protected Mtxtb001Servico servicoChamado;

	/** SimtxConfig */
	protected SimtxConfig simtxConfig;

	/** Valida a mensagem do canal */
	protected ValidadorMensagemCanal validadorMensagemCanal;

	/** Arquivode propriedades */
	protected Properties properties;

	/** Canal dono da requisicao */
	private Mtxtb004Canal canal;

	/** Id da fila. */
	private String idMensagem;

	/** Iteracao Canal */
	protected Mtxtb016IteracaoCanal iteracaoCanal;

	/** Tratador de excecoes. */
	protected TratadorDeExcecao tratadorDeExcecao;

	/** Repositorio de Mensagens */
	@Inject
	private Mensagem mensagemServidor;

	/** Repositorio de dados do assinatura Multipla */
	@Inject
	private FornecedorDadosAssinaturaMultipla fornecedorDadosAssinaturaMultipla;

	@Inject
	protected ProcessadorEnvioSicco processadorEnvioSicco;

	private DadosBarramento dadosBarramento;

	@Inject
	private GerenciadorTarefas gerenciadorTarefas;

	@Inject
	private GerenciadorFilasMQ execucaoMq;
	
	@Inject
	private GeradorPassosMigrado geradorPassosMigrado;
	
	private static String logAssinaturaMultiplaFinalizado = " ==== Processo Assinatura Multipla finalizado ==== ";
	private static String logAssinaturaMultiplaIniciado   = " ==== Processo Assinatura Multipla inicializado ==== ";

	
	/**
	 * Metodo principal que direciona as transacoes que envolvem o assinatura
	 * multipla para seu respectivo processamento
	 * 
	 */
	@Override
	public void processarMensagemMQ() {
		try {
			logger.info(logAssinaturaMultiplaIniciado);

			this.tratadorDeExcecao = new TratadorDeExcecao(this.dadosBarramento);

			switch (String.valueOf(this.servicoChamado.getNuServico())) {
			case CODIGO_SERVICO_VALIDA_ASSINATURA:
				processarTransacaoAssinatura();
				break;
			case CODIGO_SERVICO_LISTA_TRANSACOES_PENDENTES:
				processarConsultaLista();
				break;
			case CODIGO_SERVICO_CONSULTA_DETALHE:
				processarConsultaDetalhe();
				break;
			case CODIGO_SERVICO_CANCELA_ASSINATURA_MULTIPLA:
				processarCancelamentoAssinaturaMultipla();
				break;
			default:
				processarTransacaoPrimeiraAssinaturaMultipla();
				break;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void processarCancelamentoAssinaturaMultipla() {
		try {
			this.dadosBarramento = new DadosBarramento();
			Mtxtb008MeioEntrada meioEntrada = null;
			this.transformaMensagemCanal = criarTransformadorMensagemCanal(meioEntrada);
			this.transformaMensagemCanal.prepararBusData(this.mensagemCanal);

			meioEntrada = recuperarMeioEntradaCadastroAssinatura(meioEntrada);
			this.transformaMensagemCanal.setMeioEntrada(meioEntrada);
			
			List<Mtxtb003ServicoTarefa> tarefas = 
					gerenciadorTarefas.carregarTarefasServico(this.versaoServicoChamado, this.canal, null);

			String xmlEntradaSibar = montarXmlServico();
			xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, this.versaoServicoChamado, tarefas);
			String xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, this.servicoChamado);

			this.transformaMensagemCanal.getDadosBarramento().escrever(xmlSaidaSibar);

			this.gerenciadorTarefas.salvarTarefasServico(tarefas, this.versaoServicoChamado, this.transacao,
					this.transformaMensagemCanal.getDadosBarramento(), xmlSaidaSibar);
			
			BuscadorTextoXml buscadorSaidaSibar = new BuscadorTextoXml(xmlSaidaSibar);
			BuscadorResposta buscadorResposta = new BuscadorResposta();
			
			Resposta controleNegocial = buscadorResposta.buscarRespostaBarramento(buscadorSaidaSibar, 
					tarefas.get(0).getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoServicoBarramento());

			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagemPorTarefaCodRetorno(controleNegocial.getCodigo(), 
					TarefaUtils.filtrarServicoTarefaNegocial(tarefas).get(0).getMtxtb012VersaoTarefa()).getMtxtb006Mensagem();
			
			if (mensagem.isImpeditiva()) {
				throw new ServicoException(mensagem, controleNegocial.getOrigem());
			}

			String transacaoAssinaturaMultipla = this.transformaMensagemCanal.getDadosBarramento()
					.xpathTexto("/BUSDATA/*[2]/NSU_ORIGEM");

			Mtxtb006Mensagem mensagemRetorno = this.fornecedorDados.buscarMensagem(MensagemRetorno.SUCESSO);

			String respostaCanal = this.transformaMensagemCanal.preparaXmlRespostaComXslt(this.versaoServicoChamado.getDeXsltResposta(),
					String.valueOf(this.transacao.getNuNsuTransacao()), 
					Constantes.ORIGEM_SIMTX,
					mensagemRetorno);
					
			Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla = this.fornecedorDadosAssinaturaMultipla
					.buscarAssinaturaMultipla(Long.valueOf(transacaoAssinaturaMultipla));

			assinaturaMultipla.setSituacao(SituacaoAssinaturaMultipla.CANCELADA);
			this.fornecedorDadosAssinaturaMultipla.salvar(assinaturaMultipla);

			try {
				atualizaTransacao(Constantes.IC_SERVICO_FINALIZADO);
				atualizaIteracaoCanal(respostaCanal);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			responder(respostaCanal);

			this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Mtxtb006Mensagem mensagemRetorno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			try {
				String respostaCanal = this.transformaMensagemCanal
						.preparaXmlRespostaComXslt(this.versaoServicoChamado.getDeXsltResposta(), 
								Long.toString(this.transacao.getNuNsuTransacao()),
								Constantes.ORIGEM_SIMTX, mensagemRetorno,
								new ParametroXsl("erro", "true"));

				responder(respostaCanal);
				this.transacao.negarSituacao();
				this.fornecedorDados.alterarTransacao(this.transacao);
				atualizaIteracaoCanal(respostaCanal);

			} catch (Exception e2) {
				logger.error(e2.getMessage(), e2);
			}

		}
	}

	private void processarConsultaLista() {
		try {
			Mtxtb008MeioEntrada meioEntrada = null;
			this.dadosBarramento = new DadosBarramento();
			this.transformaMensagemCanal = criarTransformadorMensagemCanal(meioEntrada);
			this.transformaMensagemCanal.prepararBusData(this.mensagemCanal);
			meioEntrada = recuperarMeioEntradaCadastroAssinatura(meioEntrada);
			this.transformaMensagemCanal.setMeioEntrada(meioEntrada);

			List<Mtxtb003ServicoTarefa> tarefas = 
					gerenciadorTarefas.carregarTarefasServico(this.versaoServicoChamado, this.canal, null);
			
			String xmlEntradaSibar = montarXmlServico();
			xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, this.versaoServicoChamado, tarefas);
			String xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, this.servicoChamado);

			this.transformaMensagemCanal.getDadosBarramento().escrever(xmlSaidaSibar);

			this.gerenciadorTarefas.salvarTarefasServico(tarefas, this.versaoServicoChamado, this.transacao,
					this.transformaMensagemCanal.getDadosBarramento(), xmlSaidaSibar);

			BuscadorTextoXml buscadorSaidaSibar = new BuscadorTextoXml(xmlSaidaSibar);

			String codigoRetorno = buscadorSaidaSibar.xpathTexto("/*[1]/COD_RETORNO");

			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagemPorTarefaCodRetorno(codigoRetorno,
							TarefaUtils.filtrarServicoTarefaNegocial(tarefas).get(0).getMtxtb012VersaoTarefa())
					.getMtxtb006Mensagem();
			
			if (mensagem.isImpeditiva()) {
				throw new ServicoException(mensagem, buscadorSaidaSibar.xpathTexto("/*[1]/ORIGEM_RETORNO/text()"));
			}
			
			Node nodeTransacoes = buscadorSaidaSibar.xpath("/*[1]/CONSULTA_ASSINATURA_ELETRONICA_SAIDA/TRANSACOES");

			montarTagTransacoes(nodeTransacoes, xmlEntradaSibar);

			String respostaCanal = this.transformaMensagemCanal.preparaXmlRespostaComXslt(
					this.versaoServicoChamado.getDeXsltResposta(), 
					Long.toString(this.transacao.getNuNsuTransacao()), 
					this.transformaMensagemCanal.getDadosBarramento().xpathTexto("/BUSDATA/RESPOSTA/CONSULTA_ASSINATURA_ELETRONICA/ORIGEM_RETORNO/text()"), 
					mensagem);

			try {
				atualizaTransacao(Constantes.IC_SERVICO_FINALIZADO);
				atualizaIteracaoCanal(respostaCanal);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			responder(respostaCanal);
			this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
			
		} catch (ServicoException se) {
			logger.error(se.getMensagem().getDeMensagemTecnica(), se);

			try {
				String respostaCanal = prepararMensagemRespostaErro(this.versaoServicoChamado.getDeXsltResposta(),
						se.getMensagem(), se.getSistemaOrigem());

				responder(respostaCanal);
				this.transacao.negarSituacao();
				this.fornecedorDados.alterarTransacao(this.transacao);
				atualizaIteracaoCanal(respostaCanal);
				this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			try {
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				
				String respostaCanal = prepararMensagemRespostaErro(this.versaoServicoChamado.getDeXsltResposta(),
						mensagemErroInterno, Constantes.ORIGEM_SIMTX);
				responder(respostaCanal);

				this.transacao.negarSituacao();
				this.fornecedorDados.alterarTransacao(this.transacao);
				atualizaIteracaoCanal(respostaCanal);

			} catch (Exception e2) {
				logger.error(e2.getMessage(), e2);
			}
		}
	}

	private void processarConsultaDetalhe() {
		this.dadosBarramento = new DadosBarramento();
		Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla = null;

		try {
			this.transformaMensagemCanal = criarTransformadorMensagemCanal(null);
			this.transformaMensagemCanal.prepararBusData(this.mensagemCanal);

			List<Mtxtb003ServicoTarefa> tarefas = 
					gerenciadorTarefas.carregarTarefasServico(this.versaoServicoChamado, this.canal, null);
			
			this.gerenciadorTarefas.salvarTarefasServicoSemValidar(tarefas, this.versaoServicoChamado, this.transacao,
					this.transformaMensagemCanal.getDadosBarramento());

			long nsuTransacaoSimtx = Long
					.parseLong(this.transformaMensagemCanal.getDadosBarramento().xpathTexto("/BUSDATA/*[2]/NSU_MTX"));

			assinaturaMultipla = this.fornecedorDadosAssinaturaMultipla.buscarAssinaturaMultipla(nsuTransacaoSimtx);
			
			if (assinaturaMultipla == null) {
				logger.warn(mensagemServidor.recuperarMensagem("erro.transacao.assinatura.naoencontrado"));
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
			}

		} catch (ServicoException se) {
			logger.error(se.getMensagem().getDeMensagemTecnica(), se);

			try {
				String respostaCanal = prepararMensagemRespostaErro(this.versaoServicoChamado.getDeXsltResposta(),
						se.getMensagem(), se.getSistemaOrigem());

				responder(respostaCanal);
				this.transacao.negarSituacao();
				this.fornecedorDados.alterarTransacao(this.transacao);
				atualizaIteracaoCanal(respostaCanal);
				this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			try {
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				String respostaCanal = prepararMensagemRespostaErro(this.versaoServicoChamado.getDeXsltResposta(),
						mensagemErroInterno, Constantes.ORIGEM_SIMTX);
				responder(respostaCanal);

				this.transacao.negarSituacao();
				this.fornecedorDados.alterarTransacao(this.transacao);
				atualizaIteracaoCanal(respostaCanal);
				this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);

			} catch (Exception e2) {
				logger.error(e2.getMessage(), e2);
			}
			return;
		}

		try {
			String xml = assinaturaMultipla.getXmlNegocial().replaceAll("<\\?.*\\?>", "");
			this.transformaMensagemCanal.getDadosBarramento().escrever("<ORIGINAL>" + xml + "</ORIGINAL>");

			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.SUCESSO);
			
			String respostaCanal = this.transformaMensagemCanal.preparaXmlRespostaComXslt(
					this.versaoServicoChamado.getDeXsltResposta(), 
					Long.toString(assinaturaMultipla.getNsuAssinaturaMultipla()), 
					Constantes.ORIGEM_SIMTX, 
					mensagem, 
					new ParametroXsl("nsuSiper", Long.toString(assinaturaMultipla.getNsuPermissao())),
					new ParametroXsl("dataTransacao", assinaturaMultipla.getDataPermissao().toString()),
					new ParametroXsl("dataPrevistaEfetivacao", assinaturaMultipla.getDataEfetivacao().toString()),
					new ParametroXsl("nsuAssinaturaMultipla", Long.toString(assinaturaMultipla.getNsuAssinaturaMultipla())));
			
			responder(respostaCanal);

			atualizaTransacao(Constantes.IC_SERVICO_FINALIZADO);
			this.fornecedorDados.alterarTransacao(this.transacao);
			atualizaIteracaoCanal(respostaCanal);

			this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
			
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem().getDeMensagemTecnica(), se);

			try {
				String respostaCanal = this.transformaMensagemCanal.preparaXmlRespostaComXslt(
						this.versaoServicoChamado.getDeXsltResposta(), 
						Long.toString(this.transacao.getNuNsuTransacao()),
						Constantes.ORIGEM_SIMTX,
						se.getMensagem(), 
						new ParametroXsl("erro", "true"),
						new ParametroXsl("nsu", Long.toString(this.transacao.getNuNsuTransacao())),
						new ParametroXsl("nsuSiper", Long.toString(assinaturaMultipla.getNsuPermissao())),
						new ParametroXsl("dataSiper", assinaturaMultipla.getDataPermissao().toString()),
						new ParametroXsl("dataPrevistaEfetivacao", assinaturaMultipla.getDataEfetivacao().toString()),
						new ParametroXsl("nsuAssinaturaMultipla",Long.toString(assinaturaMultipla.getNsuAssinaturaMultipla())),
						new ParametroXsl("tipoTransacao", ""));
						
				responder(respostaCanal);
				this.transacao.negarSituacao();
				this.fornecedorDados.alterarTransacao(this.transacao);
				atualizaIteracaoCanal(respostaCanal);
				this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			try {
				String respostaCanal = this.transformaMensagemCanal.preparaXmlRespostaComXslt(
						this.versaoServicoChamado.getDeXsltResposta(), 
						Long.toString(this.transacao.getNuNsuTransacao()),
						Constantes.ORIGEM_SIMTX,
						this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO), 
						new ParametroXsl("erro", "true"),
						new ParametroXsl("nsu", Long.toString(this.transacao.getNuNsuTransacao())),
						new ParametroXsl("nsuSiper", Long.toString(assinaturaMultipla.getNsuPermissao())),
						new ParametroXsl("dataSiper", assinaturaMultipla.getDataPermissao().toString()),
						new ParametroXsl("dataPrevistaEfetivacao", assinaturaMultipla.getDataEfetivacao().toString()),
						new ParametroXsl("nsuAssinaturaMultipla",Long.toString(assinaturaMultipla.getNsuAssinaturaMultipla())),
						new ParametroXsl("tipoTransacao", ""));

				responder(respostaCanal);

				this.transacao.negarSituacao();
				this.fornecedorDados.alterarTransacao(this.transacao);
				atualizaIteracaoCanal(respostaCanal);

			} catch (Exception e2) {
				logger.error(e2.getMessage(), e2);
			}
		}
	}

	private void montarTagTransacoes(Node nodeTransacoes, String xmlEntradaSibar) {
		logger.info("Inicio Montar Tag Transacoes");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.newDocument();
			Element root = doc.createElement("TRANSACOES");
			doc.adoptNode(root);
			doc.appendChild(root);
			List<Long> nsuListBarramento = new ArrayList<Long>();

			if (nodeTransacoes != null) {
				for (int i = 0; i < nodeTransacoes.getChildNodes().getLength(); i++) {
					Node node = nodeTransacoes.getChildNodes().item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						String linha = node.getTextContent();
						if (linha.trim().contains("SIMTX")) {
							String nsu = xpath("/TRANSACAO/ORIGEM/NSU", node);
							Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla = this.fornecedorDadosAssinaturaMultipla
									.buscarAssinaturaMultipla(Long.valueOf(nsu));
							if (assinaturaMultipla != null) {
								nsuListBarramento.add(Long.valueOf(nsu));
								Element sistemaOrigem = doc
										.createElement(xpath("/TRANSACAO/ORIGEM/SISTEMA", node).trim());
								preencherTagSimtx(assinaturaMultipla, sistemaOrigem);
								montarTagTransacao(node, doc, root, sistemaOrigem);
							}
							continue;
						} else if (linha.trim().contains("SIAUT")) {
							Element sistemaOrigem = doc.createElement(xpath("/TRANSACAO/ORIGEM/SISTEMA", node).trim());
							Element resumo = doc.createElement("RESUMO");
							resumo.appendChild(doc.createTextNode(xpath("/TRANSACAO/RESUMO", node)));
							sistemaOrigem.appendChild(resumo);
							montarTagTransacao(node, doc, root, sistemaOrigem);
						}
					}
				}

			}

			BuscadorTextoXml buscadorEntradaSibar = new BuscadorTextoXml(xmlEntradaSibar);

			int unidade = 0;
			int operacao = 0;
			long numero = 0;
			int dv = 0;
			int indicadorTipoConta = 0;

			if (!buscadorEntradaSibar.xpathTexto("/*[1]/CONSULTA_ASSINATURA_ELETRONICA_ENTRADA/CONTA/CONTA_SIDEC").isEmpty()) {
				Node nodeConta = buscadorEntradaSibar.xpath("/*[1]/CONSULTA_ASSINATURA_ELETRONICA_ENTRADA/CONTA_SIDEC");
				unidade = Integer.valueOf(xpath("/CONTA_SIDEC/UNIDADE", nodeConta));
				operacao = Integer.valueOf(xpath("/CONTA_SIDEC/PRODUTO", nodeConta));
				numero = Long.parseLong(xpath("/CONTA_SIDEC/NUMERO", nodeConta));
				dv = Integer.valueOf(xpath("/CONTA_SIDEC/DV", nodeConta));
				indicadorTipoConta = 1;
			} else if (!buscadorEntradaSibar
					.xpathTexto("/*[1]/CONSULTA_ASSINATURA_ELETRONICA_ENTRADA/CONTA_NSGD").isEmpty()) {
				Node nodeConta = buscadorEntradaSibar.xpath("/*[1]/CONSULTA_ASSINATURA_ELETRONICA_ENTRADA/CONTA_NSGD");
				unidade = Integer.valueOf(xpath("/CONTA_NSGD/UNIDADE", nodeConta));
				operacao = Integer.valueOf(xpath("/CONTA_NSGD/PRODUTO", nodeConta));
				numero = Long.parseLong(xpath("/CONTA_NSGD/NUMERO", nodeConta));
				dv = Integer.valueOf(xpath("/CONTA_NSGD/DV", nodeConta));
				indicadorTipoConta = 2;

			}

			List<Mtxtb027TransacaoAssinaturaMultipla> list = this.fornecedorDadosAssinaturaMultipla
					.buscarAssinaturasMultiplasPendentesNaoRelacionadas(unidade, operacao, numero, dv,
							indicadorTipoConta, nsuListBarramento);

			for (Mtxtb027TransacaoAssinaturaMultipla mtxtb027TransacaoAssinaturaMultipla : list) {
				preencherTagSimtxCompleta(doc, root, mtxtb027TransacaoAssinaturaMultipla);
			}
			gravarDocumentNoBusData(doc);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("Fim Montar Tag Transacoes");
	}

	private void montarTagTransacao(Node nodeTransao, Document doc, Element root, Element sistemaOrigem) {
		try {
			Element transacaoTag = doc.createElement("TRANSACAO");

			Element nsuSiper = doc.createElement("NSU_SIPER");
			Element dataSiper = doc.createElement("DATA_SIPER");
			Element dataPrevistaEfetivacao = doc.createElement("DATA_PREVISTA_EFETIVACAO");
			Element situacao = doc.createElement("SITUACAO");
			Element nsuretorno = doc.createElement("NSU");

			nsuSiper.appendChild(doc.createTextNode(xpath("/TRANSACAO/NSU_SIPER", nodeTransao)));
			dataSiper.appendChild(doc.createTextNode(xpath("/TRANSACAO/DATA", nodeTransao)));
			dataPrevistaEfetivacao
					.appendChild(doc.createTextNode(xpath("/TRANSACAO/DATA_PREVISTA_EFETIVACAO", nodeTransao)));
			situacao.appendChild(doc.createTextNode(xpath("/TRANSACAO/SITUACAO", nodeTransao)));
			situacao.appendChild(doc.createTextNode(xpath("/TRANSACAO/RESUMO", nodeTransao)));
			nsuretorno.appendChild(doc.createTextNode(xpath("/TRANSACAO/ORIGEM/NSU", nodeTransao)));
			sistemaOrigem.appendChild(nsuretorno);

			transacaoTag.appendChild(nsuSiper);
			transacaoTag.appendChild(dataSiper);
			transacaoTag.appendChild(dataPrevistaEfetivacao);
			transacaoTag.appendChild(situacao);
			transacaoTag.appendChild(sistemaOrigem);

			root.appendChild(transacaoTag);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void preencherTagSimtxCompleta(Document doc, Element root,
			Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla) {
		try {
			Element transacaoTag = doc.createElement("TRANSACAO");

			Element nsuSiper = doc.createElement("NSU_SIPER");
			Element dataSiper = doc.createElement("DATA_SIPER");
			Element dataPrevistaEfetivacao = doc.createElement("DATA_PREVISTA_EFETIVACAO");
			Element situacao = doc.createElement("SITUACAO");
			Element nsuretorno = doc.createElement("NSU");

			nsuSiper.appendChild(doc.createTextNode(String.valueOf(assinaturaMultipla.getNsuPermissao())));
			dataSiper.appendChild(doc.createTextNode(String.valueOf(assinaturaMultipla.getDataPermissao())));
			dataPrevistaEfetivacao.appendChild(doc.createTextNode(String.valueOf(assinaturaMultipla.getDataEfetivacao())));
			situacao.appendChild(doc.createTextNode(assinaturaMultipla.getSituacao().toString()));
			nsuretorno.appendChild(doc.createTextNode(String.valueOf(assinaturaMultipla.getNsuAssinaturaMultipla())));

			Element sistemaOrigem = doc.createElement("SIMTX");
			preencherTagSimtx(assinaturaMultipla, sistemaOrigem);
			sistemaOrigem.appendChild(nsuretorno);

			transacaoTag.appendChild(nsuSiper);
			transacaoTag.appendChild(dataSiper);
			transacaoTag.appendChild(dataPrevistaEfetivacao);
			transacaoTag.appendChild(situacao);
			transacaoTag.appendChild(sistemaOrigem);

			root.appendChild(transacaoTag);

		} catch (Exception e) {
			logger.error("Erro ao montar tags SIMTX", e);
		}
	}

	private void preencherTagSimtx(Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla, Element simtx) {
		Element nsuSimtx = simtx.getOwnerDocument().createElement("NSU");
		Element codigoServicoSiper = simtx.getOwnerDocument().createElement("CODIGO_SERVICO");
		Element valorTransacao = simtx.getOwnerDocument().createElement("VALOR_TRANSACAO");

		nsuSimtx.appendChild(simtx.appendChild(
				simtx.getOwnerDocument().createTextNode(Long.toString(assinaturaMultipla.getNsuAssinaturaMultipla()))));
		codigoServicoSiper.appendChild(
				simtx.getOwnerDocument().createTextNode(Integer.toString(assinaturaMultipla.getServicoPermissao())));
		valorTransacao.appendChild(simtx.getOwnerDocument().createTextNode(assinaturaMultipla.getValor().toString()));

		simtx.appendChild(nsuSimtx);
		simtx.appendChild(codigoServicoSiper);
		simtx.appendChild(valorTransacao);
	}

	/**
	 * 
	 * Processa a transacao NEGOCIAL de validacao de assinatura
	 * 
	 */
	private void processarTransacaoAssinatura() {
		try {
			this.dadosBarramento = new DadosBarramento();
			this.transformaMensagemCanal = criarTransformadorMensagemCanal(null);
			this.transformaMensagemCanal.prepararBusData(this.mensagemCanal);
			TransformadorXsl transformadorXsl = new TransformadorXsl();

			String caminhoXls = this.simtxConfig.getCaminhoXslt() + this.versaoServicoChamado.getDeXsltRequisicao();
			String arquivoXsl = new RepositorioArquivo().recuperarArquivo(caminhoXls);

			String xmlEntradaSibar = transformadorXsl
					.transformar(this.transformaMensagemCanal.getDadosBarramento().getDadosLeitura(), arquivoXsl);
			
			List<Mtxtb003ServicoTarefa> tarefas = 
					gerenciadorTarefas.carregarTarefasServico(this.versaoServicoChamado, this.canal, null);
			
			xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, this.versaoServicoChamado, tarefas);
			String xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, this.servicoChamado);
			
			String busdataOld = "<BUSDATA_OLD>" + this.transformaMensagemCanal.getDadosBarramento().getDados()
					+ "</BUSDATA_OLD>";
			this.transformaMensagemCanal.getDadosBarramento().escrever(xmlSaidaSibar);

			gerenciadorTarefas.salvarTarefasServico(tarefas, this.versaoServicoChamado, this.transacao,
					this.transformaMensagemCanal.getDadosBarramento(), xmlSaidaSibar);

			String nsuTransacaoSimtx = this.transformaMensagemCanal.getDadosBarramento()
					.xpathTexto("/BUSDATA/ASSINATURA_MULTIPLA_ENTRADA/NSU_ORIGEM/text()");

			Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla = this.fornecedorDadosAssinaturaMultipla
					.buscarAssinaturaMultipla(Long.valueOf(nsuTransacaoSimtx));

			String classificacaoAssinatura = this.transformaMensagemCanal.getDadosBarramento()
					.xpathTexto("/BUSDATA/SERVICO_SAIDA/VALIDA_PERMISSAO_SAIDA/ASSINATURA/CLASSIFICACAO/text()");

			if (CLASSIFICACAO_ASSINATURA_FINAL.equals(classificacaoAssinatura)) {
				processarUltimaAssinatura(assinaturaMultipla, new XmlValidador(), busdataOld);
				return;
			}

			String mensagem = this.transformaMensagemCanal.preparaXmlRespostaComXslt(
					this.versaoServicoChamado.getDeXsltResposta(), 
					Long.toString(assinaturaMultipla.getNsuAssinaturaMultipla()), 
					Constantes.ORIGEM_SIMTX, 
					this.fornecedorDados.buscarMensagem(MensagemRetorno.SUCESSO), 
					new ParametroXsl(XSL_PARAM_INIBIR_NEGOCIAL, Boolean.TRUE.toString()));
			
			atualizaTransacao(Constantes.IC_SERVICO_FINALIZADO);
			atualizaIteracaoCanal(mensagem);

			Long cpf = Long.parseLong(this.transformaMensagemCanal.getDadosBarramento()
					.xpathTexto("/BUSDATA/ASSINATURA_MULTIPLA_ENTRADA/CPF"));
			this.fornecedorDadosAssinaturaMultipla.salvar(criarAssinatura(assinaturaMultipla, cpf));

			responder(mensagem);
			this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem().getDeMensagemTecnica(), se);

			try {
				String respostaCanal = prepararMensagemRespostaErro(this.versaoServicoChamado.getDeXsltResposta(),
						se.getMensagem(), se.getSistemaOrigem());

				responder(respostaCanal);
				this.transacao.negarSituacao();
				this.fornecedorDados.alterarTransacao(this.transacao);
				atualizaIteracaoCanal(respostaCanal);

			} catch (Exception e) {
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			try {
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				String respostaCanal = prepararMensagemRespostaErro(this.versaoServicoChamado.getDeXsltResposta(),
						mensagemErroInterno, Constantes.ORIGEM_SIMTX);
				responder(respostaCanal);

				this.transacao.negarSituacao();
				this.fornecedorDados.alterarTransacao(this.transacao);
				atualizaIteracaoCanal(respostaCanal);

			} catch (Exception e2) {
			}
		}
		logger.info(logAssinaturaMultiplaFinalizado);
	}

	private String montarXml(Long codigoTarefa) throws Exception {
		Mtxtb012VersaoTarefaPK vtpk = new Mtxtb012VersaoTarefaPK();
		vtpk.setNuTarefa002(codigoTarefa);
		vtpk.setNuVersaoTarefa(1);

		Mtxtb012VersaoTarefa tarefaAssinaturaMultipla = this.fornecedorDados.buscarVersaoTarefaPorPK(vtpk);
		String caminhoXls = this.simtxConfig.getCaminhoXslt() + tarefaAssinaturaMultipla.getDeXsltRequisicao();
		String arquivoXsl = new RepositorioArquivo().recuperarArquivo(caminhoXls);

		return new TransformadorXsl().transformar(this.transformaMensagemCanal.getDadosBarramento().getDadosLeitura(),
				arquivoXsl, new ParametroXsl("nsu", Long.toString(this.transacao.getNuNsuTransacao())));
	}

	private String montarXmlServico() throws Exception {
		String caminhoXls = this.simtxConfig.getCaminhoXslt() + this.versaoServicoChamado.getDeXsltRequisicao();
		String arquivoXsl = new RepositorioArquivo().recuperarArquivo(caminhoXls);

		return new TransformadorXsl().transformar(this.transformaMensagemCanal.getDadosBarramento().getDadosLeitura(),
				arquivoXsl);
	}

	/**
	 * 
	 * Processa a transacao de TAREFA de validacao de assinatura
	 */
	private void processarTransacaoPrimeiraAssinaturaMultipla() {
		this.dadosBarramento = new DadosBarramento();
		Mtxtb008MeioEntrada meioEntrada = null;
		try {
			this.transformaMensagemCanal = criarTransformadorMensagemCanal(meioEntrada);
			this.transformaMensagemCanal.prepararBusData(this.mensagemCanal);

			meioEntrada = recuperarMeioEntradaCadastroAssinatura(meioEntrada);
			this.transformaMensagemCanal.setMeioEntrada(meioEntrada);

			List<Mtxtb003ServicoTarefa> tarefas = new ArrayList<>();
			Mtxtb003ServicoTarefa tarefaValidaAssinatura = this.fornecedorDados
					.buscarTarefasNegocialPorPK(this.getServicoChamado().getNuServico(), 1, 100056L, 1);
			tarefas.add(tarefaValidaAssinatura);

			String xmlEntradaSibar = montarXml(CODIGO_TAREFA_VALIDA_ASSINATURA);
			xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, this.versaoServicoChamado, tarefas);
			
			Mtxtb001Servico servicoValidaAssinatura = this.fornecedorDados
					.buscarServicoPorPK(Long.valueOf(CODIGO_SERVICO_VALIDA_ASSINATURA));

			String xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servicoValidaAssinatura);
			
			this.transformaMensagemCanal.getDadosBarramento().escrever(xmlSaidaSibar);
			BuscadorTextoXml buscadorSaidaSibar = new BuscadorTextoXml(xmlSaidaSibar);

			gerenciadorTarefas.salvarTarefasServico(tarefas, this.versaoServicoChamado, this.transacao,
					this.transformaMensagemCanal.getDadosBarramento(), xmlSaidaSibar);
			String codigoRetorno = buscadorSaidaSibar.xpathTexto("/*[1]/COD_RETORNO");

			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagemPorTarefaCodRetorno(codigoRetorno,
							TarefaUtils.filtrarServicoTarefaNegocial(tarefas).get(0).getMtxtb012VersaoTarefa())
					.getMtxtb006Mensagem();
			
			if (mensagem.isImpeditiva()) {
				throw new ServicoException(mensagem, buscadorSaidaSibar.xpathTexto("/*[1]/ORIGEM_RETORNO/text()"));
			}

			String classificacaoAssinatura = buscadorSaidaSibar
					.xpathTexto("/*[1]/VALIDA_PERMISSAO_SAIDA/ASSINATURA/CLASSIFICACAO");

			Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla = criarTransacaoAssinaturaMultipla(
					buscadorSaidaSibar);
			if (CLASSIFICACAO_ASSINATURA_FINAL.equals(classificacaoAssinatura)) {
				String busdataOld = "<BUSDATA_OLD>" + xmlSaidaSibar + "</BUSDATA_OLD>";
				assinaturaMultipla.setMtxtb011VersaoServico(this.getVersaoServicoChamado());
				processarUltimaAssinatura(assinaturaMultipla, new XmlValidador(), busdataOld);
				return;
			}

			logger.info("Restam assinaturas para efetivacao da transacao");
			logger.info("Preparando xml de resposta para enviar ao Canal");

			mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.SUCESSO);
			
			//TODO BRQ cadastrar mensagem sucesso com pendencia assinatuar?
			//this.mensagemServidor.recuperarMensagem("sucesso.status.pendencia.assinatura")

			String mensagemRetorno = this.transformaMensagemCanal.preparaXmlRespostaComXslt(
					this.versaoServicoChamado.getDeXsltResposta(),
					String.valueOf(assinaturaMultipla.getNsuAssinaturaMultipla()), 
					Constantes.ORIGEM_SIMTX, 
					mensagem, 
					new ParametroXsl(XSL_PARAM_INIBIR_NEGOCIAL, Boolean.TRUE.toString()));

			try {
				logger.info("Situacao final da transacao: [" + Constantes.IC_SERVICO_FINALIZADO_PENDENTE_ASSINATURA
						+ "] Finalizada com Pendencia de Assinatura");
				atualizaTransacao(Constantes.IC_SERVICO_FINALIZADO_PENDENTE_ASSINATURA);
				atualizaIteracaoCanal(mensagemRetorno);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			logger.info("Situacao final da Assinatura Multipla: ["
					+ SituacaoAssinaturaMultipla.PENDENTE_ASSINATURA.getRotulo()
					+ "] Transacao Pendente de Assinatura");
			logger.info("Salvando Assinatura Multipla");
			assinaturaMultipla = this.fornecedorDadosAssinaturaMultipla.salvar(assinaturaMultipla);

			Mtxtb028ControleAssinaturaMultipla assinatura = criarAssinatura(assinaturaMultipla, Long
					.valueOf(buscadorSaidaSibar.xpathTexto("/*[1]/VALIDA_PERMISSAO_SAIDA/ASSINANTES/ASSINANTE/CPF")));

			this.fornecedorDadosAssinaturaMultipla.salvar(assinatura);

			responder(mensagemRetorno);

			this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
			
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem().getDeMensagemTecnica(), se);

			try {
				String respostaCanal = prepararMensagemRespostaErro(this.versaoServicoChamado.getDeXsltResposta(),
						se.getMensagem(), se.getSistemaOrigem());

				responder(respostaCanal);
				logger.info("Situacao final da transacao: [" + Constantes.IC_SERVICO_NEGADO + "] Negada");
				atualizaTransacao(Constantes.IC_SERVICO_NEGADO);
				atualizaIteracaoCanal(respostaCanal);
			} catch (Exception e) {
				logger.warn("Nao foi possivel gravar no banco de dados:\n" + e);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			try {
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				String respostaCanal = prepararMensagemRespostaErro(this.versaoServicoChamado.getDeXsltResposta(),
						mensagemErroInterno, Constantes.ORIGEM_SIMTX);
				responder(respostaCanal);

				logger.info("Situacao final da transacao: [" + Constantes.IC_SERVICO_NEGADO + "] Negada");
				atualizaTransacao(Constantes.IC_SERVICO_NEGADO);
				atualizaIteracaoCanal(respostaCanal);

			} catch (Exception e2) {
				logger.warn("Nao foi possivel gravar no banco de dados:\n" + e);
			}
		}
		logger.info(logAssinaturaMultiplaFinalizado);
	}

	/**
	 * 
	 * Metodo que retorna meio de entrada, dispara fluxo de excecao de acordo com as
	 * regras do assinatura multipla
	 * 
	 * @param meioEntrada
	 * @return
	 * @throws ServicoException
	 */
	private Mtxtb008MeioEntrada recuperarMeioEntradaCadastroAssinatura(Mtxtb008MeioEntrada meioEntrada)
			throws ServicoException {
		logger.info("Validando meio de entrada");
		try {
			meioEntrada = this.fornecedorDados.buscarMeioEntrada(CODIGO_MEIOENTRADA_AM);
		} catch (Exception e) {
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.MEIO_ENTRADA_INEXISTENTE);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}

		if (meioEntrada.getIcSituacao().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.MEIO_ENTRADA_INATIVO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}

		try {
			Mtxtb018VrsoMeioEntraSrvco meioEntradaServico = recuperarMeioEntradaPorServico(
					meioEntrada.getNuMeioEntrada(), this.servicoChamado.getNuServico(),
					this.versaoServicoChamado.getId().getNuVersaoServico());

			if (meioEntradaServico.getIcSituacao().compareTo(Constantes.IC_SITUACAO_INATIVO) == 0) {
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.MEIO_ENTRADA_INATIVO_SERVICO);
				throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			}

		} catch (SemResultadoException e) {
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.MEIO_ENTRADA_INEXISTENTE_SERVICO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}

		return meioEntrada;
	}

	/**
	 * 
	 * Monta mensagem de erro de acordo com xsl informado.
	 * 
	 * @param xsl
	 * @param codRetorno
	 * @param origemRetorno
	 * @param msgRetorno
	 * @return
	 */
	private String prepararMensagemRespostaErro(String xsl, Mtxtb006Mensagem mensagem, String origemRetorno) {
		try {
			return this.transformaMensagemCanal.preparaXmlRespostaComXslt(
					xsl, String.valueOf(this.transacao.getNuNsuTransacao()), 
					Constantes.ORIGEM_SIMTX, mensagem, 
					new ParametroXsl("erro", "true"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 
	 * Retorna o meio de entrada sem os fluxos de excecoes do valida assinatura
	 * 
	 * @param codigoMeioEntrada
	 * @param codigoServico
	 * @param codigoVersaoServico
	 * @return
	 * @throws SemResultadoException
	 */
	private Mtxtb018VrsoMeioEntraSrvco recuperarMeioEntradaPorServico(long codigoMeioEntrada, long codigoServico,
			long codigoVersaoServico) throws SemResultadoException {
		logger.info("Validando meio de entrada para o servico");
		Mtxtb018VrsoMeioEntraSrvcoPK meioEntradaServicoPK = new Mtxtb018VrsoMeioEntraSrvcoPK();
		meioEntradaServicoPK.setNuMeioEntrada008(codigoMeioEntrada);
		meioEntradaServicoPK.setNuServico011(codigoServico);
		meioEntradaServicoPK.setNuVersaoServico011(codigoVersaoServico);

		return this.fornecedorDados.buscarVersaoMeioEntraServcoPorPK(meioEntradaServicoPK);
	}

	/**
	 * Monta a classe de assinatura
	 * 
	 * @param assinaturaMultipla
	 * @return
	 */
	private Mtxtb028ControleAssinaturaMultipla criarAssinatura(Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla,
			Long cpf) {
		logger.debug("Criando assinatura");
		MtxtbAssinaturaPK assinaturaPk = new MtxtbAssinaturaPK();
		Mtxtb028ControleAssinaturaMultipla assinatura = new Mtxtb028ControleAssinaturaMultipla(
				Mtxtb028ControleAssinaturaMultipla.INDICADOR_VALIDACAO_ASSINATURA_NAO);
		assinatura.setId(assinaturaPk);

		assinatura.getId().setNsuTransacao(this.transacao.getNuNsuTransacao());
		assinatura.getId().setNsuTransacaoOrigem(assinaturaMultipla.getNsuAssinaturaMultipla());
		assinatura.setDataMulticanal(new Date());
		assinatura.setCpf(cpf);

		assinatura.setCanal(this.canal.getNuCanal());
		return assinatura;
	}

	/**
	 * valida horario da transacao negocial
	 * 
	 * @param servicoChamado
	 * @param header
	 * @throws Exception
	 */
	private void validarExecucaoNegocial(Mtxtb001Servico servicoChamado, BuscadorTextoXml buscadorXml)
			throws Exception {
		this.validadorMensagemCanal = new ValidadorMensagemCanal();
		this.validadorMensagemCanal.setCanal(this.canal);
		this.validadorMensagemCanal.setServicoChamado(servicoChamado);
		this.validadorMensagemCanal.setDataHora(buscadorXml.xpathTexto("/*[1]/HEADER/CANAL/DATAHORA"));
		this.validadorMensagemCanal.setNomeMeioEntrada(buscadorXml.xpathTexto("/*[1]/HEADER/MEIOENTRADA"));
		this.validadorMensagemCanal.setFornecedorDados(this.fornecedorDados);

		try {
			this.validadorMensagemCanal.validarParametroDoCanalHora();
		} catch (SimtxException se) {
			if (se.getErrorCode().equals("MN019")) {
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			}
		}

	}

	/**
	 * 
	 * Metodo que dispara a transacao negocial quando todas assinaturas foram
	 * realizadas.
	 * 
	 * @param assinaturaMultipla
	 * @param xmlValidador
	 * @param busdataOld
	 */
	private void processarUltimaAssinatura(Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla,
			XmlValidador xmlValidador, String busdataOld) {
		logger.info("Processando ultima assinatura");
		Mtxtb014Transacao novaTransacaoSimtx = null;
		Mtxtb016IteracaoCanal novaIteracao = null;
		Mtxtb011VersaoServico servicoOriginal = assinaturaMultipla.getMtxtb011VersaoServico();

		try {
			try {
				logger.info("Situacao final da transacao de assinatura: [" + Constantes.IC_SERVICO_FINALIZADO
						+ "] Finalizada");
				atualizaTransacao(Constantes.IC_SERVICO_FINALIZADO);
			} catch (Exception e) {
				Mtxtb006Mensagem mensagemBanco = this.fornecedorDados.buscarMensagem(MensagemRetorno.SUCESSO);
				
				String mensagem = this.transformaMensagemCanal.preparaXmlRespostaComXslt(
						this.versaoServicoChamado.getDeXsltResposta(), 
						String.valueOf(this.transacao.getNuNsuTransacao()), 
						Constantes.ORIGEM_SIMTX, 
						mensagemBanco, 
						new ParametroXsl("param1", String.valueOf(assinaturaMultipla.getNsuAssinaturaMultipla())));
				
				responder(mensagem);
			}

			try {
				logger.info("Situacao final da Assinatura Multipla: [" + SituacaoAssinaturaMultipla.ASSINADA.getRotulo()
						+ "] Transacao Assinada");
				Long cpf = Long
						.parseLong(this.transformaMensagemCanal.getDadosBarramento().xpathTexto("/BUSDATA/*[2]/CPF"));
				assinaturaMultipla.setSituacao(SituacaoAssinaturaMultipla.ASSINADA);
				this.fornecedorDadosAssinaturaMultipla.salvar(assinaturaMultipla);
				this.fornecedorDadosAssinaturaMultipla.salvar(criarAssinatura(assinaturaMultipla, cpf));
			} catch (Exception e) {
				logger.error("Nao foi possivel gravar no banco de dados:\n" + e);
			}

			try {
				novaTransacaoSimtx = criarTransacaoSimtx(assinaturaMultipla);
				novaIteracao = criarIteracaoCanal(assinaturaMultipla.getXmlNegocial(), novaTransacaoSimtx);
				salvarTransacaoServico(assinaturaMultipla, novaTransacaoSimtx);
			} catch (Exception e) {
				Mtxtb006Mensagem mensagemBanco = this.fornecedorDados.buscarMensagem(MensagemRetorno.SUCESSO);
				
				String mensagem = this.transformaMensagemCanal.preparaXmlRespostaComXslt(
						this.versaoServicoChamado.getDeXsltResposta(), 
						String.valueOf(this.transacao.getNuNsuTransacao()), 
						Constantes.ORIGEM_SIMTX, 
						mensagemBanco, 
						new ParametroXsl("param1", String.valueOf(assinaturaMultipla.getNsuAssinaturaMultipla())));
				
				responder(mensagem);
			}

			BuscadorTextoXml buscadorXmlOriginal = new BuscadorTextoXml(assinaturaMultipla.getXmlNegocial());

			try {
				validarExecucaoNegocial(servicoOriginal.getMtxtb001Servico(), buscadorXmlOriginal);
			} catch (Exception e) {
				logger.info("Situacao final da transacao negocial: [" + Constantes.IC_SERVICO_NEGADO + "] Negada");
				novaTransacaoSimtx.negarSituacao();
				this.fornecedorDados.alterarTransacao(novaTransacaoSimtx);
				throw e;
			}

			criarAssinaturaFalsa(assinaturaMultipla, novaTransacaoSimtx);

			BuscadorTextoXml buscadorSaidaSibar = null;
			String xmlSaidaSibar = null;
			TransformaMensagemCanal transformadorOld = new TransformaMensagemCanal();
			transformadorOld.setSimtxConfig(this.transformaMensagemCanal.getSimtxConfig());
			transformadorOld.setRepositorio(this.transformaMensagemCanal.getRepositorio());
			Mtxtb003ServicoTarefa tarefaNegocial = null;
			try {
				String caminhoXls = this.simtxConfig.getCaminhoXslt() + servicoOriginal.getDeXsltRequisicao();
				String arquivoXsl = new RepositorioArquivo().recuperarArquivo(caminhoXls);

				String xmlServicoOriginal = "<BUSDATA><BUSTIME>TIME</BUSTIME>"
						+ assinaturaMultipla.getXmlNegocial().replaceAll("<\\?xml.*\\?>", "") + "</BUSDATA>";
				String xmlTransformado = new TransformadorXsl().transformar(xmlServicoOriginal, arquivoXsl);

				List<Mtxtb003ServicoTarefa> tarefas = this.fornecedorDados
						.buscarTarefasNegocialPorServico(assinaturaMultipla.getServico(), 1, this.canal.getNuCanal());

				tarefaNegocial = filtrarTarefaNegocial(tarefas);
				List<Mtxtb003ServicoTarefa> listaTarefaNegocial = new ArrayList<>();
				listaTarefaNegocial.add(tarefaNegocial);

				xmlTransformado = this.geradorPassosMigrado.gerarPassos(xmlTransformado, this.versaoServicoChamado, listaTarefaNegocial);

				xmlSaidaSibar = this.execucaoMq.executar(xmlTransformado, servicoOriginal.getMtxtb001Servico());

				transformadorOld.getDadosBarramento().escrever(this.mensagemCanal.replaceAll("<\\?xml.*\\?>", ""));
				transformadorOld.getDadosBarramento().escrever(xmlSaidaSibar);

				gerenciadorTarefas.salvarTarefasServico(listaTarefaNegocial, this.versaoServicoChamado, this.transacao,
						transformadorOld.getDadosBarramento(), xmlSaidaSibar);

				buscadorSaidaSibar = new BuscadorTextoXml(xmlSaidaSibar);
				BuscadorResposta buscadorResposta = new BuscadorResposta();
				
				Resposta controleNegocial = buscadorResposta
						.buscarRespostaBarramento(buscadorSaidaSibar, listaTarefaNegocial.get(listaTarefaNegocial.size() -1).getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoServicoBarramento());
				
				Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagemPorTarefaCodRetorno(
						controleNegocial.getCodigo(), tarefaNegocial.getMtxtb012VersaoTarefa()).getMtxtb006Mensagem();
				
				if (mensagem.isImpeditiva()) {
					throw new ServicoException(mensagem, controleNegocial.getOrigem());
				}

			} catch (Exception e) {
				logger.info("Situacao final da transacao negocial: [" + Constantes.IC_SERVICO_NEGADO + "] Negada");
				novaTransacaoSimtx.negarSituacao();
				this.fornecedorDados.alterarTransacao(novaTransacaoSimtx);

				logger.info("Situacao final da Assinatura Multipla: ["
						+ SituacaoAssinaturaMultipla.NAO_EFETUADA.getRotulo() + "] Transacao nao Efetuada");
				assinaturaMultipla.setSituacao(SituacaoAssinaturaMultipla.NAO_EFETUADA);
				this.fornecedorDadosAssinaturaMultipla.salvar(assinaturaMultipla);
				throw e;
			}

			this.transformaMensagemCanal.gravarNoDadosBarramento("<NEGOCIAL>" + xmlSaidaSibar + "</NEGOCIAL>");

			Resposta controleNegocial = new BuscadorResposta()
					.buscarRespostaBarramento(buscadorSaidaSibar, tarefaNegocial.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoServicoBarramento());
			
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagemPorTarefaCodRetorno(
					controleNegocial.getCodigo(), tarefaNegocial.getMtxtb012VersaoTarefa()).getMtxtb006Mensagem();
			
			String respostaCanal = transformadorOld.preparaXmlRespostaComXslt(
					servicoOriginal.getDeXsltResposta(),
					String.valueOf(this.transacao.getNuNsuTransacao()),
					controleNegocial.getOrigem(),
					mensagem);
			
			try {
				logger.info(
						"Situacao final da transacao negocial: [" + Constantes.IC_SERVICO_FINALIZADO + "] Finalizada");
				novaTransacaoSimtx.finalizaSituacao();
				this.fornecedorDados.alterarTransacao(novaTransacaoSimtx);
				atualizaIteracaoCanal(novaIteracao, novaTransacaoSimtx, respostaCanal);

				logger.info("Situacao final da Assinatura Multipla: ["
						+ SituacaoAssinaturaMultipla.EFETIVADA.getRotulo() + "] Transacao Efetivada");
				assinaturaMultipla.setSituacao(SituacaoAssinaturaMultipla.EFETIVADA);
				this.fornecedorDadosAssinaturaMultipla.salvar(assinaturaMultipla);
			} catch (Exception e) {
				logger.error("Nao foi possivel gravar no banco de dados: " + e.getMessage(), e);
			}

			responder(respostaCanal);
			this.processadorEnvioSicco.processarEnvioOnline(transacao, Constantes.ENVIO_UNICA);
			logger.info(logAssinaturaMultiplaFinalizado);
			
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem().getDeMensagemTecnica(), se);

			try {
				String respostaCanal = prepararMensagemRespostaErro(servicoOriginal.getDeXsltResposta(), se.getMensagem(),
						se.getSistemaOrigem());

				responder(respostaCanal);
				if (novaTransacaoSimtx != null)
					atualizaIteracaoCanal(novaIteracao, novaTransacaoSimtx, respostaCanal);

			} catch (Exception e) {
				logger.error("Nao foi possivel enviar a resposta para o canal:\n" + e);
			}
		} catch (Exception e) {
			logger.error("Erro ao processar ultima assinatura", e);

			try {
				Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
				String respostaCanal = prepararMensagemRespostaErro(servicoOriginal.getDeXsltResposta(),
						mensagemErroInterno, Constantes.ORIGEM_SIMTX);

				responder(respostaCanal);
				if (novaTransacaoSimtx != null)
					atualizaIteracaoCanal(novaIteracao, novaTransacaoSimtx, respostaCanal);

			} catch (Exception e2) {
				logger.error("Nao foi possivel enviar a resposta para o canal:\n" + e2);
			}
		}

	}

	/**
	 * Atualiza a IteracaoCanal com o xml de resposta enviada para o Canal.
	 * 
	 * @param resposta
	 * @throws Exception
	 */
	public void atualizaIteracaoCanal(Mtxtb016IteracaoCanal iteracao, Mtxtb014Transacao transacao, String resposta)
			throws Exception {
		logger.info("Atualizando IteracaoCanal");

		iteracao.setMtxtb014Transacao(transacao);
		iteracao.setDeRetorno(resposta);
		iteracao.setTsRetornoSolicitacao(DataUtil.getDataAtual());

		this.fornecedorDados.alterarIteracaoCanal(iteracao);
	}

	/**
	 * Salva xml do Canal na entidade IteracaoCanal.
	 * 
	 * @param mensagem
	 * @param transacao
	 * @return
	 * @throws Exception
	 */
	public Mtxtb016IteracaoCanal criarIteracaoCanal(String mensagem, Mtxtb014Transacao transacao) throws Exception {
		logger.info("Criando IteracaoCanal negocial");
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		try {
			String terminal = "";

			iteracaoCanal = new Mtxtb016IteracaoCanal();
			iteracaoCanal.setMtxtb004Canal(new Mtxtb004Canal());
			iteracaoCanal.getMtxtb004Canal().setNuCanal(Long.parseLong(transacao.getCoCanalOrigem()));
			iteracaoCanal.setMtxtb014Transacao(transacao);
			iteracaoCanal.setTsRecebimentoSolicitacao(DataUtil.getDataAtual());
			iteracaoCanal.setTsRetornoSolicitacao(DataUtil.getDataAtual());
			iteracaoCanal.setId(new Mtxtb016IteracaoCanalPK());
			iteracaoCanal.getId().setNuNsuTransacao014(transacao.getNuNsuTransacao());
			iteracaoCanal.setDeRecebimento(mensagem);
			iteracaoCanal.setDtReferencia(transacao.getDtReferencia());
			iteracaoCanal.setCodTerminal(terminal);
			iteracaoCanal = this.fornecedorDados.salvarIteracaoCanal(iteracaoCanal);
		} catch (Exception e) {
			throw new SimtxException("MN018");
		}
		return iteracaoCanal;
	}

	private Mtxtb028ControleAssinaturaMultipla criarAssinaturaFalsa(
			Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla, Mtxtb014Transacao novaTransacaoSimtx) {
		MtxtbAssinaturaPK assinaturaFalsaPk = new MtxtbAssinaturaPK();
		Mtxtb028ControleAssinaturaMultipla assinaturaFalsa = new Mtxtb028ControleAssinaturaMultipla(
				Mtxtb028ControleAssinaturaMultipla.INDICADOR_VALIDACAO_ASSINATURA_SIM);

		assinaturaFalsa.setId(assinaturaFalsaPk);
		assinaturaFalsa.getId().setNsuTransacao(novaTransacaoSimtx.getNuNsuTransacao());
		assinaturaFalsa.getId().setNsuTransacaoOrigem(assinaturaMultipla.getNsuAssinaturaMultipla());
		assinaturaFalsa.setCpf(null);
		assinaturaFalsa.setDataMulticanal(new Date());

		this.fornecedorDadosAssinaturaMultipla.salvar(assinaturaFalsa);
		return assinaturaFalsa;
	}

	/**
	 * Retorna tarefa negocial de uma lista de tarefas
	 * 
	 * @param tarefas
	 * @return A tarefa negocial, se nao encontrar ira retornar null
	 */
	private Mtxtb003ServicoTarefa filtrarTarefaNegocial(List<Mtxtb003ServicoTarefa> tarefas) {
		for (Mtxtb003ServicoTarefa tarefa : tarefas) {
			if (tarefa.isNegocial())
				return tarefa;
		}

		return null;
	}

	private TransformaMensagemCanal criarTransformadorMensagemCanal(Mtxtb008MeioEntrada meioEntrada) throws Exception {
		TransformaMensagemCanal transformador = new TransformaMensagemCanal();
		transformador.setSimtxConfig(this.simtxConfig);
		transformador.setProperties(this.properties);
		transformador.setTransacao(this.transacao);
		transformador.setServicoChamado(this.servicoChamado);
		transformador.setVersaoServicoChamado(this.versaoServicoChamado);
		transformador.setFornecedorDados(this.fornecedorDados);
		transformador.setMeioEntrada(meioEntrada);
		transformador.setRepositorio(new RepositorioArquivo());
		transformador.setCanal(this.canal);

		return transformador;
	}

	private void responder(String mensagem) {
		new FilaCanal().postarMensagem(this.idMensagem, mensagem, this.canal);
	}

	/**
	 * Salva o ServicoTransacao.
	 * 
	 * @param transacao
	 * @throws Exception
	 */
	private void salvarTransacaoServico(Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla,
			Mtxtb014Transacao transacao) throws Exception {
		logger.info("Salvando Servico Transacao");
		Mtxtb017VersaoSrvcoTrnso versaoServicoTransacao = new Mtxtb017VersaoSrvcoTrnso();

		versaoServicoTransacao.setTsSolicitacao(DataUtil.getDataAtual());
		versaoServicoTransacao.setId(new Mtxtb017VersaoSrvcoTrnsoPK());
		versaoServicoTransacao.getId().setNuNsuTransacao014(transacao.getNuNsuTransacao());
		versaoServicoTransacao.getId()
				.setNuServico011(assinaturaMultipla.getMtxtb011VersaoServico().getMtxtb001Servico().getNuServico());
		versaoServicoTransacao.getId()
				.setNuVersaoServico011(assinaturaMultipla.getMtxtb011VersaoServico().getId().getNuVersaoServico());
		versaoServicoTransacao.setDtReferencia(transacao.getDtReferencia());

		versaoServicoTransacao = this.fornecedorDados.salvarTransacaoServico(versaoServicoTransacao);
		if (versaoServicoTransacao == null)
			throw new SimtxException("MN018");
	}

	/**
	 * Atualiza a Transacao com a nova situacao.
	 * 
	 * @param situacao
	 * @throws Exception
	 */
	public void atualizaTransacao(BigDecimal situacao) throws Exception {
		logger.info("Atualizando transacao");
		try {
			if (this.transacao != null) {
				this.transacao.setIcSituacao(situacao);
				this.fornecedorDados.alterarTransacao(this.transacao);
			}
		} catch (Exception e) {
			throw new SimtxException("MN018");
		}
	}

	/**
	 * Atualiza a Transacao com a nova situacao.
	 * 
	 * @param situacao
	 * @throws Exception
	 */
	public void atualizaTransacao(BigDecimal situacao, Mtxtb014Transacao transacao) throws Exception {
		logger.info("Atualizando transacao carregada");
		try {
			transacao.setIcSituacao(situacao);
			this.fornecedorDados.alterarTransacao(transacao);
		} catch (Exception e) {
			throw new SimtxException("MN018");
		}
	}

	/**
	 * Atualiza a IteracaoCanal com o xml de resposta enviada para o Canal.
	 * 
	 * @param resposta
	 * @throws Exception
	 */
	public void atualizaIteracaoCanal(String resposta) throws Exception {
		logger.info("Atualizando IteracaoCanal");
		try {
			if (this.iteracaoCanal != null && this.transacao != null) {
				this.iteracaoCanal.setMtxtb014Transacao(this.transacao);
				this.iteracaoCanal.setDeRetorno(resposta);
				this.iteracaoCanal.setTsRetornoSolicitacao(DataUtil.getDataAtual());
				this.fornecedorDados.alterarIteracaoCanal(this.iteracaoCanal);
			}
		} catch (Exception e) {
			throw new SimtxException("MN018");
		}
	}

	/**
	 * 
	 * Recupera dados do BUSDATA para criar uma transacao de assinaturaMultipla.
	 * 
	 * @return
	 * @throws ParseException
	 */
	private Mtxtb027TransacaoAssinaturaMultipla criarTransacaoAssinaturaMultipla(BuscadorTextoXml respSibar)
			throws ParseException {
		logger.debug("Criando Assinatura Multipla");
		Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla = new Mtxtb027TransacaoAssinaturaMultipla();

		DadosBarramento dadosBarramentoAssinatura = this.transformaMensagemCanal.getDadosBarramento();

		Node tag = dadosBarramentoAssinatura.xpath("/BUSDATA/*[2]/CONTA");
		int tipoContaSidec = 1;

		if (tag == null) {
			tipoContaSidec = 2;
		}

		int unidadeConta = Integer
				.parseInt(dadosBarramentoAssinatura.xpathTexto("/BUSDATA/*[2]/CONTA/CONTA_SIDEC/UNIDADE"));
		int produtoConta = Integer
				.parseInt(dadosBarramentoAssinatura.xpathTexto("/BUSDATA/*[2]/CONTA/CONTA_SIDEC/OPERACAO"));
		long numeroConta = Long
				.parseLong(dadosBarramentoAssinatura.xpathTexto("/BUSDATA/*[2]/CONTA/CONTA_SIDEC/CONTA"));
		short dvConta = Short.parseShort(dadosBarramentoAssinatura.xpathTexto("/BUSDATA/*[2]/CONTA/CONTA_SIDEC/DV"));

		String valorStr = "";
		BigDecimal valor = null;

		if (StringUtil.isEmpty(valorStr)) {
			valor = new BigDecimal(0);
		} else {
			valor = new BigDecimal(valorStr);
		}
		SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd");

		assinaturaMultipla.setNsuAssinaturaMultipla(this.transacao.getNuNsuTransacao());
		assinaturaMultipla.setUnidade(unidadeConta);
		assinaturaMultipla.setProduto(produtoConta);
		assinaturaMultipla.setConta(numeroConta);
		assinaturaMultipla.setDvConta(dvConta);
		assinaturaMultipla.setIndicadorTipoConta(tipoContaSidec);
		assinaturaMultipla.setNsuPermissao(
				Long.valueOf(respSibar.xpathTexto("/*[1]/VALIDA_PERMISSAO_SAIDA/CONTROLE_NEGOCIAL/NSU")));
		assinaturaMultipla.setDataPermissao(
				formataData.parse(respSibar.xpathTexto("/*[1]/VALIDA_PERMISSAO_SAIDA/DATA_TRANSACAO")));
		assinaturaMultipla.setSituacao(SituacaoAssinaturaMultipla.PENDENTE_ASSINATURA);
		assinaturaMultipla.setValor(valor);
		assinaturaMultipla.setXmlNegocial(this.mensagemCanal);
		assinaturaMultipla.setDataEfetivacao(formataData.parse(
				dadosBarramentoAssinatura.xpathTexto("/BUSDATA/*[2]/ASSINATURA_MULTIPLA/DATA_PREVISTA_EFETIVACAO")));
		assinaturaMultipla.setServico(this.versaoServicoChamado.getId().getNuServico001());
		assinaturaMultipla.setVersaoServico(this.versaoServicoChamado.getId().getNuVersaoServico());

		return assinaturaMultipla;
	}

	/**
	 * Salva a transacao.
	 * 
	 * @param header
	 * @param mensagem
	 * @return
	 * @throws Exception
	 */
	private Mtxtb014Transacao criarTransacaoSimtx(Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla)
			throws Exception {
		logger.info("Gravando transacao negocial");

		Mtxtb014Transacao transacao = null;
		Mtxtb004Canal canal = new Mtxtb004Canal();
		try {
			String xml = assinaturaMultipla.getXmlNegocial();
			BuscadorTextoXml buscador = new BuscadorTextoXml(xml);

			canal.setSigla(buscador.xpathTexto("/*[1]/HEADER/CANAL/SIGLA"));
			canal = this.fornecedorDados.buscarCanalPorSigla(canal);
			if (canal == null) {
				canal = new Mtxtb004Canal();
				canal.setNuCanal(Constantes.CODIGO_CANAL_INEXISTENTE.longValue());
			}
			transacao = new Mtxtb014Transacao();
			transacao.setCoCanalOrigem(String.valueOf(canal.getNuCanal()));
			transacao.setIcSituacao(Constantes.IC_SERVICO_EM_ANDAMENTO);
			transacao.setDhMultiCanal(new Date());
			transacao.setDtReferencia(new Date());
			transacao.setDhTransacaoCanal(new Date());
			transacao.setIcEnvio(new BigDecimal(0));
			transacao.setIcRetorno(new BigDecimal(0));
			transacao.setTsAtualizacao(DataUtil.getDataAtual());
			transacao = this.fornecedorDados.salvarTransacao(transacao);

			logger.info("NSUMTX gerado: " + transacao.getNuNsuTransacao());
			logger.info("Situacao da transacao negocial: [" + Constantes.IC_SERVICO_EM_ANDAMENTO + "] Em Andamento");

		} catch (Exception e) {
			throw new SimtxException("MN006");
		}
		return transacao;
	}

	/**
	 * Valida o Header da mensagem recebida pelo canal.
	 * 
	 * @param mensagem
	 * @return
	 * @return
	 * @throws Exception
	 */
	public HeaderSibar lerHeaderDaMensagem(BuscadorTextoXml buscador) throws Exception {
		logger.info("Validando header");
		try {
			Node nodeHeader = buscador.buscarNode("HEADER");
			return new HeaderSibarFactory().createHeaderSibar(nodeHeader);
		} catch (Exception e) {
			logger.error("Erro ao ler header do xml negocial");
			return null;
		}
	}

	/**
	 * 
	 * Metodo para auxiliar a buscar uma tag numa arvore de nodes. TODO: Mover este
	 * metodo para uma classe auxiliadora
	 * 
	 * @param caminho
	 * @param node
	 * @return
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public String xpath(String caminho, Node node) throws ParserConfigurationException, XPathExpressionException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();
		doc.adoptNode(node);
		doc.appendChild(node);

		return (String) XPathFactory.newInstance().newXPath().evaluate(caminho, doc, XPathConstants.STRING);
	}

	/**
	 * Recebe um document de XML e grava no busdata. TODO: Mover este metodo para
	 * uma classe auxiliadora
	 * 
	 * @param doc
	 * @throws Exception
	 */
	public void gravarDocumentNoBusData(Document doc) throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r", "");

		this.transformaMensagemCanal.gravarNoDadosBarramento(output);
	}

	public String getMensagemCanal() {
		return mensagemCanal;
	}

	public void setMensagemCanal(String mensagemCanal) {
		this.mensagemCanal = mensagemCanal;
	}

	public Mtxtb011VersaoServico getVersaoServicoChamado() {
		return versaoServicoChamado;
	}

	public void setVersaoServicoChamado(Mtxtb011VersaoServico versaoServicoChamado) {
		this.versaoServicoChamado = versaoServicoChamado;
	}

	public TransformaMensagemCanal getTransformaMensagemCanal() {
		return transformaMensagemCanal;
	}

	public void setTransformaMensagemCanal(TransformaMensagemCanal transformaMensagemCanal) {
		this.transformaMensagemCanal = transformaMensagemCanal;
	}

	public Mtxtb014Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Mtxtb014Transacao transacao) {
		this.transacao = transacao;
	}

	public Mtxtb001Servico getServicoChamado() {
		return servicoChamado;
	}

	public void setServicoChamado(Mtxtb001Servico servicoChamado) {
		this.servicoChamado = servicoChamado;
	}

	public SimtxConfig getSimtxConfig() {
		return simtxConfig;
	}

	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Mtxtb004Canal getCanal() {
		return canal;
	}

	public void setCanal(Mtxtb004Canal canal) {
		this.canal = canal;
	}

	public String getIdMensagem() {
		return idMensagem;
	}

	public void setIdMensagem(String idMensagem) {
		this.idMensagem = idMensagem;
	}

	public Mtxtb016IteracaoCanal getIteracaoCanal() {
		return iteracaoCanal;
	}

	public void setIteracaoCanal(Mtxtb016IteracaoCanal iteracaoCanal) {
		this.iteracaoCanal = iteracaoCanal;
	}
}
