package br.gov.caixa.simtx.util.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb003ServicoTarefa;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagem;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb007TarefaMensagemPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb011VersaoServico;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFila;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb024TarefaFilaPK;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegras;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb026ServicoTarefaRegrasPK;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.gerenciador.Mensagem;

@Stateless
public class GeradorPassosMigrado {
	
	private static final Logger logger = Logger.getLogger(GeradorPassosMigrado.class);
	
	@Inject
	private FornecedorDados fornecedorDados;
	
	@Inject
	private Mensagem mensagemServidor;
	
	
	/**
	 * Gera os passos para o Servico Migrado.
	 * 
	 * @param xml
	 * @param servicoChamado
	 * @param versaoServicoChamado
	 * @param listaTarefaExecutar
	 * @return
	 * @throws Exception
	 */
	public String gerarPassos(String xml, Mtxtb011VersaoServico versaoServicoChamado,
			List<Mtxtb003ServicoTarefa> listaTarefaExecutar) throws ServicoException {
		try {
			
			if (versaoServicoChamado.isMigrado()) {
				logger.info("Gerando o(s) passo(s) para o servico");
				Document tagOrquestracao = criaTagOrquestracao(versaoServicoChamado, listaTarefaExecutar);
				return adicionaNovaTag(xml, tagOrquestracao);	
			}
			
			return xml;
		
		} 
		catch (Exception e) {
			logger.error(this.mensagemServidor.recuperarMensagem("erro.tag.orquestracao"), e);
			Mtxtb006Mensagem mensagemErroInterno = this.fornecedorDados.buscarMensagem(MensagemRetorno.ERRO_INTERNO);
			throw new ServicoException(mensagemErroInterno,Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Cria TAG Orquestracao.
	 * 
	 * @param versaoServicoChamado
	 * @param listaTarefaExecutar
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws Exception
	 */
	public Document criaTagOrquestracao(Mtxtb011VersaoServico versaoServicoChamado,
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
    		logger.info("Tarefa/Passo ["+servicoTarefa.getId().getNuTarefa012()+"] "
					+ servicoTarefa.getMtxtb012VersaoTarefa().getMtxtb002Tarefa().getNoTarefa()+"");
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
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws UnsupportedEncodingException 
	 * @throws TransformerException 
	 */
	public String adicionaNovaTag(String xml, Document novaTag)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		logger.debug("Adicionando nova TAG no xml");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document xmlDocument;
		docBuilder = docFactory.newDocumentBuilder();
		xmlDocument = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
	
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
	
	/**
	 * Adiciona TAG.
	 * 
	 * @param novaTag
	 * @param header
	 * @param xml
	 */
	private void adicionaTagControleTransacao(Node novaTag, Node header, Document xml) {
		logger.info("adicionando tag controle transacao");
		for ( int i = 0; i < novaTag.getChildNodes().getLength();  i++) {
			Node nodeCriado = novaTag.getChildNodes().item(i);
			Node tagDepoisDoHeader = getNextTag(header);
			header.getParentNode().insertBefore(xml.importNode(nodeCriado, true), tagDepoisDoHeader);
		}
	}
	
	/**
	 * Pega a proxima TAG.
	 * 
	 * @param node
	 * @return
	 */
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

	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}

}
