/*******************************************************************************
 * Copyright (C)  2018 - CAIXA Econômica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.agendamento.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import br.gov.caixa.simtx.agendamento.enuns.ServicoAgendamentoEnum;
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
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb023Parametro;
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
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

/**
 * Classe responsavel por tratar e executar todos os servicos passiveis de Agendamento. Ex: Pagamento Boleto.
 * 
 * @author rsfagundes
 *
 */
@Stateless
public class ConsultaListaAgendamentos extends GerenciadorTransacao implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ConsultaListaAgendamentos.class);
	
	private static String pDataInicioDefault = "pDataInicioDefault";
	
	private static String pDataTerminDefault = "pDataTerminDefault";
	
	protected DadosBarramento dadosBarramento;
	
	@Inject
	protected GerenciadorFilasMQ execucaoMq;
	
	@Inject
	protected GeradorPassosMigrado geradorPassosMigrado;
	
	@Inject
	protected ValidadorRegrasNegocio validadorRegrasNegocio;
	
	@Inject
	protected SimtxConfig simtxConfig;
	
	@Inject
	protected FornecedorDadosAgendamento fornecedorDadosAgendamento;
	
	@Inject
	private RepositorioArquivo repositorioArquivo;
	
	@Inject
	protected AtualizadorDadosTransacao atualizadorDadosTransacao;
	
	@Inject
	protected TratadorDeExcecao tratadorDeExcecao;
	

	
	/**
	 * Trata o servico Lista Agendamentos.
	 * 
	 * @param transacao
	 * @param iteracaoCanal
	 * @param canal
	 * @param servicoOrigem
	 * @param meioEntrada
	 * @param dadosBarramento
	 * @param parametrosAdicionais
	 * @throws ServicoException
	 */
	public void processar(Mtxtb014Transacao transacao, Mtxtb016IteracaoCanal iteracaoCanal, Mtxtb004Canal canal,
			Mtxtb011VersaoServico servicoOrigem, Mtxtb008MeioEntrada meioEntrada, DadosBarramento dadosBarramento,
			ParametrosAdicionais parametrosAdicionais) throws ServicoException {
		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		try {
			logger.info(" ==== Iniciando Processo Lista Agendamento Iniciado ==== ");
			
			this.dadosBarramento = dadosBarramento;
			
			List<Mtxtb003ServicoTarefa> listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servicoOrigem, canal, meioEntrada);

			logger.info("Preparando xml de requisicao para enviar ao Sibar");
			String xmlEntradaSibar = transformarXml(servicoOrigem.getDeXsltRequisicao(), transacao, canal, null);
			xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, servicoOrigem, listaTarefas);
			this.dadosBarramento.escrever(xmlEntradaSibar);

			String xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servicoOrigem.getMtxtb001Servico());
			this.dadosBarramento.escrever(xmlSaidaSibar);

			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servicoOrigem, transacao, this.dadosBarramento, xmlSaidaSibar);
			this.gerenciadorTarefas.validarTarefasServicoResposta(tarefasServicoResposta);
			
			String transacoes = processarListaTransacoes(xmlSaidaSibar, xmlEntradaSibar);
			if(transacoes.isEmpty()) {
				Resposta resposta = recuperarMensagemSemTransacoes();
				tarefasServicoResposta.setResposta(resposta);
			}
			
			this.dadosBarramento.escrever(transacoes);
			
			logger.info("Preparando xml de resposta para enviar ao Canal");
			String xmlSaidaCanal = transformarXml(servicoOrigem.getDeXsltResposta(), transacao, canal, tarefasServicoResposta.getResposta());
			enviarRespostaCanal(xmlSaidaCanal, parametrosAdicionais);
			
			logger.info("Preparando informações das tarefas e transacao");
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servicoOrigem);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			iteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoTarefasIteracoes(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			
			direcionaProcessadorMensagemTransacao(false, statusAtualizacaoTransacao, transacao, servicoOrigem, dadosBarramento, xmlSaidaSibar);
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			parametrosAdicionais.setDadosBarramento(this.dadosBarramento);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(servicoOrigem, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			parametrosAdicionais.setDadosBarramento(this.dadosBarramento);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);		
		}
		finally {
			logger.info(" ==== Finalizando Processo Lista Agendamento Iniciado ==== ");
		}
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
	public String transformarXml(String xslt, Mtxtb014Transacao transacao, Mtxtb004Canal canal, Resposta mensagem)
			throws ServicoException {
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			
			parametrosNovos.add(new ParametroXsl("nsuSimtx", String.valueOf(transacao.getNuNsuTransacao())));
			parametrosNovos.add(new ParametroXsl("pRedeTransmissora", String.valueOf(canal.getNuRedeTransmissora())));
			
			BuscadorTextoXml buscadorTextoXmlInicio = new BuscadorTextoXml(this.dadosBarramento.getDadosLeitura());
			String dtInicio = buscadorTextoXmlInicio.buscarTexto("/BUSDATA/*[2]/LISTA_AGENDAMENTOS_TRANSACOES/PERIODO/DATA_INICIO");
			
			BuscadorTextoXml buscadorTextoXmlTermino = new BuscadorTextoXml(this.dadosBarramento.getDadosLeitura());
			String dtTermin = buscadorTextoXmlTermino.buscarTexto("/BUSDATA/*[2]/LISTA_AGENDAMENTOS_TRANSACOES/PERIODO/DATA_FIM");
			
			String dataMaisUm = null; 
			String dataMaxFinal = null;
			
			if (null == dtInicio && null == dtTermin) {
				dataMaisUm = DataUtil.obterDataFormatada(DataUtil.obterDataFutura(1), DataUtil.FORMATO_DATA_XML);
				Mtxtb023Parametro parametro = this.fornecedorDados.buscarParametroPorPK(Constantes.FLAG_LIM_PAGAMENTO);
				Integer qtdDiasMax = Integer.parseInt(parametro.getDeConteudoParam());
				dataMaxFinal =  DataUtil.obterDataFormatada(DataUtil.obterDataFutura(qtdDiasMax), DataUtil.FORMATO_DATA_XML);
				parametrosNovos.add(new ParametroXsl(pDataInicioDefault, dataMaisUm));
				parametrosNovos.add(new ParametroXsl(pDataTerminDefault, dataMaxFinal));
			}

			if(mensagem != null) {
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigo()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, mensagem.getOrigem()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL,mensagem.getMensagemNegocial()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getMensagemTecnica()));
			}
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];
			
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl, parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}

	public String processarListaTransacoes(String xmlSaidaSibar, String xmlEntradaSibar) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.newDocument();
		Element root = doc.createElement("TRANSACOES");
		doc.adoptNode(root);
		doc.appendChild(root);
		BuscadorTextoXml buscadorSaidaSibar = new BuscadorTextoXml(xmlSaidaSibar);

		Node nodeTransacoes = buscadorSaidaSibar.xpath("/*[1]/CONSULTA_TRANSACOES_CONTA_SAIDA/BOLETO/TRANSACOES");
		if(nodeTransacoes != null) {
			for (int i = 0; i < nodeTransacoes.getChildNodes().getLength(); i++) {
				Node node = nodeTransacoes.getChildNodes().item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String linha = node.getTextContent();
					if (linha.trim().contains("Agendado")) {
						montarTagTransacaoSiaut(node, doc, root, ServicoAgendamentoEnum.CONSULTA_DETALHE_AGENDAMENTO_BOLETO.getServicoOrigem());
					}
				}
			}
		}
		
		BuscadorTextoXml buscadorEntradaSibar = new BuscadorTextoXml(xmlEntradaSibar);

		int unidade = 0;
		int operacao = 0;
		long numero = 0;
		int dv = 0;

		if (!buscadorEntradaSibar.xpathTexto("/*[1]/CONSULTA_TRANSACOES_CONTA_ENTRADA/CONTA").isEmpty()) {
			Node nodeConta = buscadorEntradaSibar.xpath("/*[1]/CONSULTA_TRANSACOES_CONTA_ENTRADA/CONTA");
			unidade = Integer.valueOf(BuscadorTextoXml.xpath("CONTA/UNIDADE", nodeConta));
			operacao = Integer.valueOf(BuscadorTextoXml.xpath("CONTA/PRODUTO", nodeConta));
			numero = Long.parseLong(BuscadorTextoXml.xpath("CONTA/NUMERO", nodeConta));
			dv = Integer.valueOf(BuscadorTextoXml.xpath("CONTA/DV", nodeConta));
		}
		String periodoInicioFmt = null;
		String periodoTerminoFmt = null;
		if (!buscadorEntradaSibar.xpathTexto("/*[1]/CONSULTA_TRANSACOES_CONTA_ENTRADA/PERIODO").isEmpty()) {
			Node nodePeriodo = buscadorEntradaSibar.xpath("/*[1]/CONSULTA_TRANSACOES_CONTA_ENTRADA/PERIODO");
			periodoInicioFmt = String.valueOf(BuscadorTextoXml.xpath("PERIODO/INICIO", nodePeriodo));
			periodoTerminoFmt = String.valueOf(BuscadorTextoXml.xpath("PERIODO/FIM", nodePeriodo));
		}
		
		Mtxtb034TransacaoAgendamento consulta = new Mtxtb034TransacaoAgendamento();
		consulta.setNuUnidade(unidade);
		consulta.setDvConta(dv);
		consulta.setNuProduto(operacao);
		consulta.setNuConta(numero);
		Date periodoInicio 	= DataUtil.formatStringData(periodoInicioFmt, DataUtil.FORMATO_DATA_XML);
		Date periodoTermino = DataUtil.formatStringData(periodoTerminoFmt, DataUtil.FORMATO_DATA_XML);

		List<Mtxtb034TransacaoAgendamento> agendamentos = fornecedorDadosAgendamento
				.buscaTransacoesAgendamentoPeriodo(consulta, periodoInicio, periodoTermino);

		for (Mtxtb034TransacaoAgendamento mtxtb034TransacaoAgendamento : agendamentos) {
			montarTagTransacaoSimtx(doc, root,
					ServicoAgendamentoEnum.CONSULTA_DETALHE_AGENDAMENTO_BOLETO.getServicoOrigem(),
					mtxtb034TransacaoAgendamento);	
		}
		
		if(nodeTransacoes == null && agendamentos.isEmpty())
			return "";
		
		return BuscadorTextoXml.parseDocToXML(doc);
	}

	private void montarTagTransacaoSiaut(Node nodeTransao, Document doc, Element root, Long codigoDetalhe) throws XPathExpressionException, ParserConfigurationException {

			Element transacaoTag = doc.createElement("TRANSACAO");

			Element sistemaOrigem = doc.createElement("SISTEMA_ORIGEM");
			Element nsu = doc.createElement("NSU");
			Element dataAgendamento = doc.createElement("DATA_AGENDAMENTO");
			Element dataEfetivacao = doc.createElement("DATA_EFETIVACAO");
			Element valor = doc.createElement("VALOR");
			Element identificador = doc.createElement("IDENTIFICADOR");
			Element identificadorCancelamento = doc.createElement("IDENTIFICADOR_CANCELAMENTO");
			Element cdDetalheLista = doc.createElement("CODIGO_SERVICO_DETALHE");

			sistemaOrigem.appendChild(doc.createTextNode("SIAUT"));
			nsu.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/NSU", nodeTransao)));
			dataAgendamento
					.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/DATA_PAGAMENTO", nodeTransao)));
			dataEfetivacao
					.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/DATA_EFETIVACAO", nodeTransao)));
			valor.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/VALOR", nodeTransao)));
			identificador
					.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/IDENTIFICADOR", nodeTransao)));
			identificadorCancelamento
					.appendChild(doc.createTextNode(BuscadorTextoXml.xpath("/TRANSACAO/SITUACAO", nodeTransao)));
			cdDetalheLista.appendChild(doc.createTextNode(codigoDetalhe.toString()));

			transacaoTag.appendChild(sistemaOrigem);
			transacaoTag.appendChild(nsu);
			transacaoTag.appendChild(dataAgendamento);
			transacaoTag.appendChild(dataEfetivacao);
			transacaoTag.appendChild(identificador);
			transacaoTag.appendChild(identificadorCancelamento);
			transacaoTag.appendChild(cdDetalheLista);

			root.appendChild(transacaoTag);
	}

	private void montarTagTransacaoSimtx(Document doc, Element root, Long codigoDetalhe, Mtxtb034TransacaoAgendamento agendamento) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Element transacaoTag = doc.createElement("TRANSACAO");

			Element sistemaOrigem = doc.createElement("SISTEMA_ORIGEM");
			Element nsu = doc.createElement("NSU");
			Element dataAgendamento = doc.createElement("DATA_AGENDAMENTO");
			Element dataEfetivacao = doc.createElement("DATA_EFETIVACAO");
			Element valor = doc.createElement("VALOR");
			Element identificador = doc.createElement("IDENTIFICACAO");
			Element identificadorCancelamento = doc.createElement("IDENTIFICADOR_CANCELAMENTO");
			Element cdDetalheLista = doc.createElement("CODIGO_SERVICO_DETALHE");

			sistemaOrigem.appendChild(doc.createTextNode("SIMTX"));
			nsu.appendChild(doc.createTextNode(String.valueOf(agendamento.getNuNsuTransacaoAgendamento())));
			dataAgendamento.appendChild(doc.createTextNode(sdf.format(agendamento.getDtReferencia())));
			dataEfetivacao.appendChild(doc.createTextNode(sdf.format(agendamento.getDtEfetivacao())));
			valor.appendChild(doc.createTextNode(String.valueOf(agendamento.getValorTransacao())));
			identificador.appendChild(doc.createTextNode("1"));
			identificadorCancelamento.appendChild(doc.createTextNode("AGENDADO"));
			cdDetalheLista.appendChild(doc.createTextNode(String.valueOf(codigoDetalhe)));

			transacaoTag.appendChild(sistemaOrigem);
			transacaoTag.appendChild(nsu);
			transacaoTag.appendChild(valor);
			transacaoTag.appendChild(dataAgendamento);
			transacaoTag.appendChild(dataEfetivacao);
			transacaoTag.appendChild(identificador);
			transacaoTag.appendChild(identificadorCancelamento);
			transacaoTag.appendChild(cdDetalheLista);

			root.appendChild(transacaoTag);
	}

	/**
	 * Recupera a mensagem quando nao possuir transacoes no SIAUT e nem na base do
	 * SIMTX.
	 * 
	 * @return Resposta
	 * @throws ServicoException
	 */
	public Resposta recuperarMensagemSemTransacoes() throws ServicoException {
		try {
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.NAO_ENCONTROU_RESULTADOS);
			Resposta resposta = new Resposta();
			resposta.setIcTipoMensagem(mensagem.getIcTipoMensagem());
			resposta.setCodigo(mensagem.getCodigoRetorno());
			resposta.setMensagemNegocial(mensagem.getDeMensagemNegocial());
			resposta.setMensagemTecnica(mensagem.getDeMensagemTecnica());
			resposta.setOrigem(Constantes.ORIGEM_SIMTX);
			return resposta;
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
}
