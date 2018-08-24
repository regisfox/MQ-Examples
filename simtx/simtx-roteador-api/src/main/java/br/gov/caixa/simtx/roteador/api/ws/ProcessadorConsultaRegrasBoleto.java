package br.gov.caixa.simtx.roteador.api.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;

import br.gov.caixa.simtx.persistencia.cache.core.FornecedorDados;
import br.gov.caixa.simtx.persistencia.constante.Constantes;
import br.gov.caixa.simtx.persistencia.constante.MensagemRetorno;
import br.gov.caixa.simtx.persistencia.core.entidade.Mtxtb006Mensagem;
import br.gov.caixa.simtx.roteador.api.util.ConstantesRoteadorWeb;
import br.gov.caixa.simtx.roteador.api.ws.matriz.LeitorMatrizConsultaRegrasBoleto;
import br.gov.caixa.simtx.roteador.api.ws.matriz.RegrasMatriz;
import br.gov.caixa.simtx.roteador.api.ws.xml.Header;
import br.gov.caixa.simtx.roteador.api.ws.xml.consulta.ConsultaRegrasBoletoDadosEntrada;
import br.gov.caixa.simtx.roteador.api.ws.xml.consulta.ConsultaRegrasBoletoDadosSaida;
import br.gov.caixa.simtx.roteador.api.ws.xml.consulta.ConsultaRegrasBoletoEntrada;
import br.gov.caixa.simtx.roteador.api.ws.xml.consulta.ConsultaRegrasBoletoSaida;
import br.gov.caixa.simtx.util.SimtxConfig;
import br.gov.caixa.simtx.util.TratadorCodigoBarras;
import br.gov.caixa.simtx.util.exception.ServicoException;
import br.gov.caixa.simtx.util.vo.CodigoBarras;

@Stateless
public class ProcessadorConsultaRegrasBoleto {
	
	private static final Logger logger = Logger.getLogger(ProcessadorConsultaRegrasBoleto.class);
	
	@Inject
	private FornecedorDados fornecedorDados;
	
	@Inject
	private LeitorMatrizConsultaRegrasBoleto leitorMatriz;
	
	@Inject
	private SimtxConfig simtxConfig;
	
	@Inject
	private TratadorCodigoBarras tratadorCodigoBarras;
	
	public static final String PATH_XSD = "validacao_entradas/SIMTX_TYPES/Consulta_Regras_Boleto/V1/Consulta_Regras_Boleto.xsd";
	
	
	public String processar(String xmlEntrada) {
		logger.info(" ==== Processo Consulta Regras Boleto (WS1) Iniciado ==== ");
		logger.info("Mensagem Recebida do SIBAR:\n" + xmlEntrada);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		Header header = new Header();
		header.setVersao("1");
		header.setOperacao("CONSULTA_REGRAS_BOLETO");
		header.setDataHora(format.format(new Date()));
		
		try { 
			ConsultaRegrasBoletoEntrada entrada = transformarXmlParaJAXB(xmlEntrada);
			
			CodigoBarras codigoBarras = retornarValoresCodigoBarras(entrada.getDados());
			
			entrada = atualizarDadosEntrada(entrada, codigoBarras);
			
			String retornoMatriz = recuperarValorMatriz(entrada);
			
			validarSaidaMatriz(retornoMatriz);
			
			return montarResposta(retornoMatriz, header, codigoBarras);
		} 
		catch (ServicoException se) {
			return tratarExcecao(se, header);
		}
	}
	
