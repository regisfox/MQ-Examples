package br.gov.caixa.simtx.assinatura.multipla.servico;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import br.gov.caixa.simtx.persistencia.assinaturamultipla.entidade.Mtxtb027TransacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.assinaturamultipla.vo.SituacaoAssinaturaMultipla;
import br.gov.caixa.simtx.persistencia.atualizador.dados.AtualizadorDadosTransacao;
import br.gov.caixa.simtx.persistencia.cache.assinaturamultipla.FornecedorDadosAssinaturaMultipla;
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
import br.gov.caixa.simtx.util.ParametrosAdicionais;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.GerenciadorTransacao;
import br.gov.caixa.simtx.util.gerenciador.TratadorDeExcecao;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;
import br.gov.caixa.simtx.util.gerenciador.servico.ProcessadorServicos;
import br.gov.caixa.simtx.util.mensagem.GerenciadorFilasMQ;
import br.gov.caixa.simtx.util.to.DadosTransacaoComuns;
import br.gov.caixa.simtx.util.to.TarefasServicoResposta;
import br.gov.caixa.simtx.util.xml.BuscadorTextoXml;
import br.gov.caixa.simtx.util.xml.DadosBarramento;
import br.gov.caixa.simtx.util.xml.GeradorPassosMigrado;
import br.gov.caixa.simtx.util.xml.ParametroXsl;
import br.gov.caixa.simtx.util.xml.Resposta;
import br.gov.caixa.simtx.util.xml.TransformadorXsl;

@Stateless
public class ListaAssinaturaMultipla extends GerenciadorTransacao implements ProcessadorServicos {
	
	private static final long serialVersionUID = 2495284182317849743L;

	private static final Logger logger = Logger.getLogger(ListaAssinaturaMultipla.class);
	
	private static final String PROCESSO_FINALIZADO = " ==== Processo Lista Assinatura Multipla Finalizado ==== ";
	
	private DadosBarramento dadosBarramento;
	
	@Inject
	private FornecedorDadosAssinaturaMultipla fornecedorDadosAssinaturaMultipla;
	
	@Inject
	private GeradorPassosMigrado geradorPassosMigrado;
	
	@Inject
	private AtualizadorDadosTransacao atualizadorDadosTransacao;
	
	@Inject
	private GerenciadorFilasMQ execucaoMq;
	
	@Inject
	private TratadorDeExcecao tratadorDeExcecao;

	@Inject
	private RepositorioArquivo repositorioArquivo;
	
