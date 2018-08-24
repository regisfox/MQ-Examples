/*******************************************************************************
 * Copyright (C)  2017 - CAIXA EconÃ´mica Federal 
 * 
 * Todos os direitos reservados
 ******************************************************************************/
package br.gov.caixa.simtx.util.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.AcaoRetorno;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb001Servico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb004Canal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb005ServicoCanal;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagemPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb008MeioEntrada;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb012VersaoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb014Transacao;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb015SrvcoTrnsoTrfaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFila;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFilaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegras;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegrasPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb032MarcaConta;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.StringGravavel;
import br.gov.caixa.simtx.util.StringUtil;
import br.gov.caixa.simtx.util.XmlProcessador;
import br.gov.caixa.simtx.util.data.DataUtil;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.exception.SimtxException;
import br.gov.caixa.simtx.util.gerenciador.ValidadorRegrasNegocio;
import br.gov.caixa.simtx.util.gerenciador.cache.RepositorioArquivo;

/**
 * Classe responsavel por tratar a mensagem de entrada e saida do Canal.
 * 
 * @author rctoscano
 *
 */
public class TransformaMensagemCanal implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(TransformaMensagemCanal.class);
	
	private DadosBarramento dadosBarramento;
	
	private FornecedorDados fornecedorDados;
	
	private Mtxtb014Transacao transacao;
	
	private Mtxtb001Servico servicoChamado;
	
	private Mtxtb011VersaoServico versaoServicoChamado;
	
	private Mtxtb004Canal canal;
	
	private Mtxtb008MeioEntrada meioEntrada;
	
	private Mtxtb005ServicoCanal servicoCanal;
	
	private List<Mtxtb003ServicoTarefa> listaTarefaExecutar;
	
	private SimtxConfig simtxConfig;
	
	protected Properties properties;
	
	private RepositorioArquivo repositorio;
	
	private TransformadorXsl transformadorXsl;
	
	private StringBuilder funcionalidades;
	
	/**
	 * Construtor 
	 * mensagem - String - Xml que sera transformado
	 * @throws Exception 
	 * 
	 */
	public TransformaMensagemCanal() {
		this.dadosBarramento = new DadosBarramento();
		this.transformadorXsl = new TransformadorXsl();
		this.simtxConfig = new SimtxConfig();
	}
	
	/**
	 * Processa a mensagem de requisicao do canal e 
	 * prepara o xml para enviar ao Sibar.
	 * 
	 * @param mensagemCanal
	 * @return
	 */
	public String processaEntrada(String mensagem) throws Exception {
		prepararBusData(mensagem);
		
		String resposta = "";
		List<Mtxtb003ServicoTarefa> listaTarefaExecutarEntrada = carregaTarefas(this.transacao);
		if(this.getVersaoServicoChamado().isMigrado()) {
			resposta = preparaXmlEntradaSibar();

			Document d = criaTagControleTransacao(this.versaoServicoChamado, listaTarefaExecutarEntrada);
			resposta = adicionaNovaTag(resposta, d); 
		}
		else {
			resposta = preparaXmlEntradaSibar(); 
		}
		
		this.dadosBarramento.escrever(resposta);
		return resposta;
	}
	
	public String processaSaida(String mensagemSaida, ValidadorRegrasNegocio validadorRegrasNegocio) throws ServicoException, Exception {
		gravarNoDadosBarramento(mensagemSaida);
		salvarTarefasExecutadas();

		Resposta respostaXml = null;
		
		if (this.getVersaoServicoChamado().isMigrado()) {
			for (Mtxtb003ServicoTarefa tarefa : this.listaTarefaExecutar) {
				String mensagemTarefa = preparaXmlComXslt(tarefa.getMtxtb012VersaoTarefa().getDeXsltResposta());

				if(mensagemTarefa.equals("")) {
					mensagemTarefa = "<semMsg>vazio</semMsg>";
				}
				
				BuscadorTextoXml buscadorTarefa = new BuscadorTextoXml(mensagemTarefa);
				
				if(tarefa.isImpeditiva()) {
					respostaXml = validadorRegrasNegocio.verificarCodigoImpeditivo(buscadorTarefa, 
							new BuscadorTextoXml(mensagemSaida),
							tarefa.getMtxtb012VersaoTarefa());
				}
			}
		}
		
		logger.info("Preparando xml de resposta para enviar ao Canal");
		return transformarXml(this.versaoServicoChamado.getDeXsltResposta(), this.transacao, respostaXml);
	}
	
	public Mtxtb006Mensagem buscarMensagemTarefa(String codigoMensagem, Mtxtb012VersaoTarefa vTarefa) throws Exception {
		return this.fornecedorDados.buscarMensagemPorTarefaCodRetorno(codigoMensagem, vTarefa).getMtxtb006Mensagem();
	}
	
	/**
	 * Busca as tarefas a serem executadas pelo Sibar.
	 * 
	 * @param mensagem
	 * @param transacao
	 * @return
	 * @throws Exception
	 */
	protected List<Mtxtb003ServicoTarefa> carregaTarefas(Mtxtb014Transacao transacao) throws Exception {
        logger.info("Carregando tarefas");
        long nuServico = this.servicoChamado.getNuServico();
        long nuVersaoServico = this.versaoServicoChamado.getId().getNuVersaoServico();
        long nuCanal = this.canal.getNuCanal();
        long nuMeioEntrada = this.getMeioEntrada().getNuMeioEntrada();
        
        List<Long> lista = new ArrayList<>();
        List<Object> obj = new ArrayList<>();
        
        if(this.versaoServicoChamado.isMigrado()) {
        	this.listaTarefaExecutar = this.fornecedorDados.buscarTarefasExecutar(nuServico,
    				nuVersaoServico, nuMeioEntrada, nuCanal);
        }
        else {
			this.listaTarefaExecutar = this.fornecedorDados.buscarTarefasExecutar(nuServico,
					nuVersaoServico, nuCanal);
        }
        
        for(Mtxtb003ServicoTarefa mtxtb003ServicoTarefa : this.listaTarefaExecutar) {
			if(!lista.contains(mtxtb003ServicoTarefa.getId().getNuTarefa012())) {
				logger.info("Tarefa ["+mtxtb003ServicoTarefa.getId().getNuTarefa012()+"] "
						+ mtxtb003ServicoTarefa.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoTarefa()+"");
	        	Mtxtb015SrvcoTrnsoTrfa transacaoTarefa = new Mtxtb015SrvcoTrnsoTrfa();
		        transacaoTarefa.setId(new Mtxtb015SrvcoTrnsoTrfaPK());
		        transacaoTarefa.getId().setNuNsuTransacao017(transacao.getNuNsuTransacao());
		        transacaoTarefa.getId().setNuTarefa012(mtxtb003ServicoTarefa.getId().getNuTarefa012());
		        transacaoTarefa.getId().setNuVersaoTarefa012(mtxtb003ServicoTarefa.getId().getNuVersaoTarefa012());
		        transacaoTarefa.getId().setNuServico017(nuServico);
		        transacaoTarefa.getId().setNuVersaoServico017(nuVersaoServico);
		        transacaoTarefa.setDtReferencia(transacao.getDtReferencia());
		        transacaoTarefa.setNsuCorp(0L);
		        transacaoTarefa.setTsExecucaoTransacao(DataUtil.getDataAtual());
		        Mtxtb015SrvcoTrnsoTrfa tarefaTransacao = this.fornecedorDados.salvarTransacaoTarefa(transacaoTarefa);
		        if (tarefaTransacao == null) {
		            throw new SimtxException("MN025");
		        }
		        lista.add(mtxtb003ServicoTarefa.getId().getNuTarefa012());
			}
			else{
				obj.add(mtxtb003ServicoTarefa);
			}
        }
        for(Object ob : obj) {
        	this.listaTarefaExecutar.remove(ob);
        }
        
        this.funcionalidades = new StringBuilder();
		List<Mtxtb032MarcaConta> listaServico = this.fornecedorDados.buscarMarcasPorServico(nuServico);
		if(!listaServico.isEmpty()) {
			for(Mtxtb032MarcaConta servico: listaServico){
				this.funcionalidades.append("<FUNCIONALIDADE_ESPECIAL>"
					+ servico.getCoMarcaConta() + "</FUNCIONALIDADE_ESPECIAL>");
			}
		}
        
        return this.listaTarefaExecutar;
    }
	
	/**
	 * Cria a ORQUESTRACAO.
	 * 
	 * @param listaTarefaExecutar
	 * @return
	 */
	public Document criaTagControleTransacao(Mtxtb011VersaoServico versaoServicoChamado,
			List<Mtxtb003ServicoTarefa> listaTarefaExecutar) throws Exception {
		logger.info("Criando nova TAG Orquestracao");

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		//root elements
		Document doc = docBuilder.newDocument();
		Element controle = doc.createElement("ORQUESTRACAO");
		doc.appendChild(controle);
		
		/** Passos */
		Element passos = doc.createElement("PASSOS");
		for(Mtxtb003ServicoTarefa servicoTarefa: listaTarefaExecutar) {
			Mtxtb024TarefaFilaPK tarefaPK = new Mtxtb024TarefaFilaPK();
            tarefaPK.setNuTarefa012(servicoTarefa.getId().getNuTarefa012());
            tarefaPK.setNuVersaoTarefa012(servicoTarefa.getId().getNuVersaoTarefa012());
    		logger.info("Passo - servicoTarefa: " + servicoTarefa.getId().getNuTarefa012());
            List<Mtxtb024TarefaFila> tarefaFila = this.fornecedorDados.buscarTarefasFilas(tarefaPK);
     
            
            Mtxtb026ServicoTarefaRegrasPK regrasPK = new Mtxtb026ServicoTarefaRegrasPK();
            regrasPK.setNuServico003(versaoServicoChamado.getMtxtb001Servico().getNuServico());
            regrasPK.setNuVersaoServico003(versaoServicoChamado.getId().getNuVersaoServico());
            regrasPK.setNuTarefa003(servicoTarefa.getId().getNuTarefa012());
            regrasPK.setNuVersaoTarefa003(servicoTarefa.getId().getNuVersaoTarefa012());
            List<Mtxtb026ServicoTarefaRegras> listaRegras = this.fornecedorDados.buscarRegrasPorServicoTarefa(regrasPK);
            
            
            Mtxtb007TarefaMensagemPK tarefaMensagemPK = new Mtxtb007TarefaMensagemPK();
            tarefaMensagemPK.setNuTarefa012(servicoTarefa.getId().getNuTarefa012());
            tarefaMensagemPK.setNuVersaoTarefa012(servicoTarefa.getId().getNuVersaoTarefa012());
			List<Mtxtb007TarefaMensagem> tarefasMensagem = this.fornecedorDados
					.buscarAutorizadorasPorTarefa(tarefaMensagemPK);
            
			Element passo = doc.createElement("PASSO");
			
			Element nomeServico = doc.createElement("SERVICO");
			nomeServico.appendChild(doc.createTextNode(String.valueOf(servicoTarefa.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoServicoBarramento())));
			passo.appendChild(nomeServico);

			Element nomeOperacao = doc.createElement("OPERACAO");
			nomeOperacao.appendChild(doc.createTextNode(String.valueOf(servicoTarefa.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoOperacaoBarramento())));
			passo.appendChild(nomeOperacao);
			
			Element versaoServico = doc.createElement("VERSAO");
			versaoServico.appendChild(doc.createTextNode(servicoTarefa.getMtxtb012VersaoTarefa().getVersaoBarramento()));
			passo.appendChild(versaoServico);
			
			String integracao = tarefaFila.get(0).getNoModoIntegracao();
			
			Element modoIntegracao = doc.createElement("MODO_INTEGRACAO");
			modoIntegracao.appendChild(doc.createTextNode(integracao));
			passo.appendChild(modoIntegracao);
			
			Element recursos = doc.createElement("RECURSOS");
			
			if(integracao.equals("MQ")) {
				Element filaReq = doc.createElement("FILA_REQUISICAO");
				filaReq.appendChild(doc.createTextNode(tarefaFila.get(0).getNoRecurso()));
				recursos.appendChild(filaReq);
	
				Element filaRsp = doc.createElement("FILA_RESPOSTA");
				filaRsp.appendChild(doc.createTextNode(tarefaFila.get(0).getNoRecurso().replace("REQ", "RSP")));
				recursos.appendChild(filaRsp);
			}
			else {
				Element url = doc.createElement("URL");
				url.appendChild(doc.createTextNode(tarefaFila.get(0).getNoRecurso()));
				recursos.appendChild(url);
			}
			passo.appendChild(recursos);
			
			Element timeout = doc.createElement("TEMPO_ESPERA");
			timeout.appendChild(doc.createTextNode(String.valueOf(tarefaFila.get(0).getQtdeTempoEspera())));
			passo.appendChild(timeout);
			
			String impede = servicoTarefa.getIcImpedimento() == BigDecimal.ZERO ? "N" : "S";
 			Element impedimento = doc.createElement("IMPEDIMENTO");
			impedimento.appendChild(doc.createTextNode(impede));
			passo.appendChild(impedimento);
			
			String forma = servicoTarefa.getMtxtb012VersaoTarefa().getIcAssincrono() == BigDecimal.ZERO ? "ASSINCRONA" : "SINCRONA";
			Element formaIntegracao = doc.createElement("FORMA_INTEGRACAO");
			formaIntegracao.appendChild(doc.createTextNode(forma));
			passo.appendChild(formaIntegracao);

			/** Regras */
			if(listaRegras != null && !listaRegras.isEmpty()) {
				Element dependencias = doc.createElement("DEPENDENCIAS");
				for(Mtxtb026ServicoTarefaRegras servicoTarefaRegra: listaRegras) {
					Element dependencia = doc.createElement("DEPENDENCIA");
					
					Element servicoOrigem = doc.createElement("SERVICO");
					servicoOrigem.appendChild(doc.createTextNode(servicoTarefaRegra.getMtxtb025RegraProcessamento().getNoServicoOrigem()));
					dependencia.appendChild(servicoOrigem);

					Element campoOrigem = doc.createElement("CAMPO_ORIGEM");
					
					Element caminho = doc.createElement("NOME");
					caminho.appendChild(doc.createTextNode(servicoTarefaRegra.getMtxtb025RegraProcessamento().getDeCaminhoInformacao()));
					campoOrigem.appendChild(caminho);
					dependencia.appendChild(campoOrigem);

					Element campoDestino = doc.createElement("CAMPO_DESTINO");
					
					Element nomeDestino = doc.createElement("NOME");
					nomeDestino.appendChild(doc.createTextNode(servicoTarefaRegra.getMtxtb025RegraProcessamento().getNoCampoDependencia()));
					campoDestino.appendChild(nomeDestino);
					dependencia.appendChild(campoDestino);
				
					dependencias.appendChild(dependencia);
				}
				passo.appendChild(dependencias);
			}
			
			Element retorno = doc.createElement("REGRAS_RETORNO_SUCESSO");
			
			Element campo = doc.createElement("CAMPO");
			campo.appendChild(doc.createTextNode(tarefasMensagem.get(0).getNoCampoRetorno()));
			retorno.appendChild(campo);

			for(Mtxtb007TarefaMensagem tarefaMensagem : tarefasMensagem) {
				Element resultado = doc.createElement("RESULTADO_ESPERADO");
				resultado.appendChild(doc.createTextNode(tarefaMensagem.getMtxtb006Mensagem().getCoMensagem()));
				retorno.appendChild(resultado);
				passo.appendChild(retorno);
			}
			
			passos.appendChild(passo);
		}
		controle.appendChild(passos);
		
		return doc;
	}
	
	/**
	 * Acopla a a TAG ORQUESTRACAO depois do Header.
	 * 
	 * @param xml
	 * @param novaTag
	 * @return
	 */
	public String adicionaNovaTag(String xml, Document novaTag) throws Exception {
		logger.debug("Adicionando nova TAG no xml");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document xmlDocument;
		docBuilder = docFactory.newDocumentBuilder();
		xmlDocument = docBuilder.parse(new ByteArrayInputStream(xml.getBytes(Constantes.ENCODE_PADRAO)));
	
		Node nodeHeader = XmlUtils.procurarHeader(xmlDocument);
		adicionaTagControleTransacao(novaTag, nodeHeader, xmlDocument);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(xmlDocument);
		StringWriter stringWriter = new StringWriter();
		StreamResult result = new StreamResult(stringWriter);
		transformer.transform(source, result);
		return stringWriter.toString();
    }
	
	private void adicionaTagControleTransacao(Node novaTag, Node header, Document xml) {
		for ( int i = 0; i < novaTag.getChildNodes().getLength();  i++) {
			Node nodeCriado = novaTag.getChildNodes().item(i);
			Node tagDepoisDoHeader = getNextTag(header);
			header.getParentNode().insertBefore(xml.importNode(nodeCriado, true), tagDepoisDoHeader);
		}
	}
	
	private Node getNextTag(Node node) {
		Node atual = node.getNextSibling();
		for(int i=0; i < 10; i++) {
			if(atual.getNodeType() == Node.TEXT_NODE) {
				atual = atual.getNextSibling();
				i++; continue;
			} else {
				return atual;
			}
		}
		return null;
	}
	
	/**
     * Insere a mensagem do canal.
     * 
     * @throws Exception
     */
	public void prepararBusData(String mensagem) throws Exception {
		logger.debug("Iniciando e preparando o BusData");
        try {
            StringGravavel output = new StringGravavel();
            String fileName = this.simtxConfig.getCaminhoXslt() + this.properties.getProperty("path.requisicao.xslt");
            
            XmlProcessador processador = new XmlProcessador();
            processador.setXml(mensagem);
            processador.setXslt(new StreamSource(new StringReader(this.repositorio.recuperarArquivo(fileName))));
            processador.setOutput(output);
            
            if(processador.getException() != null) {
                logger.warn("Erro de gravacao inicial no busData: ", processador.getException());
                throw new RuntimeException(processador.getException());
            }
            processador.transforme();
            String msg = output.toString().replaceAll("\\<\\?xml(.+?)\\?\\>", "");
            this.dadosBarramento.escrever(msg);
        }
        catch(IOException e) {
            logger.warn("Erro de gravacao inicial no busData: ", e);
            throw new RuntimeException(e);
        }
        catch(Exception e) {
        	throw e;
        }
    }
	
	/** 
	 * Grava a mensagem de resposta do Sibar no BusData.
	 * 
	 * @param mensagem
	 * @throws Exception
	 */
	public void gravarNoDadosBarramento(String mensagem) throws Exception {
		logger.debug("Gravando xml no BusData");
		mensagem = StringUtil.limpaString(mensagem);
		
        String caminho = this.simtxConfig.getCaminhoXslt() + this.properties.getProperty("path.resposta.xslt");
        String arquivoXsl = this.repositorio.recuperarArquivo(caminho);

        String mensagemTransformada = this.transformadorXsl.transformar(mensagem, arquivoXsl);
        this.dadosBarramento.escrever(mensagemTransformada);
	}
	
	/** 
	 * Grava a mensagem de resposta do Sibar no BusData.
	 * 
	 * @param mensagem
	 * @throws Exception
	 */
	public void gravarNoBusData(String mensagem, Mtxtb012VersaoTarefa tarefa) throws Exception {
		logger.debug("Gravando xml no BusData");
		mensagem = StringUtil.limpaString(mensagem);
        
        String caminho = this.simtxConfig.getCaminhoXslt() + tarefa.getDeXsltResposta();
        String arquivoXsl = this.repositorio.recuperarArquivo(caminho);
        
        String msgTransformada = this.transformadorXsl.transformar(mensagem, arquivoXsl);
        this.dadosBarramento.escrever(msgTransformada);
	}

	/**
	 * Prepara o xml de entrada do canal para enviar ao Sibar.
	 * 
	 * @param tag
	 * @return
	 * @throws Exception
	 */
	protected String preparaXmlEntradaSibar() throws Exception {
		logger.info("Preparando xml de requisicao para enviar ao Sibar");
		
		String caminho = this.simtxConfig.getCaminhoXslt() + this.versaoServicoChamado.getDeXsltRequisicao();
		String xsl = this.repositorio.recuperarArquivo(caminho);
		
		if(xsl.contains(Constantes.TAG_TRANSACAO_ORIGEM) && this.transacao.getNuNsuTransacaoPai() != 0) {
			this.dadosBarramento.escrever(recuperarTransacaoOrigemNoDadosBarramento(this.transacao.getNuNsuTransacaoPai()));
		}
		
		return this.transformadorXsl.transformar(this.dadosBarramento.getDadosLeitura(), xsl,
				new ParametroXsl(ParametroXsl.XSL_PARAM_NSU_SIMTX, String.valueOf(this.transacao.getNuNsuTransacao())),
				new ParametroXsl(ParametroXsl.XSL_PARAM_REDE_TRANSMISSORA, String.valueOf(this.canal.getNuRedeTransmissora())),
				new ParametroXsl(ParametroXsl.XSL_PARAM_SEGMENTO, String.valueOf(this.canal.getNuSegmento())),
				new ParametroXsl(ParametroXsl.XSL_PARAM_FUNCIONALIDADE, this.funcionalidades.toString()));
    }

	/**
	 * Recupera o xml da transacao origem.
	 * 
	 * @param nsu
	 * @return
	 * @throws ServicoException
	 */
	public String recuperarTransacaoOrigemNoDadosBarramento(Long nsu) throws ServicoException {
		Mtxtb014Transacao transacaoOrigem = this.fornecedorDados.buscarTransacaoOrigem(nsu);
		if(transacaoOrigem != null) {
			String xmlEntrada = transacaoOrigem.getMtxtb016IteracaoCanals().get(0).getDeRecebimento();
			String xmlSaida = transacaoOrigem.getMtxtb016IteracaoCanals().get(0).getDeRetorno();
			
			String xmlTarefas = carregarTarefasTransacao(nsu).toString();
			
			return "<TRANSACAO_ORIGEM>" + xmlEntrada + xmlSaida + xmlTarefas + "</TRANSACAO_ORIGEM>";
		}
		else {
			return "";
		}
	}
	
	/**
	 * Carrega os xmls das Tarefas.
	 * 
	 * @param nsu
	 * @throws ServicoException
	 */
	public StringBuilder carregarTarefasTransacao(Long nsu) throws ServicoException {
		try {
			List<Mtxtb015SrvcoTrnsoTrfa> tarefasExecutadas = this.fornecedorDados.buscarTarefasPorNsu(nsu);
			StringBuilder xml = new StringBuilder();
			
			for(Mtxtb015SrvcoTrnsoTrfa tarefa : tarefasExecutadas) {
				xml.append(tarefa.getDeXmlRequisicao().replaceAll("<\\?xml.*\\?>", ""));
				xml.append(tarefa.getDeXmlResposta().replaceAll("<\\?xml.*\\?>", ""));
			}
			return xml;
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.ERRO_RECUPERAR_TAREFAS);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}

	/**
	 * Salva os xml das tarefas executadas pelo Sibar.
	 * 
	 * @param dadosEntrada
	 * @param dadosSaida
	 * @param tsExecucaoTransacao
	 * @throws Exception
	 */
	protected void salvarTarefasExecutadas() throws Exception {
		logger.info("Salvando tarefas executadas");
		String mensagemSaida = "";

		for(Mtxtb003ServicoTarefa servicoTarefa : this.listaTarefaExecutar) {
			logger.debug("Tarefa [" + servicoTarefa.getMtxtb012VersaoTarefa().getId().getNuTarefa002() + "] "
					+ servicoTarefa.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoTarefa() + " ");

			String mensagemEntrada = "";
			if(!servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao().equals("-"))
				mensagemEntrada = preparaXmlComXslt(servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltRequisicao());
			
			mensagemSaida = preparaXmlComXslt(servicoTarefa.getMtxtb012VersaoTarefa().getDeXsltResposta());
			
			Mtxtb015SrvcoTrnsoTrfa transacaoTarefa = new Mtxtb015SrvcoTrnsoTrfa();
	        transacaoTarefa.setId(new Mtxtb015SrvcoTrnsoTrfaPK());
	        transacaoTarefa.getId().setNuNsuTransacao017(this.transacao.getNuNsuTransacao());
	        transacaoTarefa.getId().setNuTarefa012(servicoTarefa.getMtxtb012VersaoTarefa().getId().getNuTarefa002());
	        transacaoTarefa.getId().setNuVersaoTarefa012(servicoTarefa.getMtxtb012VersaoTarefa().getId().getNuVersaoTarefa());
	        transacaoTarefa.getId().setNuServico017(this.servicoChamado.getNuServico());
	        transacaoTarefa.getId().setNuVersaoServico017(this.versaoServicoChamado.getId().getNuVersaoServico());
	        transacaoTarefa.setTsExecucaoTransacao(DataUtil.getDataAtual());
	        transacaoTarefa.setDtReferencia(this.transacao.getDtReferencia());
	        transacaoTarefa.setDeXmlRequisicao(mensagemEntrada);
	        transacaoTarefa.setDeXmlResposta(mensagemSaida);
	        transacaoTarefa.setNsuCorp(0L);
	        Mtxtb015SrvcoTrnsoTrfa transacaoRetorno = this.fornecedorDados.atualizarTransacaoTarefa(transacaoTarefa);
	        if (transacaoRetorno == null) {
	            throw new SimtxException("MN018");
	        }
		}
    }
	
	/**
	 * Retorna o xml de acordo com o xslt.
	 * 
	 * @param xslt
	 * @return
	 * @throws Exception
	 */
	public String preparaXmlComXslt(String xslt, ParametroXsl ... parametros) throws Exception {
		logger.debug("Preparando xml com xslt");
        
        String caminho = this.simtxConfig.getCaminhoXslt() + xslt;
        String arquivoXsl = this.repositorio.recuperarArquivo(caminho);

        return this.dadosBarramento.ler(arquivoXsl, parametros);
    }
	
	public String preparaXmlRespostaComXslt(String xslt, String nsuSimtx, String origemRetorno,
			Mtxtb006Mensagem mensagem, ParametroXsl... parametros) throws Exception {
		logger.debug("Preparando xml com xslt");
        
        String caminho = this.simtxConfig.getCaminhoXslt() + xslt;
        String arquivoXsl = this.repositorio.recuperarArquivo(caminho);

        List<ParametroXsl> parametrosNovos = new ArrayList<>();
        
        for(ParametroXsl p : parametros) {
        	parametrosNovos.add(p);
        }
        if (null != mensagem) {
            parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigoRetorno()));
            parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL, mensagem.getDeMensagemNegocial()));
            parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getDeMensagemTecnica()));
            parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));	
		}
        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, origemRetorno));
        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_NSU_SIMTX, nsuSimtx));
        
        ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];
        return this.dadosBarramento.ler(arquivoXsl, parametrosNovos.toArray(pArr));
    }
	
	/**
	 * Define Parametros
	 * 
	 * @param processador
	 * @param parametros
	 */
	public void definirParametros(XmlProcessador processador, ParametroXsl[] parametros) {
		for(ParametroXsl parametro : parametros) {
			processador.getTranformer().setParameter(parametro.getChave(), parametro.getValor());
		}
	}
	
	/**
	 * Transforma o xml em outro a partir do xslt.
	 * 
	 * @param xslt
	 * @param dadosBarramento
	 * @return
	 * @throws ServicoException
	 */
	public String transformarXml(String xslt, Mtxtb014Transacao transacao, Resposta mensagem)
			throws ServicoException {
		try {
			List<ParametroXsl> parametrosNovos = new ArrayList<>();
			parametrosNovos.add(new ParametroXsl("nsuSimtx", String.valueOf(transacao.getNuNsuTransacao())));
			if(mensagem != null) {
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigo()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, mensagem.getOrigem()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL, mensagem.getMensagemNegocial()));
		        parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getMensagemTecnica()));
			}
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];
			
			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = this.repositorio.recuperarArquivo(caminhoXls);
			return transformadorXsl.transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl, parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}
	
	public String transformarXmlRespostaCanal(String xslt, Mtxtb014Transacao transacao, Resposta mensagem, RepositorioArquivo repositorio, List<ParametroXsl> parametrosNovos) throws ServicoException {
		try {
			parametrosNovos.add(new ParametroXsl("nsuSimtx", String.valueOf(transacao.getNuNsuTransacao())));
			if (mensagem != null) {
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_COD_RETORNO, mensagem.getCodigo()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ACAO_RETORNO, AcaoRetorno.recuperarAcao(mensagem.getIcTipoMensagem()).getRotulo()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_ORIGEM_RETORNO, mensagem.getOrigem()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_NEGOCIAL, mensagem.getMensagemNegocial()));
				parametrosNovos.add(new ParametroXsl(ParametroXsl.XSL_PARAM_MENSAGEM_TECNICA, mensagem.getMensagemTecnica()));
			}
			ParametroXsl[] pArr = new ParametroXsl[parametrosNovos.size()];

			String caminhoXls = this.simtxConfig.getCaminhoXslt() + xslt;
			String arquivoXsl = repositorio.recuperarArquivo(caminhoXls);
			return transformadorXsl.transformar(this.dadosBarramento.getDadosLeitura(), arquivoXsl,
					parametrosNovos.toArray(pArr));
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno, Constantes.ORIGEM_SIMTX);
		}
	}
	
	public FornecedorDados getFornecedorDados() {
		return fornecedorDados;
	}

	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
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

	public Mtxtb011VersaoServico getVersaoServicoChamado() {
		return versaoServicoChamado;
	}

	public void setVersaoServicoChamado(Mtxtb011VersaoServico versaoServicoChamado) {
		this.versaoServicoChamado = versaoServicoChamado;
	}

	public Mtxtb004Canal getCanal() {
		return canal;
	}

	public void setCanal(Mtxtb004Canal canal) {
		this.canal = canal;
	}

	public Mtxtb008MeioEntrada getMeioEntrada() {
		return meioEntrada;
	}

	public void setMeioEntrada(Mtxtb008MeioEntrada meioEntrada) {
		this.meioEntrada = meioEntrada;
	}

	public Mtxtb005ServicoCanal getServicoCanal() {
		return servicoCanal;
	}

	public void setServicoCanal(Mtxtb005ServicoCanal servicoCanal) {
		this.servicoCanal = servicoCanal;
	}

	public SimtxConfig getSimtxConfig() {
		return simtxConfig;
	}

	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}

	public RepositorioArquivo getRepositorio() {
		return repositorio;
	}

	public void setRepositorio(RepositorioArquivo repositorio) {
		this.repositorio = repositorio;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public DadosBarramento getDadosBarramento() {
		return dadosBarramento;
	}

	public TransformadorXsl getTransformadorXsl() {
		return transformadorXsl;
	}

	public void setTransformadorXsl(TransformadorXsl transformadorXsl) {
		this.transformadorXsl = transformadorXsl;
	}

	public void setDadosBarramento(DadosBarramento dadosBarramento) {
		this.dadosBarramento = dadosBarramento;
	}

}