	/**
	 * Transforma o Xml recebido em classes JAXB.
	 * 
	 * @param entrada
	 * @return
	 * @throws ServicoException
	 */
	public ConsultaRegrasBoletoEntrada transformarXmlParaJAXB(String entrada) throws ServicoException {
		logger.info("Transformando xml recebido para classes JAXB");
		try {
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
			Schema schema = sf.newSchema(new File(this.simtxConfig.getCaminhoXsd() + PATH_XSD));

			JAXBContext context = JAXBContext.newInstance(ConsultaRegrasBoletoEntrada.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			unmarshaller.setSchema(schema);
			
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(entrada.getBytes())),
					ConsultaRegrasBoletoEntrada.class).getValue();
		} 
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.WS_LAYOUT_INVALIDO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		} 
	}
	
	public CodigoBarras retornarValoresCodigoBarras(ConsultaRegrasBoletoDadosEntrada dadosEntrada) {
		try {
			String tipoContingencia = dadosEntrada.getTipoContingencia();
			logger.info("Flag Contingencia: "+tipoContingencia);
			
			String flagNovaCobranca = dadosEntrada.getNovaPlataformaCobranca();
			logger.info("Flag Nova Plataforma Cobranca: "+flagNovaCobranca);
			
			if (tipoContingencia.equals(ConstantesRoteadorWeb.CONTINGENCIA_CIP)
					|| flagNovaCobranca.equals(ConstantesRoteadorWeb.NAO)) {
				
				return this.tratadorCodigoBarras.retornarValoresCodigoBarras(dadosEntrada.getCodigoBarras());
			}
			else {
				return null;
			}
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	public ConsultaRegrasBoletoEntrada atualizarDadosEntrada(ConsultaRegrasBoletoEntrada entrada, CodigoBarras codigoBarras) {
		if(codigoBarras != null) {
			ConsultaRegrasBoletoDadosEntrada dados = entrada.getDados();
			dados.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd").format(codigoBarras.getDataVencimento()));
			entrada.setDados(dados);
		}
		return entrada;
	}
	
	/**
	 * Recupera o resultado da matriz de acordo com os dados de entrada.
	 * 
	 * @param dadosEntrada
	 * @return
	 * @throws ServicoException
	 */
	public String recuperarValorMatriz(ConsultaRegrasBoletoEntrada dadosEntrada) throws ServicoException {
		logger.info("Recuperando valor da matriz");
		try {
			RegrasMatriz regrasMatriz = new RegrasMatriz();
			String entradaMatriz = regrasMatriz.tratarParametrosEntradaWs1(dadosEntrada.getDados());
			logger.info("Entrada para matriz: "+entradaMatriz);
			
			String retornoMatriz = this.leitorMatriz.processaRegraBoletoEntrada(entradaMatriz);
			logger.info("Saida da matriz: " + retornoMatriz);
			return retornoMatriz;
		} 
		catch (Exception pe) {
			logger.error(pe);
			Mtxtb006Mensagem mensagem = this.fornecedorDados
					.buscarMensagem(MensagemRetorno.WS_NAO_RECUPEROU_DADOS_MATRIZ);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Valida a saida da matriz.
	 * 
	 * @param retornoMatriz
	 * @throws ServicoException
	 */
	public void validarSaidaMatriz(String retornoMatriz) throws ServicoException {
		if (retornoMatriz.equals(ConstantesRoteadorWeb.NAO_RECEBER)) {
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.WS1_NAO_RECEBER_BOLETO);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
 	
	/**
	 * Monta a resposta para o SIBAR.
	 * 
	 * @param retornoMatriz
	 * @param e
	 * @return
	 * @throws JAXBException 
	 */
	public String montarResposta(String retornoMatriz, Header header, CodigoBarras codigoBarras) throws ServicoException {
		try {
			logger.info("Montando o xml de resposta");
			ConsultaRegrasBoletoSaida saida = new ConsultaRegrasBoletoSaida();
			
			saida.setHeader(header);
			saida.setOrigemRetorno(ConstantesRoteadorWeb.SISTEMA_ORIGEM);
			
			ConsultaRegrasBoletoDadosSaida dadosSaida = new ConsultaRegrasBoletoDadosSaida();
			dadosSaida.setFormaRecebimento(retornoMatriz);
			if(codigoBarras != null) {
				dadosSaida.setDataVencimento(codigoBarras.getDataVencimento());
				dadosSaida.setValorNominal(codigoBarras.getValorTitulo());
			}
			saida.setDados(dadosSaida);
			
			Mtxtb006Mensagem mensagem = this.fornecedorDados.buscarMensagem(MensagemRetorno.SUCESSO);
			saida.setCodRetorno(mensagem.getCodigoRetorno());
			saida.setMsgRetorno(mensagem.getDeMensagemNegocial());
			
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
			Schema schema = sf.newSchema(new File(this.simtxConfig.getCaminhoXsd() + PATH_XSD));
			
			JAXBContext context = JAXBContext.newInstance(ConsultaRegrasBoletoSaida.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setSchema(schema);
			
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(byteOutputStream);
			marshaller.marshal(saida, result);
			
			String resposta = byteOutputStream.toString();
			
			logger.info("Enviando resposta para o SIBAR:\n"+resposta);
			logger.info(" ==== Processo Consulta Regras Boleto (WS1) Finalizado ==== ");
			return resposta;
		}
		catch (Exception e) {
			logger.error(e);
			Mtxtb006Mensagem mensagem = new Mtxtb006Mensagem();
			mensagem.setCodigoRetorno(ConstantesRoteadorWeb.ERRO_CONSULTA);
			mensagem.setDeMensagemNegocial(ConstantesRoteadorWeb.MSG_GENERICA);
			throw new ServicoException(mensagem, Constantes.ORIGEM_SIMTX);
		}
	}
	
	/**
	 * Monta a mensagem de excecao para enviar ao SIBAR.
	 * 
	 * @param e
	 * @param header
	 * @return
	 */
	public String tratarExcecao(ServicoException e, Header header) {
		logger.info("Tratando excecao");
		try {
			ConsultaRegrasBoletoSaida saida = new ConsultaRegrasBoletoSaida();
			
			saida.setHeader(header);
			saida.setDados(new ConsultaRegrasBoletoDadosSaida());
			saida.setCodRetorno(e.getMensagem().getCoMensagem());
			saida.setMsgRetorno(e.getMensagem().getDeMensagemTecnica());
			saida.setOrigemRetorno(e.getSistemaOrigem());
			
			JAXBContext context = JAXBContext.newInstance(ConsultaRegrasBoletoSaida.class);
			Marshaller marshaller = context.createMarshaller();
			
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(byteOutputStream);
			marshaller.marshal(saida, result);
			
			String resposta = byteOutputStream.toString();
			logger.info("Enviando resposta para o SIBAR:\n"+resposta);
			logger.info(" ==== Processo Consulta Regras Boleto (WS1) Finalizado ==== ");
			return resposta;
		} 
		catch (Exception e2) {
			logger.error(e2);
			return ConstantesRoteadorWeb.MSG_GENERICA;
		}
	}

	
	public void setFornecedorDados(FornecedorDados fornecedorDados) {
		this.fornecedorDados = fornecedorDados;
	}

	public void setSimtxConfig(SimtxConfig simtxConfig) {
		this.simtxConfig = simtxConfig;
	}

	public void setLeitorMatriz(LeitorMatrizConsultaRegrasBoleto leitorMatriz) {
		this.leitorMatriz = leitorMatriz;
	}

	public void setTratadorCodigoBarras(TratadorCodigoBarras tratadorCodigoBarras) {
		this.tratadorCodigoBarras = tratadorCodigoBarras;
	}
	
	
}