	@Inject
	private SimtxConfig simtxConfig;

	
	@Override
	public void processar(ParametrosAdicionais parametrosAdicionais) {
		Mtxtb014Transacao transacao = null;
		Mtxtb016IteracaoCanal iteracaoCanal = null;
		Mtxtb011VersaoServico servico = null;
		Mtxtb004Canal canal = null;
		
		String xmlEntradaSibar = null;
		String xmlSaidaSibar = null;

		TarefasServicoResposta tarefasServicoResposta = new TarefasServicoResposta();
		List<Mtxtb003ServicoTarefa> listaTarefas = null;
		
		try {
			logger.info(" ==== Processo Lista Assinatura Multipla Iniciado ==== ");
			
			this.dadosBarramento = new DadosBarramento();
			this.dadosBarramento.escrever(parametrosAdicionais.getXmlMensagem());

			BuscadorTextoXml buscador = new BuscadorTextoXml(parametrosAdicionais.getXmlMensagem());
			
			Long codigoServico = Long.parseLong(buscador.xpathTexto("/*[1]/HEADER/SERVICO/CODIGO"));
			Integer versaoServico = Integer.parseInt(buscador.xpathTexto("/*[1]/HEADER/SERVICO/VERSAO"));
			servico = buscarServico(codigoServico, versaoServico);
			
			canal = buscarCanal(buscador);

			transacao = salvarTransacao(buscador, canal);
			iteracaoCanal = salvarIteracaoCanal(buscador, parametrosAdicionais.getXmlMensagem(), transacao);
			salvarTransacaoServico(transacao, servico);

			int nuMeioEntrada = Integer.parseInt(buscador.xpathTexto("/*[1]/HEADER/MEIOENTRADA"));
			Mtxtb008MeioEntrada meioEntrada = new Mtxtb008MeioEntrada();
			meioEntrada.setNuMeioEntrada(nuMeioEntrada);
			meioEntrada = this.fornecedorDados.buscarMeioEntrada(nuMeioEntrada);

			this.validadorRegrasNegocio.validarRegrasMigrado(parametrosAdicionais.getXmlMensagem(), canal, meioEntrada, servico);
			
			listaTarefas = this.gerenciadorTarefas.carregarTarefasServico(servico, canal, meioEntrada);

			logger.info("Preparando xml de requisicao para enviar ao Sibar");
			xmlEntradaSibar = transformarXml(servico, transacao, canal);
			xmlEntradaSibar = this.geradorPassosMigrado.gerarPassos(xmlEntradaSibar, servico, listaTarefas);
			this.dadosBarramento.escrever(xmlEntradaSibar);

			xmlSaidaSibar = this.execucaoMq.executar(xmlEntradaSibar, servico.getMtxtb001Servico());
			this.dadosBarramento.escrever(xmlSaidaSibar);

			List<Mtxtb027TransacaoAssinaturaMultipla> listTransacoes = buscarTransacoesSimtx(parametrosAdicionais.getXmlMensagem());
			verificarSePossuiTransacoes(listTransacoes);
			if(!listTransacoes.isEmpty()) {
				montarTransacoesSimtx(listTransacoes);
			}
			
			logger.info("Preparando xml de resposta para enviar ao Canal");
			tarefasServicoResposta = this.gerenciadorTarefas.montarTarefasResposta(listaTarefas, servico, transacao, this.dadosBarramento, xmlSaidaSibar);
			String xmlSaidaCanal = transformarXml(servico, transacao, tarefasServicoResposta.getResposta());
			logger.info("Enviando resposta para Canal");
			enviarRespostaCanal(xmlSaidaCanal, parametrosAdicionais);
			
			this.gerenciadorTarefas.validarTarefasServicoResposta(tarefasServicoResposta);
			
			logger.info("Atualizando informacoes das tarefas e transacao");
			BigDecimal situacaoTransacao = this.validadorRegrasNegocio.situacaoFinalTransacao(servico);
			transacao = atualizaStatusTransacao(transacao, situacaoTransacao);
			iteracaoCanal = montaMtxtb016IteracaoCanalTansacao(iteracaoCanal, transacao, xmlSaidaCanal);
			
			boolean statusAtualizacaoTransacao = this.atualizadorDadosTransacao.atualizarDadosTransacaoTarefasIteracoes(transacao, tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			
			direcionaProcessadorMensagemTransacao(true, statusAtualizacaoTransacao, transacao, servico, this.dadosBarramento, xmlSaidaSibar);
			logger.info(PROCESSO_FINALIZADO);
		} 
		catch (ServicoException se) {
			logger.error(se.getMensagem());
			parametrosAdicionais.setDadosBarramento(this.dadosBarramento);
			parametrosAdicionais.setXmlMensagem(xmlSaidaSibar);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(listaTarefas, servico, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
			logger.info(PROCESSO_FINALIZADO);
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			ServicoException se = new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
			parametrosAdicionais.setDadosBarramento(dadosBarramento);
			parametrosAdicionais.setXmlMensagem(xmlSaidaSibar);
			DadosTransacaoComuns dadosTransacaoComuns = new DadosTransacaoComuns(listaTarefas, servico, transacao,tarefasServicoResposta.getListaTransacaoTarefas(), iteracaoCanal);
			this.tratadorDeExcecao.tratarExcecaoRetomada(dadosTransacaoComuns, se, parametrosAdicionais, true);
			logger.info(PROCESSO_FINALIZADO);
		}
	}
	
	public List<Mtxtb027TransacaoAssinaturaMultipla> buscarTransacoesSimtx(String xmlEntradaCanal) {
		try {
			logger.info("Buscando transacoes pendentes no Simtx");
			BuscadorTextoXml buscador = new BuscadorTextoXml(xmlEntradaCanal);
			Node tag = buscador.xpath(Constantes.PATH_CONTA_SIDEC);
			int tipoConta = 1;
			String tipoContaTexto = Constantes.PATH_CONTA_SIDEC;
			String operacao = "/OPERACAO";
			if (tag == null) {
				tipoConta = 2;
				tipoContaTexto = "/*[1]/CONTA/CONTA_NSGD";
				operacao = "/PRODUTO";
			}
			int nuUnidade 	= Integer.parseInt(buscador.xpathTexto(tipoContaTexto+"/UNIDADE"));
			int nuProduto 	= Integer.parseInt(buscador.xpathTexto(tipoContaTexto+operacao));
			long nuConta 	= Long.parseLong(buscador.xpathTexto(tipoContaTexto+"/CONTA"));
			int dvConta 	= Integer.parseInt(buscador.xpathTexto(tipoContaTexto+"/DV"));
	
			return this.fornecedorDadosAssinaturaMultipla.buscarAssinaturasMultiplasPendentes(nuUnidade, nuProduto,
					nuConta, dvConta, tipoConta);
		} 
		catch (Exception e) {
			logger.error("Nao foi possivel buscar as transacoes pendentes no simtx", e);
			return new ArrayList<>();
		}
	}
	
	public void montarTransacoesSimtx(List<Mtxtb027TransacaoAssinaturaMultipla> listTransacoes) {
		try {
			logger.info("Montando tag de transacoes do simtx");
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element controle = doc.createElement("TRANSACOES");
			doc.appendChild(controle);
			
			SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			for (Mtxtb027TransacaoAssinaturaMultipla assinaturaMultipla : listTransacoes) {

				Element transacao = doc.createElement("TRANSACAO");
				
				Element nsuSiper = doc.createElement("NSU_SIPER");
				nsuSiper.appendChild(doc.createTextNode(String.valueOf(assinaturaMultipla.getNsuPermissao())));
				transacao.appendChild(nsuSiper);
				
				Element dtSiper = doc.createElement("DATA_TRANSACAO");
				dtSiper.appendChild(doc.createTextNode(dtFormat.format(assinaturaMultipla.getDataPermissao())));
				transacao.appendChild(dtSiper);
				
				Element dtPrevista = doc.createElement("DATA_PREVISTA_EFETIVACAO");
				dtPrevista.appendChild(doc.createTextNode(dtFormat.format(assinaturaMultipla.getDataEfetivacao())));
				transacao.appendChild(dtPrevista);
				
				Element situacao = doc.createElement("SITUACAO");
				situacao.appendChild(doc.createTextNode(SituacaoAssinaturaMultipla.PENDENTE_ASSINATURA.getDescricaoXsd()));
				transacao.appendChild(situacao);
				
				Element sistemaOrigem = doc.createElement("SISTEMA_ORIGEM");
				
				Element tagSimtx = doc.createElement("SIMTX");
				
				Element nsu = doc.createElement("NSU");
				nsu.appendChild(doc.createTextNode(String.valueOf(assinaturaMultipla.getNsuAssinaturaMultipla())));
				tagSimtx.appendChild(nsu);
				
				Element codServico = doc.createElement("CODIGO_SERVICO");
				codServico.appendChild(doc.createTextNode(String.valueOf(assinaturaMultipla.getServico())));
				tagSimtx.appendChild(codServico);
				
				Element vrTransacao = doc.createElement("VALOR_TRANSACAO");
				vrTransacao.appendChild(doc.createTextNode(String.valueOf(assinaturaMultipla.getValor())));
				tagSimtx.appendChild(vrTransacao);
			
				sistemaOrigem.appendChild(tagSimtx);
				transacao.appendChild(sistemaOrigem);
				controle.appendChild(transacao);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StringWriter stringWriter = new StringWriter();
			StreamResult result = new StreamResult(stringWriter);
			transformer.transform(source, result);
			this.dadosBarramento.escrever(stringWriter.toString().replaceAll("\n|\r", ""));
		} 
		catch (Exception e) {
			logger.error(e);
		}
	}
	
	public void verificarSePossuiTransacoes(List<Mtxtb027TransacaoAssinaturaMultipla> listTransacoes) throws ServicoException {
		try {
			logger.info("Verificando se possui transacoes no Siaut e/ou no Simtx");
			String transacoesSiaut = this.dadosBarramento.xpathTexto("/BUSDATA/*[local-name()='SERVICO_SAIDA']/*[local-name()='CONSULTA_ASSINATURA_ELETRONICA_SAIDA']/*[local-name()='TRANSACOES']");
			if(transacoesSiaut.isEmpty() && listTransacoes.isEmpty()) {
				logger.error("Nao ha transacoes no Siaut e no Simtx");
				Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.NAO_ENCONTROU_RESULTADOS);
				throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
			}
		} 
		catch (ServicoException e) {
			throw e;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Transforma o xml em outro a partir do xslt.
	 * 
	 * @param versaoServico
	 * @param transacao
	 * @param canal
	 * @return
	 * @throws ServicoException
	 */
	public String transformarXml(Mtxtb011VersaoServico versaoServico, Mtxtb014Transacao transacao, Mtxtb004Canal canal) throws ServicoException {
		logger.info("inicializando tranformacao xml");
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_NSU_SIMTX, String.valueOf(transacao.getNuNsuTransacao())));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_REDE_TRANSMISSORA, String.valueOf(canal.getNuRedeTransmissora())));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_SEGMENTO, String.valueOf(canal.getNuSegmento())));

			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];

			String caminhoXls = this.simtxConfig.getCaminhoXslt() + versaoServico.getDeXsltRequisicao();
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl, parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Transforma o xml em outro a partir do xslt.
	 * 
	 * @param versaoServico
	 * @param transacao
	 * @param mensagem
	 * @return
	 * @throws ServicoException
	 */
	public String transformarXml(Mtxtb011VersaoServico versaoServico, Mtxtb014Transacao transacao, Resposta mensagem) throws ServicoException {
		logger.info("inicializando tranformacao xml");
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_NSU_SIMTX, String.valueOf(transacao.getNuNsuTransacao())));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigo()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, mensagem.getOrigem()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL, mensagem.getMensagemNegocial()));
			parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getMensagemTecnica()));
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];
			
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + versaoServico.getDeXsltResposta();
			String arquivoXsl = this.repositorioArquivo.recuperarArquivo(caminhoXls);
			return new TransformadorXsl().transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl, parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	public void setFornecedorDadosAssinaturaMultipla(FornecedorDadosAssinaturaMultipla fornecedorDadosAssinaturaMultipla) {
		this.fornecedorDadosAssinaturaMultipla = fornecedorDadosAssinaturaMultipla;
	}

	public void setGeradorPassosMigrado(GeradorPassosMigrado geradorPassosMigrado) {
		this.geradorPassosMigrado = geradorPassosMigrado;
	}

	public void setTratadorDeExcecao(TratadorDeExcecao tratadorDeExcecao) {
		this.tratadorDeExcecao = tratadorDeExcecao;
	}

	public void setValidadorRegrasNegocio(ValidadorRegrasNegocio validadorRegrasNegocio) {
		this.validadorRegrasNegocio = validadorRegrasNegocio;
	}

	public void setAtualizadorDadosTransacao(AtualizadorDadosTransacao atualizadorDadosTransacao) {
		this.atualizadorDadosTransacao = atualizadorDadosTransacao;
	}

	public void setExecucaoMq(GerenciadorFilasMQ execucaoMq) {
		this.execucaoMq = execucaoMq;
	}

	public void setRepositorioArquivo(RepositorioArquivo repositorioArquivo) {
		this.repositorioArquivo = repositorioArquivo;
	}

	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}
	
	public void setDadosBarramento(DadosBarramento dadosBarramento) {
		this.dadosBarramento = dadosBarramento;
	}

	public DadosBarramento getDadosBarramento() {
		return dadosBarramento;
	}

}
